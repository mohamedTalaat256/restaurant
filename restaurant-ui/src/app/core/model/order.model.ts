export type OrderType = 'QUICK_ORDER' | 'PLACE_ORDER' | 'DELIVERY';

export type OrderStatus =
  | 'NEW'
  | 'CONFIRMED'
  | 'IN_PROGRESS'
  | 'READY'
  | 'COMPLETED'
  | 'CHECKED_OUT'
  | 'CANCELLED'
  | 'MERGED'
  | 'SPLIT';

export type KitchenOrderStatus = 'PENDING' | 'ACCEPTED' | 'PREPARING' | 'READY';
export type KitchenOrderItemStatus = 'PENDING' | 'ACCEPTED' | 'PREPARING' | 'READY' | 'SERVED' | 'REJECTED';
export type PaymentMethod = 'CASH' | 'CARD' | 'MIXED';

export interface OrderItemAddOn {
  id: number;
  addOnId: number;
  addOnName: string;
  price: number;
}

export interface OrderItem {
  id: number;
  itemFoodId: number;
  itemFoodName: string;
  price: number;
  quantity: number;
  variantId?: number;
  variantName?: string;
  addOnsPrice: number;
  totalPrice: number;
  notes?: string;
  orderItemAddOns: OrderItemAddOn[];
}

export interface KitchenOrderItem {
  id: number;
  orderItemId: number;
  itemFoodName: string;
  quantity: number;
  variantName?: string;
  addOnsPrice: number;
  status: KitchenOrderItemStatus;
  notes?: string;
}

export interface KitchenOrder {
  id: number;
  orderId: number;
  orderNumber: string;
  kitchenId: number;
  kitchenName: string;
  status: KitchenOrderStatus;
  notes?: string;
  kitchenOrderItems: KitchenOrderItem[];
  createdAt: string;
  acceptedAt?: string;
  rejectedAt?: string;
  preparedAt?: string;
  readyAt?: string;
}

export interface Order {
  id: number;
  orderNumber: string;
  orderType: OrderType;
  status: OrderStatus;
  customerType: string;
  customerId?: number;
  customerName?: string;
  thirdPartyCustomerId?: number;
  thirdPartyCustomerName?: string;
  tableId?: number;
  tableName?: string;
  waiterId?: number;
  waiterName?: string;
  cashRegisterId?: number;
  totalAmount: number;
  notes?: string;
  parentOrderId?: number;
  orderItems: OrderItem[];
  kitchenOrders: KitchenOrder[];
  createdAt: string;
  updatedAt?: string;
  completedAt?: string;
}



export interface OrderTrackingItem {
  kitchenOrderItemId: number;
  itemFoodName: string;
  quantity: number;
  itemStatus: KitchenOrderItemStatus;
}

export interface KitchenProgress {
  kitchenOrderId: number;
  kitchenName: string;
  kitchenOrderStatus: KitchenOrderStatus;
  items: OrderTrackingItem[];
}

export interface OrderTracking {
  orderId: number;
  orderNumber: string;
  overallStatus: OrderStatus;
  totalItems: number;
  readyItems: number;
  kitchenProgress: KitchenProgress[];
}

export interface CreateOrderRequest {
  orderType: OrderType;
  customerType: string;
  customerId?: number;
  thirdPartyCustomerId?: number;
  tableId?: number;
  waiterId?: number;
  cashRegisterId?: number;
  notes?: string;
  orderItems: {
    itemFoodId: number;
    price: number;
    quantity: number;
    variantId?: number;
    variantName?: string;
    addOnIds?: number[];
    addOnsPrice?: number;
    notes?: string;
  }[];
}

export interface OrderItemRequest {
  itemFoodId: number;
  price: number;
  quantity: number;
  variantId?: number;
  variantName?: string;
  addOnIds?: number[];
  addOnsPrice?: number;
  notes?: string;
}

export interface CheckoutRequest {
  paidAmount: number;
  isCash: boolean;
}

export interface SplitOrderRequest {
  splitGroups: { orderItemId: number; quantity: number }[][];
  notes?: string;
}

export interface MergeOrdersRequest {
  orderIds: number[];
  notes?: string;
}
