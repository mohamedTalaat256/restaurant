package com.mtalaat.restaurant.modules.account.service;

import com.mtalaat.restaurant.modules.account.dto.AccountDTO;
import com.mtalaat.restaurant.modules.account.dto.JournalEntryDTO;
import com.mtalaat.restaurant.modules.account.dto.JournalItemDTO;
import com.mtalaat.restaurant.modules.account.entity.Account;
import com.mtalaat.restaurant.modules.account.entity.JournalEntry;
import com.mtalaat.restaurant.modules.account.entity.JournalItem;
import com.mtalaat.restaurant.modules.account.enums.AccountType;
import com.mtalaat.restaurant.modules.account.mapper.AccountMapper;
import com.mtalaat.restaurant.modules.account.mapper.JournalMapper;
import com.mtalaat.restaurant.modules.account.repository.AccountRepository;
import com.mtalaat.restaurant.modules.account.repository.JournalEntryRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final JournalEntryRepository journalEntryRepository;
    private final AccountMapper accountMapper;
    private final JournalMapper journalMapper;



    // داخل كلاس AccountService...

    /**
     * 1. جلب جميع الحسابات كقائمة مسطحة عادية (Flat List) مرتبة حسب الكود
     * مناسبة جداً لـ Dropdown الاختيار في الفواتير أو القيود
     */
    @Transactional(readOnly = true)
    public List<AccountDTO> getAllAccountsFlat() {
        return accountRepository.findAll(Sort.by(Sort.Direction.DESC, "updatedAt")).stream()
                .map(accountMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * 2. جلب جميع الحسابات على شكل هيكل شجري متداخل (Hierarchical Tree Structure)
     * ممتازة جداً لشاشة "دليل الحسابات / شجرة الحسابات" في لوحة التحكم
     */
    @Transactional(readOnly = true)
    public List<AccountDTO> getAllAccountsAsTree() {
        // جلب كل الحسابات من قاعدة البيانات أولاً
        List<Account> allAccounts = accountRepository.findAll();

        // تحويلهم جميعاً إلى DTOs وتجميعهم في Map لتسهيل الوصول والربط برمجياً بالـ ID
        Map<Long, AccountDTO> dtoMap = allAccounts.stream()
                .map(accountMapper::toDTO)
                .collect(Collectors.toMap(AccountDTO::getId, dto -> dto));

        List<AccountDTO> rootAccounts = new ArrayList<>();

        // بناء العلاقات (ربط الأبناء بالآباء) داخل الـ Memory بدون تكرار استعلامات الـ DB
        for (Account account : allAccounts) {
            AccountDTO currentDto = dtoMap.get(account.getId());

            if (account.getParent() == null) {
                // إذا لم يكن له أب، إذن هو حساب رئيسي (Level 1) مثل (الأصول، الخصوم...)
                rootAccounts.add(currentDto);
            } else {
                // إذا كان له أب، نجلب الـ DTO الخاص بالأب من الـ Map ونضيف الحالي في قائمة أبنائه
                AccountDTO parentDto = dtoMap.get(account.getParent().getId());
                if (parentDto != null) {
                    if (parentDto.getChildren() == null) {
                        parentDto.setChildren(new ArrayList<>());
                    }
                    parentDto.getChildren().add(currentDto);
                }
            }
        }

        // ترتيب الحسابات الرئيسية بحسب الكود (1، 2، 3، 4، 5) وترتيب أبنائهم داخلياً
        rootAccounts.sort((a1, a2) -> a1.getCode().compareTo(a2.getCode()));
        sortTreeChildren(rootAccounts);

        return rootAccounts;
    }



    public Account findAccountByCode(String accountCode){
        return accountRepository.findAccountByCode(accountCode).orElseThrow();

    }

    // ميثود مساعدة لترتيب الأبناء داخلياً بشكل ريكرسف (Recursive)
    private void sortTreeChildren(List<AccountDTO> dtos) {
        for (AccountDTO dto : dtos) {
            if (dto.getChildren() != null && !dto.getChildren().isEmpty()) {
                dto.getChildren().sort((c1, c2) -> c1.getCode().compareTo(c2.getCode()));
                sortTreeChildren(dto.getChildren()); // النزول للمستوى الأدنى في الشجرة
            }
        }
    }


    /**
     * إنشاء حساب فرعي تلقائياً (مثلاً عند إضافة مورد أو عميل جديد)
     */
    @Transactional
    public Account createSubAccount(String parentCode, String subAccountName) {
        Account parentAccount = accountRepository.findByCode(parentCode)
                .orElseThrow(() -> new RuntimeException("msg_parent_account_code_not_provided: " + parentCode));

        // توليد الكود التسلسلي التالي تلقائياً
        String maxChildCode = accountRepository.findMaxCodeByParentId(parentAccount.getId()).orElse(null);
        String nextCode;
        if (maxChildCode == null) {
            nextCode = parentAccount.getCode() + "0001"; // أول حساب فرعي
        } else {
            long numericCode = Long.parseLong(maxChildCode) + 1;
            nextCode = String.valueOf(numericCode);
        }

        Account subAccount = Account.builder()
                .code(nextCode)
                .name(subAccountName)
                .type(parentAccount.getType())
                .parent(parentAccount)
                .allowTransaction(true) // الحسابات الفرعية يُسمح عليها بالحركات المالية
                .status(true)
                .balance(BigDecimal.ZERO)
                .build();

        return accountRepository.save(subAccount);
    }

    /**
     * تسجيل قيد يومية جديد وتحديث الأرصدة تلقائياً
     */
    @Transactional
    public JournalEntryDTO createJournalEntry(JournalEntryDTO dto) {
        // 1. التحقق من توازن القيد المحاسبي (مجموع المدين = مجموع الدائن)
        BigDecimal totalDebit = dto.getItems().stream().map(JournalItemDTO::getDebit).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalCredit = dto.getItems().stream().map(JournalItemDTO::getCredit).reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalDebit.compareTo(totalCredit) != 0) {
            throw new RuntimeException("فشل تسجيل القيد: يجب أن يكون إجمالي الطرف المدين مساوياً لإجمالي الطرف الدائن.");
        }

        // 2. توليد رقم قيد تلقائي
        String prefix = "JV-" + LocalDateTime.now().getYear() + "-";
        long count = journalEntryRepository.countByEntryNumberStartingWith(prefix) + 1;
        String entryNumber = prefix + String.format("%04d", count);

        // 3. بناء رأس القيد
        JournalEntry entry = JournalEntry.builder()
                .entryNumber(entryNumber)
                .entryDate(dto.getEntryDate() != null ? dto.getEntryDate() : LocalDateTime.now())
                .description(dto.getDescription())
                .reference(dto.getReference())
                .build();

        // 4. معالجة تفاصيل السطور وتحديث أرصدة الحسابات المتأثرة
        for (JournalItemDTO itemDto : dto.getItems()) {
            Account account = accountRepository.findById(itemDto.getAccountId())
                    .orElseThrow(() -> new RuntimeException("الحساب المالي غير موجود بـ ID: " + itemDto.getAccountId()));

            if (!account.getAllowTransaction()) {
                throw new RuntimeException("لا يمكن القيد على الحساب الرئيسي التجميعي: " + account.getName());
            }

            JournalItem item = JournalItem.builder()
                    .account(account)
                    .debit(itemDto.getDebit())
                    .credit(itemDto.getCredit())
                    .build();

            entry.addItem(item);

            // تحديث معادلة رصيد الحساب بناءً على طبيعة نوع الحساب
            // (الأصول والمصروفات تزيد بالمدين وتقل بالدائن، وباقي الحسابات العكس)
            BigDecimal modifier = BigDecimal.ZERO;
            if (account.getType() == AccountType.ASSET || account.getType() == AccountType.EXPENSE) {
                modifier = itemDto.getDebit().subtract(itemDto.getCredit());
            } else {
                modifier = itemDto.getCredit().subtract(itemDto.getDebit());
            }

            account.setBalance(account.getBalance().add(modifier));
            accountRepository.save(account);
        }

        JournalEntry savedEntry = journalEntryRepository.save(entry);
        return journalMapper.toDTO(savedEntry);
    }
}