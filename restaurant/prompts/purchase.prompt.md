أنا أقوم ببناء تطبيق لإدارة المطاعم (Restaurant Management/ERP System) باستخدام Spring Boot و JPA. 
لدي حالياً الـ Entity التالية الخاصة بـ فاتورة الشراء (Purchase):

@Entity
@Table(name = "purchases")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Purchase extends BaseEntity {

    @Column(name = "invoice_number", nullable = false)
    private String invoiceNumber;

    private PaymentMethod paymentMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @Column(name = "purchase_date", nullable = false)
    private LocalDate purchaseDate;

    @Column(name = "expiry_date") // محتاجين ننقل ده
    private LocalDate expiryDate;

    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;

    @Column(name = "paid_amount", nullable = false)
    private Double paidAmount;

    @Column(name = "note")
    private String note;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "purchase_id")
    private List<PurchaseItem> items = new ArrayList<>();
}

أريد منك إجراء التعديلات التالية على الـ Models وكتابة الـ Service Layer لتطبيق البزنس لوجيك الخاص بالأنظمة الحقيقية.

المطلوب بالتفصيل:

1. تعديل الـ Entities:
- نقل حقل تاريخ الصلاحية `expiryDate` وحقل تاريخ الإنتاج `productionDate` من الـ Purchase Entity إلى الـ PurchaseItem Entity (لأن كل صنف له تاريخ صلاحية مختلف).
- إضافة حقل الحالة `status` في الـ Purchase (يكون Enum يشمل: DRAFT, APPROVED, VOIDED).
- إضافة قيد فريد (Unique Constraint) مركب بين `supplier_id` و `invoice_number` لمنع تكرار رقم الفاتورة لنفس المورد.
- إنشاء Entity جديدة لكشف حساب المورد باسم `SupplierLedger` تحتوي على (supplier, transactionDate, transactionType [PURCHASE, PURCHASE_EDIT, VOIDED], referenceId, debit, credit, runningBalance).

2. بناء كود الـ Service Layer لتحديث المخزن والحسابات:
أريد ميثود `approvePurchase(Long purchaseId)` تقوم بالآتي داخل `@Transactional`:
- تغيير حالة الفاتورة إلى APPROVED.
- زيادة كميات الـ Ingredients في المخزن بناءً على الـ PurchaseItems.
- حساب متوسط التكلفة الجديد (Moving Average Cost) لكل Ingredient وتحديث سعره في جدول الـ Ingredient.
- تسجيل حركة في جدول الـ SupplierLedger (قيمة الـ credit تساوي المتبقي من الفاتورة: totalAmount - paidAmount).
- خصم الـ paidAmount من خزينة المطعم (تخيل وجود ميثود وهمية في CashService).

3. هندسة لوجيك "تعديل الفاتورة" (Update Invoice Logic):
أريد ميثود `updatePurchase(Long purchaseId, PurchaseDto updatedDto)` تعتمد على طريقة "عكس القيود وإلغاء الفاتورة القديمة" (Void & Replace Method) وهي الأأمن محاسبياً:
- إذا كانت الفاتورة القديمة APPROVED، قم بعمل ميثود فرعية `voidPurchase(Long purchaseId)` لعمل الآتي:
  * تغيير حالة الفاتورة القديمة إلى VOIDED.
  * عكس حركة المخزن (خصم الكميات التي دخلت سابقاً بالكامل).
  * عكس حركة الحسابات في الـ SupplierLedger (عمل حركة تعوض الـ credit القديم).
  * إرجاع الكاش المصروف سابقاً إلى الخزينة.
- بعد إلغاء القديمة، قم بإنشاء سجل فاتورة جديد تماماً (New Version) بالبيانات المعدلة من الـ Dto وتطبيق لوجيك الـ approvePurchase الجديد عليها.

رجاءً قم بكتابة كود Java نظيف، منظم، مع التعامل مع الـ Exceptions (مثل عدم وجود كمية كافية عند العكس)، واستخدام الـ Best Practices لـ Spring Boot 3.
