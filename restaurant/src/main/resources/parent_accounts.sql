-- ==========================================
-- LEVEL 1: الحسابات الرئيسية (الروافد الخمسة)
-- ==========================================
INSERT INTO acc_accounts (code, name, type, allow_transaction, status, balance, parent_id)
VALUES ('1', 'الأصول', 'ASSET', 0, 1, 0.00, NULL);
SET @asset_id = LAST_INSERT_ID();

INSERT INTO acc_accounts (code, name, type, allow_transaction, status, balance, parent_id)
VALUES ('2', 'الخصوم (الالتزامات)', 'LIABILITY', 0, 1, 0.00, NULL);
SET @liability_id = LAST_INSERT_ID();

INSERT INTO acc_accounts (code, name, type, allow_transaction, status, balance, parent_id)
VALUES ('3', 'حقوق الملكية', 'EQUITY', 0, 1, 0.00, NULL);
SET @equity_id = LAST_INSERT_ID();

INSERT INTO acc_accounts (code, name, type, allow_transaction, status, balance, parent_id)
VALUES ('4', 'الإيرادات', 'REVENUE', 0, 1, 0.00, NULL);
SET @revenue_id = LAST_INSERT_ID();

INSERT INTO acc_accounts (code, name, type, allow_transaction, status, balance, parent_id)
VALUES ('5', 'المصروفات', 'EXPENSE', 0, 1, 0.00, NULL);
SET @expense_id = LAST_INSERT_ID();


-- ==========================================
-- LEVEL 2 & 3: تفريع الأصول (ASSETS)
-- ==========================================
INSERT INTO acc_accounts (code, name, type, allow_transaction, status, balance, parent_id)
VALUES ('11', 'الأصول المتداولة', 'ASSET', 0, 1, 0.00, @asset_id);
SET @asset_current_id = LAST_INSERT_ID();

INSERT INTO acc_accounts (code, name, type, allow_transaction, status, balance, parent_id)
VALUES ('1101', 'النقدية وما في حكمها', 'ASSET', 0, 1, 0.00, @asset_current_id);
SET @cash_and_banks_id = LAST_INSERT_ID();

INSERT INTO acc_accounts (code, name, type, allow_transaction, status, balance, parent_id)
VALUES ('1102', 'العملاء والمدينون', 'ASSET', 0, 1, 0.00, @asset_current_id);

INSERT INTO acc_accounts (code, name, type, allow_transaction, status, balance, parent_id)
VALUES ('1103', 'المخزون', 'ASSET', 0, 1, 0.00, @asset_current_id);
SET @inventory_id = LAST_INSERT_ID();


INSERT INTO acc_accounts (code, name, type, allow_transaction, status, balance, parent_id)
VALUES ('1104', 'سلف وعهد الموظفين', 'ASSET', 0, 1, 0.00, @asset_current_id);

INSERT INTO acc_accounts (code, name, type, allow_transaction, status, balance, parent_id)
VALUES ('12', 'الأصول الثابتة', 'ASSET', 0, 1, 0.00, @asset_id);
SET @asset_fixed_id = LAST_INSERT_ID();

INSERT INTO acc_accounts (code, name, type, allow_transaction, status, balance, parent_id)
VALUES ('1201', 'الأجهزة والمعدات والأثاث', 'ASSET', 0, 1, 0.00, @asset_fixed_id);


-- ==========================================
-- LEVEL 2 & 3: تفريع الخصوم (LIABILITIES)
-- ==========================================
INSERT INTO acc_accounts (code, name, type, allow_transaction, status, balance, parent_id)
VALUES ('21', 'الالتزامات المتداولة', 'LIABILITY', 0, 1, 0.00, @liability_id);
SET @liability_current_id = LAST_INSERT_ID();

INSERT INTO acc_accounts (code, name, type, allow_transaction, status, balance, parent_id)
VALUES ('2101', 'الموردون والدائنون', 'LIABILITY', 0, 1, 0.00, @liability_current_id);

INSERT INTO acc_accounts (code, name, type, allow_transaction, status, balance, parent_id)
VALUES ('2102', 'الأجور والرواتب المستحقة', 'LIABILITY', 0, 1, 0.00, @liability_current_id);


-- ==========================================
-- LEVEL 2 & 3: تفريع حقوق الملكية (EQUITY)
-- ==========================================
INSERT INTO acc_accounts (code, name, type, allow_transaction, status, balance, parent_id)
VALUES ('31', 'حقوق الملكية للملاك', 'EQUITY', 0, 1, 0.00, @equity_id);
SET @equity_owners_id = LAST_INSERT_ID();

INSERT INTO acc_accounts (code, name, type, allow_transaction, status, balance, parent_id)
VALUES ('3101', 'رأس المال المدفوع', 'EQUITY', 0, 1, 0.00, @equity_owners_id);

INSERT INTO acc_accounts (code, name, type, allow_transaction, status, balance, parent_id)
VALUES ('3102', 'الأرباح والخسائر المرحلية', 'EQUITY', 0, 1, 0.00, @equity_owners_id);


-- ==========================================
-- LEVEL 2 & 3: تفريع الإيرادات (REVENUE)
-- ==========================================
INSERT INTO acc_accounts (code, name, type, allow_transaction, status, balance, parent_id)
VALUES ('41', 'إيرادات النشاط الجاري', 'REVENUE', 0, 1, 0.00, @revenue_id);
SET @revenue_current_id = LAST_INSERT_ID();

INSERT INTO acc_accounts (code, name, type, allow_transaction, status, balance, parent_id)
VALUES ('4101', 'مبيعات المطعم', 'REVENUE', 0, 1, 0.00, @revenue_current_id);


-- ==========================================
-- LEVEL 2 & 3: تفريع المصروفات (EXPENSE)
-- ==========================================
INSERT INTO acc_accounts (code, name, type, allow_transaction, status, balance, parent_id)
VALUES ('51', 'تكلفة الحصول على الإيراد (COGS)', 'EXPENSE', 0, 1, 0.00, @expense_id);
SET @cogs_id = LAST_INSERT_ID();

INSERT INTO acc_accounts (code, name, type, allow_transaction, status, balance, parent_id)
VALUES ('5101', 'تكلفة المواد الخام والأغذية', 'EXPENSE', 0, 1, 0.00, @cogs_id);

INSERT INTO acc_accounts (code, name, type, allow_transaction, status, balance, parent_id)
VALUES ('52', 'المصروفات التشغيلية', 'EXPENSE', 0, 1, 0.00, @expense_id);
SET @operating_expense_id = LAST_INSERT_ID();

INSERT INTO acc_accounts (code, name, type, allow_transaction, status, balance, parent_id)
VALUES ('5201', 'مصروفات المنافع والطاقة', 'EXPENSE', 0, 1, 0.00, @operating_expense_id);

INSERT INTO acc_accounts (code, name, type, allow_transaction, status, balance, parent_id)
VALUES ('5202', 'مصروف الإيجار', 'EXPENSE', 0, 1, 0.00, @operating_expense_id);

INSERT INTO acc_accounts (code, name, type, allow_transaction, status, balance, parent_id)
VALUES ('5203', 'مصروفات أدوات ومواد استهلاكية', 'EXPENSE', 0, 1, 0.00, @operating_expense_id);

INSERT INTO acc_accounts (code, name, type, allow_transaction, status, balance, parent_id)
VALUES ('53', 'المصروفات الإدارية والعمومية', 'EXPENSE', 0, 1, 0.00, @expense_id);
SET @admin_expense_id = LAST_INSERT_ID();

INSERT INTO acc_accounts (code, name, type, allow_transaction, status, balance, parent_id)
VALUES ('5301', 'مصروفات الأجور والرواتب', 'EXPENSE', 0, 1, 0.00, @admin_expense_id);

INSERT INTO acc_accounts (code, name, type, allow_transaction, status, balance, parent_id)
VALUES ('5302', 'مصروف الهالك والتوالف', 'EXPENSE', 0, 1, 0.00, @admin_expense_id);

INSERT INTO acc_accounts (code, name, type, allow_transaction, status, balance, parent_id)
VALUES ('5303', 'مصروفات الدعاية والتسويق', 'EXPENSE', 0, 1, 0.00, @admin_expense_id);





INSERT INTO `restaurant_db`.`acc_accounts` (`allow_transaction`, `balance`, `code`, `name`, `status`, `type`, `parent_id`)
VALUES (b'1', 0.00, '11030001', 'مخزن المواد الخام والأغذية', b'1', 'ASSET', @cash_and_banks_id);

INSERT INTO `restaurant_db`.`acc_accounts` (`allow_transaction`, `balance`, `code`, `name`, `status`, `type`, `parent_id`)
VALUES (b'1', 0.00, '11010001', 'الخزينة الرئيسية', b'1', 'ASSET', @inventory_id);