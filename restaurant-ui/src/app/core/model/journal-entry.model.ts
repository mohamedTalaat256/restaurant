export interface JournalEntry {
  id: number;
  entryNumber: string;
  entryDate: string;
  description: string;
  reference: string;
  source: string;
  items: JournalItem[];
}

export interface JournalItem {
  accountId: number;
  accountName: string;
  accountCode: string;
  debit: number;
  credit: number;
  costCenterId: number;
  costCenterName: string;
}

export interface ManualJournalEntryRequest {
  entryDate: string;
  description: string;
  reference: string;
  items: ManualJournalItemRequest[];
}

export interface ManualJournalItemRequest {
  accountId: number;
  debit: number;
  credit: number;
  costCenterId?: number;
}
