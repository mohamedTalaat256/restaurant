# Restaurant POS & Kitchen Management System Design Prompt

i order module in src/main/java/com/mtalaat/restaurant/modules/order

I want you to act as a Senior Software Architect and Backend Engineer and design the Orders module with a scalable architecture following clean architecture and best practices.

## Current Order Request

When an order is submitted (Quick Order or Place Order), the backend receives:

```json
{
  "customerType": "WALK_IN_CUSTOMER",
  "customerId": 3,
  "thirdPartyCustomerId": null,
  "tableId": 1,
  "waiterId": 2,
  "paidAmount": 131.1,
  "paymentMethodId": "CASH",
  "orderItems": [
    {
      "itemFoodId": 3,
      "itemFoodName": "شاورما فراخ",
      "price": 115,
      "quantity": 1,
      "variantId": 2,
      "variantName": "كبير",
      "addOnIds": [2],
      "addOnNames": "تومية",
      "addOnsPrice": 15
    }
  ]
}
```

---

# Order Types

The POS contains two buttons:

* Quick Order
* Place Order

## Quick Order

* No table is required.
* Mainly for takeaway or walk-in customers.
* Can go directly to checkout after completion.

## Place Order

* Usually assigned to a table.
* Has waiter information.
* Will pass through the complete restaurant workflow.

---

# Order Entity

Design an Order entity that supports the following features:

## Create Order

Create a new order from the POS.

---

## Update Order

Allow editing an existing order:

* Add items
* Remove items
* Update quantities
* Change variants
* Change add-ons

---

## Delete Order

Allow deleting an order if business rules allow.

---

## Merge Orders

The system should have an Orders page.

The cashier can:

* Select multiple orders.
* Merge them into one order.

Requirements:

* Merge all order items.
* Preserve quantities.
* Preserve pricing.
* Handle duplicate items correctly.
* Keep audit history if possible.

---

## Split Order

Allow splitting an existing order.

Example:

Order A contains 10 items.

The cashier chooses:

Split into 3 orders.

The UI allows moving individual items or quantities from the original order into each new order.

Requirements:

* Split by item quantity.
* Preserve prices.
* Preserve add-ons.
* Preserve variants.
* Original order should be updated accordingly.

---

## Complete Order

The order should move through its lifecycle until:

Completed

After completion:

Allow Checkout.

---

## Checkout

Support payment after completion.

Possible payment methods:

* Cash
* Card
* Mixed payment (future support)

Store:

* Paid amount
* Remaining amount
* Payment status

---

# Order Status Workflow

Design a proper OrderStatus enum.

Example:

NEW

CONFIRMED

IN_PROGRESS

READY

COMPLETED

CHECKED_OUT

CANCELLED

MERGED

SPLIT

or suggest a better workflow.

---

# Kitchen Orders

Each ItemFood already belongs to a Kitchen.

Example:

```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "kitchen_id")
private Kitchen kitchen;
```

Because of this, I want a separate entity called:

KitchenOrder

The purpose is:

When a Place Order is created,

the system should automatically group order items by kitchen.

Example:

Main Order

* Burger (Kitchen A)
* Fries (Kitchen B)
* Pizza (Kitchen C)
* Cola (Bar Kitchen)

The backend automatically creates:

KitchenOrder #1

Kitchen A

Burger

KitchenOrder #2

Kitchen B

Fries

KitchenOrder #3

Kitchen C

Pizza

KitchenOrder #4

Bar

Cola

Each KitchenOrder should reference the parent Order.

---

# KitchenOrder Entity

Design this entity.

It should include:

* Parent Order
* Kitchen
* Status
* Created At
* Accepted At
* Prepared At
* Rejected At
* Notes

Also decide whether KitchenOrder should contain:

KitchenOrderItems

instead of referencing OrderItems directly.

Explain which design is better.

---

# Kitchen Order Item

Each kitchen item should have its own status.

Example:

PENDING

ACCEPTED

REJECTED

PREPARING

READY

SERVED

because sometimes one item finishes before another.

---

# Kitchen Dashboard

Create a Kitchen Dashboard.

Each kitchen should only see its own KitchenOrders.

Kitchen staff can:

Accept Order

Reject Order

Start Preparing

Mark Item Ready

Mark Kitchen Order Ready

View pending orders

View preparing orders

View completed orders

---

# Order Tracking Screen

Create another screen for waiters/cashiers.

It should display:

Overall Order Status

Kitchen Status

Progress

Example:

Order #105

Kitchen A

Burger

Ready

Kitchen B

Fries

Preparing

Kitchen C

Pizza

Accepted

Overall Progress:

2 of 4 items ready

---

# Entity Design

Design all required entities.

Example:

Order

OrderItem

KitchenOrder

KitchenOrderItem

Payment

PaymentTransaction

OrderHistory

OrderStatusHistory

or any additional entities you recommend.

Include:

Relationships

OneToMany

ManyToOne

Cascade options

Indexes

Unique constraints

Best practices

---

# APIs

Design REST APIs for:

Create Order

Update Order

Delete Order

Merge Orders

Split Order

Complete Order

Checkout

Get Orders

Get Order Details

Kitchen Dashboard

Accept Kitchen Order

Reject Kitchen Order

Prepare Kitchen Order

Ready Kitchen Order

Kitchen Order Details

Order Tracking

---

# Business Rules

Please include all business rules, edge cases, and validations.

For example:

Cannot merge completed orders.

Cannot split checked-out orders.

Cannot checkout before completion.

Cannot edit prepared kitchen items.

Cannot delete orders already accepted by the kitchen.

Explain every rule.

---

# Database Design

Provide a normalized database schema.

Include:

ER Diagram (text format)

Tables

Columns

Primary Keys

Foreign Keys

Indexes

Recommended constraints

---

# Sequence Diagrams

Generate sequence diagrams for:

Place Order

Quick Order

Kitchen Order Creation

Merge Orders

Split Orders

Complete Order

Checkout

Kitchen Workflow

---

# Architecture

Recommend the best architecture for this module using Spring Boot.

Include:

Entities

DTOs

Services

Repositories

Specifications

Mappers

Validation

Transactions

Domain Events (if useful)

Explain why your design is scalable and suitable for large restaurant systems.
