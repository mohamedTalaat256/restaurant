Act as a Senior Java Software Architect with extensive experience in Spring Boot, Domain-Driven Design (DDD), Clean Architecture, and Restaurant POS systems.

I am building a Restaurant Management System using:

- Java 21
- Spring Boot 4
- Spring Data JPA
- Hibernate
- MySQL
- Maven
- Lombok
- MapStruct
- Bean Validation
- REST APIs

I already have these modules:

- Authentication
- Users
- Roles
- Customers
- Orders
- Order Items
- Payments

Now I want to design a Delivery Module.

## Existing Order Entity

Order already contains:

- id
- customer
- customerType
- orderStatus
- totalAmount
- branch
- createdAt

Customer Types are:

- WALK_IN_CUSTOMER
- THIRD_PARTY_CUSTOMER
- TAKEAWAY_CUSTOMER
- ONLINE_CUSTOMER

I want to redesign this if necessary.

## Requirements

I want the architecture to follow enterprise best practices.

### 1. Separate Customer Type from Order Type

Instead of using CustomerType to determine whether an order is delivery, create:

CustomerType

- WALK_IN
- REGISTERED
- THIRD_PARTY
- ONLINE

OrderType

- DINE_IN
- TAKEAWAY
- DELIVERY

Explain why this design is better.

---

### 2. Delivery Module

A Delivery belongs to exactly one Order.

An Order may or may not have a Delivery.

Relationship:

Order 1 ---- 0..1 Delivery

Generate the Delivery entity with:

- id
- order
- driver
- status
- assignedAt
- acceptedAt
- pickedUpAt
- deliveredAt
- cancelledAt
- estimatedDeliveryTime
- actualDeliveryTime
- deliveryFee
- notes

---

### 3. Driver Module

Drivers are users with additional delivery information.

Design Driver entity:

- id
- user
- vehicleType
- vehiclePlate
- phone
- isOnline
- status

Driver Status:

- ONLINE
- OFFLINE
- BUSY
- BREAK

---

### 4. Delivery Assignment

Create DeliveryAssignment entity to keep assignment history.

Fields:

- id
- delivery
- driver
- assignedBy
- assignedAt
- reason

Support driver reassignment.

---

### 5. Delivery Tracking

Create DeliveryTracking entity.

Fields:

- id
- delivery
- latitude
- longitude
- speed
- heading
- createdAt

Tracking records should be append-only.

---

### 6. Delivery Address

Never use the customer's current address directly.

Create:

DeliveryAddress
(stored customer addresses)

and

OrderDeliveryAddress
(snapshot copied when order is created)

Explain why snapshotting addresses is important.

---

### 7. Order Status

Create OrderStatus enum:

NEW
CONFIRMED
PREPARING
READY
COMPLETED
CANCELLED

---

### 8. Delivery Status

Create DeliveryStatus enum:

CREATED
WAITING_ASSIGNMENT
ASSIGNED
ACCEPTED
ARRIVED_AT_RESTAURANT
PICKED_UP
ON_THE_WAY
DELIVERED
FAILED
CANCELLED

Explain why Delivery Status must be independent from Order Status.

---

### 9. APIs

Generate REST APIs for:

Delivery

- Create Delivery
- Assign Driver
- Reassign Driver
- Accept Delivery
- Pickup Order
- Start Delivery
- Complete Delivery
- Cancel Delivery
- Get Delivery Details

Driver

- Get Available Drivers
- Change Driver Status
- Driver Current Delivery
- Driver Delivery History

Tracking

- Update Driver Location
- Get Delivery Tracking

---

### 10. Business Rules

Implement business validations:

- Only DELIVERY orders can have Delivery records.
- Order must be READY before pickup.
- Driver cannot have two active deliveries.
- Driver must be ONLINE before assignment.
- Delivery cannot be completed before pickup.
- Completed orders cannot be modified.
- Cancelled deliveries cannot be updated.
- Validate all state transitions.

---

### 11. Architecture

Use Clean Architecture.

Generate:

- Entities
- Enums
- DTOs
- Request Models
- Response Models
- Repository interfaces
- Service interfaces
- Service implementations
- Controllers
- Mapper classes (MapStruct)
- Validation classes
- Custom Exceptions
- Global Exception Handler

---

### 12. Database Design

Generate complete ERD.

Generate all JPA relationships:

@OneToOne
@OneToMany
@ManyToOne

Choose FetchType and CascadeType appropriately and explain every choice.

---

### 13. Package Structure

Use:

com.restaurant.delivery
    controller
    service
        impl
    repository
    entity
    dto
        request
        response
    mapper
    exception
    validation
    enums

---

### 14. Code Quality

Use:

- Constructor Injection
- Lombok
- @Transactional
- Bean Validation
- SOLID Principles
- Clean Code
- Java 21 best practices

Avoid anemic models where appropriate.

---

### 15. Output

Generate the implementation step by step.

Start with:

1. Domain Model
2. ER Diagram
3. Entities
4. Enums
5. DTOs
6. Repositories
7. Services
8. Controllers
9. Validation
10. Business Flow
11. Sample JSON Requests/Responses

Do not skip any files.
Explain architectural decisions before generating code.
Generate production-ready code only.