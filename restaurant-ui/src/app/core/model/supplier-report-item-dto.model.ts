export interface SupplierReportItemDto {
  entryNumber: string;
  date: string;
  description: string;
  reference: string;
  debit: number;
  credit: number;
  runningBalance: number;
}

export interface SupplierStatementReport {
  supplierAccountId: number;
  supplierName: string;
  fromDate: string;
  toDate: string;
  openingBalance: number;
  items: SupplierReportItemDto[];
  closingBalance: number;
}
