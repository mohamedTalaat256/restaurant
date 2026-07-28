import { CustomerType } from './customer-type.model';

export interface Customer {
  id: number;
  name: string;
  email: string;
  phone: string;
  address: string;
  favoriteDeliveryAddress: string;
  password: string;
  customerType: CustomerType;
  allowCredit: boolean;
  accountId?: number;
  accountCode?: string;
  accountName?: string;
  status: boolean;
}
