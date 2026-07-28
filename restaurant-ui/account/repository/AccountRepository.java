package com.mtalaat.restaurant.modules.account.repository;

import com.mtalaat.restaurant.modules.account.entity.Account;
import com.mtalaat.restaurant.modules.account.enums.AccountType;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByCode(String code);

    // جلب أعلى كود فرعي تحت حساب أب معين لتوليد الكود التالي تلقائياً
    @Query("SELECT MAX(a.code) FROM Account a WHERE a.parent.id = :parentId")
    Optional<String> findMaxCodeByParentId(@Param("parentId") Long parentId);

    Optional<Account> findAccountByCode(String accountCode);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Account a WHERE a.id = :id")
    Optional<Account> findByIdForUpdate(@Param("id") Long id);

    List<Account> findByTypeIn(List<AccountType> types);

    @Query("SELECT a FROM Account a WHERE a.type IN :types AND a.balance <> 0 AND a.allowTransaction = true")
    List<Account> findByTypeInAndBalanceNotZero(@Param("types") List<AccountType> types);

    List<Account> findByAllowTransactionTrue();
}