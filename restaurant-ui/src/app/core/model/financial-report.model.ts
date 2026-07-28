export interface GeneralLedgerReport {
  accountId: number;
  accountCode: string;
  accountName: string;
  accountType: string;
  fromDate: string;
  toDate: string;
  openingBalance: number;
  totalDebit: number;
  totalCredit: number;
  closingBalance: number;
  lines: GeneralLedgerLine[];
}

export interface GeneralLedgerLine {
  date: string;
  entryNumber: string;
  description: string;
  reference: string;
  debit: number;
  credit: number;
  runningBalance: number;
  costCenterName: string;
}

export interface TrialBalance {
  fromDate: string;
  toDate: string;
  lines: TrialBalanceLine[];
  totalOpeningDebit: number;
  totalOpeningCredit: number;
  totalPeriodDebit: number;
  totalPeriodCredit: number;
  totalClosingDebit: number;
  totalClosingCredit: number;
}

export interface TrialBalanceLine {
  accountId: number;
  accountCode: string;
  accountName: string;
  accountType: string;
  openingBalance: number;
  totalDebit: number;
  totalCredit: number;
  closingBalance: number;
}

export interface ProfitLoss {
  fromDate: string;
  toDate: string;
  revenueAccounts: ProfitLossLine[];
  totalRevenue: number;
  cogsAccounts: ProfitLossLine[];
  totalCogs: number;
  grossProfit: number;
  operatingExpenseAccounts: ProfitLossLine[];
  totalOperatingExpenses: number;
  netProfit: number;
}

export interface ProfitLossLine {
  accountId: number;
  accountCode: string;
  accountName: string;
  amount: number;
}

export interface BalanceSheet {
  asOfDate: string;
  assets: BalanceSheetSection;
  liabilities: BalanceSheetSection;
  equity: BalanceSheetSection;
  totalLiabilitiesAndEquity: number;
  isBalanced: boolean;
}

export interface BalanceSheetSection {
  sectionName: string;
  accounts: BalanceSheetLine[];
  sectionTotal: number;
}

export interface BalanceSheetLine {
  accountId: number;
  accountCode: string;
  accountName: string;
  balance: number;
}

export interface YearEndClosingResult {
  fiscalYear: number;
  closingDate: string;
  closingEntryNumber: string;
  totalRevenue: number;
  totalExpenses: number;
  netIncome: number;
  accountsClosed: number;
}
