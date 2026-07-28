# Restaurant POS - Orders Module Frontend Prompt

## Tech Stack Angular primeng 
## Backend Base URL
http://localhost:8080/api

All responses follow this envelope:
{
  "status": true | false,
  "message": "msg_key",
  "data": <payload>,
  "code": 200 | 201 | 400 | 404 | ...
}

All requests require a Bearer JWT in the Authorization header.

---

## Enums

OrderType:      QUICK_ORDER | PLACE_ORDER
OrderStatus:    NEW | CONFIRMED | IN_PROGRESS | READY | COMPLETED | CHECKED_OUT | CANCELLED | MERGED | SPLIT
KitchenOrderStatus:     PENDING | ACCEPTED | PREPARING | READY
KitchenOrderItemStatus: PENDING | ACCEPTED | PREPARING | READY | SERVED | REJECTED
PaymentMethod:  CASH | CARD | MIXED
PaymentStatus:  PENDING | PAID | PARTIALLY_PAID | REFUNDED

---

## Core Data Shapes

### Order
{
  id, orderNumber, orderType, status, customerType,
  customerId, customerName,
  thirdPartyCustomerId, thirdPartyCustomerName,
  tableId, tableName,
  waiterId, waiterName,
  cashRegisterId,
  totalAmount,
  notes,
  parentOrderId,
  orderItems: [ OrderItem ],
  kitchenOrders: [ KitchenOrder ],
  createdAt, updatedAt, completedAt
}

### OrderItem
{
  id, itemFoodId, itemFoodName, price, quantity,
  variantId, variantName, addOnsPrice, totalPrice, notes,
  orderItemAddOns: [{ id, addOnId, addOnName, price }]
}

### KitchenOrder
{
  id, orderId, orderNumber, kitchenId, kitchenName, status, notes,
  kitchenOrderItems: [ KitchenOrderItem ],
  createdAt, acceptedAt, rejectedAt, preparedAt, readyAt
}

### KitchenOrderItem
{
  id, orderItemId, itemFoodName, quantity,
  variantName, addOnsPrice, status, notes
}

### Payment
{
  id, orderId, orderNumber, paymentMethod,
  totalAmount, paidAmount, remainingAmount, changeAmount,
  status, createdAt
}

### OrderTracking
{
  orderId, orderNumber, overallStatus,
  totalItems, readyItems,
  kitchenProgress: [{
    kitchenOrderId, kitchenName, kitchenOrderStatus,
    items: [{ kitchenOrderItemId, itemFoodName, quantity, itemStatus }]
  }]
}

---

## REST API Reference

### Orders  →  /api/orders

POST   /api/orders
  Body: {
    orderType: "QUICK_ORDER" | "PLACE_ORDER",
    customerType: string,
    customerId?: number,
    thirdPartyCustomerId?: number,
    tableId?: number,         // required for PLACE_ORDER
    waiterId?: number,
    cashRegisterId?: number,
    notes?: string,
    orderItems: [{
      itemFoodId: number,
      price: number,
      quantity: number,
      variantId?: number,
      variantName?: string,
      addOnIds?: number[],
      addOnsPrice?: number,
      notes?: string
    }]
  }
  Returns: Order (201)

GET    /api/orders                       → Order[]
GET    /api/orders?orderStatus=NEW       → Order[] (filtered)
GET    /api/orders/{id}                  → Order
DELETE /api/orders/{id}                  → 200

POST   /api/orders/{id}/items            Body: OrderItemRequest   → Order
DELETE /api/orders/{id}/items/{itemId}                           → Order
PATCH  /api/orders/{id}/items/{itemId}/quantity?quantity=3       → Order

POST   /api/orders/merge
  Body: { orderIds: number[], notes?: string }
  Returns: Order (merged)

POST   /api/orders/{id}/split
  Body: {
    splitGroups: [
      [{ orderItemId: number, quantity: number }, ...],  // group 1
      [{ orderItemId: number, quantity: number }, ...]   // group 2
    ],
    notes?: string
  }
  Returns: Order[] (new split orders)

POST   /api/orders/{id}/complete         → Order
POST   /api/orders/{id}/checkout
  Body: { paymentMethod: "CASH"|"CARD"|"MIXED", paidAmount: number }
  Returns: Payment

GET    /api/orders/{id}/tracking         → OrderTracking

---

### Kitchen Dashboard  →  /api/orders/kitchen

GET  /api/orders/kitchen/by-kitchen/{kitchenId}                        → KitchenOrder[]
GET  /api/orders/kitchen/by-kitchen/{kitchenId}/status/{status}        → KitchenOrder[]
GET  /api/orders/kitchen/{id}                                          → KitchenOrder

POST /api/orders/kitchen/{id}/accept     Body: { notes?: string }      → KitchenOrder
POST /api/orders/kitchen/{id}/reject     Body: { notes?: string }      → KitchenOrder
POST /api/orders/kitchen/{id}/prepare                                  → KitchenOrder
POST /api/orders/kitchen/{id}/ready                                    → KitchenOrder

POST /api/orders/kitchen/{id}/items/{itemId}/ready                     → KitchenOrder
POST /api/orders/kitchen/{id}/items/{itemId}/served                    → KitchenOrder

---

## Pages to Build

### 1. POS Screen
- Two buttons: "Quick Order" and "Place Order"
- Food item grid (from existing food management API)
- Click item → add to cart panel on the right
- Cart panel shows: items, variants, add-ons, quantities, subtotals, total
- Quick Order: no table selection required → submit → show checkout modal
- Place Order: select table + waiter → submit → order goes to kitchen
- On submit, POST /api/orders

### 2. Orders List Page
- Table showing all orders with columns:
  orderNumber | type | status | table | waiter | total | createdAt | actions
- Filter by OrderStatus (tabs or dropdown)
- Actions per row: View, Complete, Checkout, Delete
- "Merge Orders" button (select multiple rows → merge)

### 3. Order Detail / Edit Page
- Show full order details
- Add/remove items (calls POST /items or DELETE /items/{id})
- Update quantities (calls PATCH /items/{id}/quantity)
- Show kitchen orders with their statuses
- Show tracking progress bar
- Buttons: Complete, Checkout, Split

### 4. Split Order Modal
- Show all order items with quantity inputs
- Group items into N new orders (dynamic "Add Group" button)
- Drag items between groups or use quantity inputs
- Submit → POST /api/orders/{id}/split

### 5. Checkout Modal
- Show total amount
- Select payment method (CASH / CARD / MIXED)
- Input paid amount → auto-calculate change/remaining
- Submit → POST /api/orders/{id}/checkout

### 6. Kitchen Dashboard
- One page per kitchen (route: /kitchen/{kitchenId})
- Real-time polling (every 5s) or WebSocket
- Three columns (Kanban-style): PENDING | PREPARING | READY
- Each card shows: order number, items list with individual statuses
- Buttons on card: Accept / Reject / Prepare / Mark Ready
- Buttons on item row: Mark Item Ready / Mark Item Served

### 7. Order Tracking Screen  (for waiters)
- Route: /orders/{id}/tracking
- Shows: orderNumber, overallStatus badge
- Progress bar: {readyItems} / {totalItems} items ready
- Per-kitchen section with item status chips
- Color coding: PENDING=gray, PREPARING=orange, READY=green, SERVED=blue

---

## Status Color Map

OrderStatus:
  NEW → blue
  CONFIRMED → cyan
  IN_PROGRESS → orange
  READY → lime
  COMPLETED → green
  CHECKED_OUT → teal
  CANCELLED → red
  MERGED → purple
  SPLIT → magenta

KitchenOrderStatus / KitchenOrderItemStatus:
  PENDING → gray
  ACCEPTED → blue
  PREPARING → orange
  READY → green
  SERVED → teal
  REJECTED → red

---

## Business Rules for UI
- PLACE_ORDER requires tableId (validate before submit)
- Merge: select ≥ 2 orders, none can be COMPLETED/CHECKED_OUT/CANCELLED/MERGED
- Split: cannot split CHECKED_OUT/CANCELLED/MERGED orders
- Checkout button only enabled when status = COMPLETED
- Complete button only enabled when status ∈ {NEW, CONFIRMED, IN_PROGRESS, READY}
- Cannot edit/remove items that are already ACCEPTED or further in kitchen
- Kitchen Accept/Reject only available when status = PENDING
- Kitchen Prepare only available when status = ACCEPTED
- Mark Ready available when ACCEPTED or PREPARING
