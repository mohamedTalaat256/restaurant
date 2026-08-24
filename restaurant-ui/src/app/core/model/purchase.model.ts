import { Supplier } from './supplier.model';
import { PurchaseItem } from './purchase-item.model';
import { PaymentMethod } from '../enum/paymentMethod.enum';
import { PurchaseStatus } from '../enum/purchaseStatus.enum';

export interface Purchase {
  id: number;
  invoiceNumber: string;
  paymentMethod: PaymentMethod;
  supplier: Supplier;
  supplierId: number;
  purchaseDate: string;
  status: PurchaseStatus;
  totalAmount: number;
  paidAmount: number;
  note?: string;
  purchaseItems?: PurchaseItem[];
}
