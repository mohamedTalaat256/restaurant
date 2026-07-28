export interface Supplier {
  id: number;
  name: string;
  email?: string;
  phone?: string;
  address?: string;
  accountId: number;
  status: boolean;
}
