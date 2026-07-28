package com.mtalaat.restaurant.modules.account.enums;

public enum FinancialTransactionType {
    SUPPLIER_PAYMENT,      // سداد مبلغ لمورد (الخزنة دائن، المورد مدين)
    CUSTOMER_COLLECTION,   // تحصيل مبلغ من عميل (الخزنة مدين، العميل دائن)
    OPERATIONAL_EXPENSE,   // سحب مبلغ نظير مصروفات - صيانة، إيجار، كهرباء (الخزنة دائن، المصروف مدين)
    EMPLOYEE_ADVANCE,      // صرف سلفة لموظف (الخزنة دائن، سلف الموظفين مدين)
    SALARY_PAYMENT,        // صرف رواتب الموظفين (الخزنة دائن، مصروف الرواتب مدين)
    CAPITAL_INJECTION,     // تغذية رأس المال / إيداع من المالك (الخزنة مدين، رأس المال دائن)
    INTERNAL_TRANSFER      // تحويل داخلي بين الحسابات - مثلاً من خزنة الكاشير للخزنة الرئيسية
}