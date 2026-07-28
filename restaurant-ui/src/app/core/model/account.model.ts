export interface Account {
  id: number;
  code: string;
  name: string;
  type: 'ASSET' | 'LIABILITY' | 'EQUITY' | 'REVENUE' | 'EXPENSE';
  allowTransaction: boolean;
  status: boolean;
  balance: number;
  parentId?: number;
  parentName?: string;
  children?: Account[];
}
