Act as a Senior Frontend Engineer with expertise in React, TypeScript, REST API integration, and Restaurant POS systems.

## Context

The backend has just shipped a new **Delivery Module** for a Restaurant Management System. Your task is to build the complete frontend integration for this module.

The backend stack is:
- Spring Boot REST API
- Base URL: `/api`
- All responses follow this envelope:

```json
{
  "status": true,
  "message": "msg_key",
  "data": { ... },
  "code": 200
}
```

---

## What Changed in the Backend

### 1. OrderType Enum — New Value Added

`OrderType` now has a third value:

```
QUICK_ORDER
PLACE_ORDER
DELIVERY      ← NEW
```

**Impact:** When creating or displaying orders, the order type selector must include `DELIVERY`. Only orders with `orderType: "DELIVERY"` will have a delivery record.

---

### 2. New Entities

#### Driver
A system user with a driver profile.

```ts
interface Driver {
  id: number;
  userId: number;
  fullName: string;
  email: string;
  phone: string;
  vehicleType: 'BICYCLE' | 'MOTORCYCLE' | 'CAR' | 'VAN' | 'TRUCK';
  vehiclePlate: string;
  isOnline: boolean;
  status: 'ONLINE' | 'OFFLINE' | 'BUSY' | 'BREAK';
  createdAt: string;
}
```

#### Delivery
```ts
interface Delivery {
  id: number;
  orderId: number;
  orderNumber: string;
  driver: {
    id: number;
    fullName: string;
    phone: string;
    vehiclePlate: string;
    vehicleType: string;
  } | null;
  status: DeliveryStatus;
  deliveryFee: number | null;
  estimatedDeliveryTime: string | null;
  actualDeliveryTime: string | null;
  assignedAt: string | null;
  acceptedAt: string | null;
  pickedUpAt: string | null;
  deliveredAt: string | null;
  cancelledAt: string | null;
  notes: string | null;
  createdAt: string;
  updatedAt: string;
}

type DeliveryStatus =
  | 'CREATED'
  | 'WAITING_ASSIGNMENT'
  | 'ASSIGNED'
  | 'ACCEPTED'
  | 'ARRIVED_AT_RESTAURANT'
  | 'PICKED_UP'
  | 'ON_THE_WAY'
  | 'DELIVERED'
  | 'FAILED'
  | 'CANCELLED';
```

#### DeliveryTrackingPoint
```ts
interface DeliveryTrackingPoint {
  id: number;
  deliveryId: number;
  latitude: number;
  longitude: number;
  speed: number | null;
  heading: number | null;
  createdAt: string;
}
```

---

## REST API Reference

### Deliveries — `/api/deliveries`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/deliveries` | List all deliveries |
| `GET` | `/api/deliveries/{id}` | Get delivery by ID |
| `GET` | `/api/deliveries/order/{orderId}` | Get delivery by order ID |
| `POST` | `/api/deliveries` | Create delivery for a DELIVERY-type order |
| `POST` | `/api/deliveries/{id}/assign` | Assign a driver |
| `POST` | `/api/deliveries/{id}/reassign` | Reassign to a different driver |
| `PATCH` | `/api/deliveries/{id}/accept` | Driver accepts the delivery |
| `PATCH` | `/api/deliveries/{id}/arrived` | Driver arrived at restaurant |
| `PATCH` | `/api/deliveries/{id}/pickup` | Driver picks up the order |
| `PATCH` | `/api/deliveries/{id}/start` | Driver starts heading to customer |
| `PATCH` | `/api/deliveries/{id}/complete` | Delivery completed |
| `PATCH` | `/api/deliveries/{id}/cancel` | Cancel delivery |

**Create Delivery — POST `/api/deliveries`**
```json
{
  "orderId": 42,
  "deliveryFee": 15.00,
  "estimatedDeliveryTime": "2026-07-28T14:30:00",
  "notes": "Ring the bell"
}
```

**Assign Driver — POST `/api/deliveries/{id}/assign`**
```json
{
  "driverId": 3,
  "reason": null
}
```

**Reassign Driver — POST `/api/deliveries/{id}/reassign`**
```json
{
  "newDriverId": 7,
  "reason": "Original driver reported sick"
}
```

**Cancel Delivery — PATCH `/api/deliveries/{id}/cancel`**
```json
{
  "reason": "Customer cancelled"
}
```

---

### Drivers — `/api/drivers`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/drivers` | List all drivers |
| `GET` | `/api/drivers/available` | List ONLINE drivers with no active delivery |
| `GET` | `/api/drivers/{id}` | Get driver profile |
| `PATCH` | `/api/drivers/{id}/status` | Change driver status |
| `GET` | `/api/drivers/{id}/current-delivery` | Driver's active delivery |
| `GET` | `/api/drivers/{id}/deliveries` | Driver's completed delivery history |

**Change Driver Status — PATCH `/api/drivers/{id}/status`**
```json
{
  "status": "ONLINE"
}
```

---

### Tracking — `/api/deliveries/{deliveryId}/tracking`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/deliveries/{deliveryId}/tracking` | Full GPS trail |
| `GET` | `/api/deliveries/{deliveryId}/tracking/latest` | Latest driver position |
| `POST` | `/api/deliveries/{deliveryId}/tracking/location` | Record new GPS point (driver app) |

**Record Location — POST `/api/deliveries/{deliveryId}/tracking/location`**
```json
{
  "latitude": 30.0444,
  "longitude": 31.2357,
  "speed": 42.5,
  "heading": 180.0
}
```

---

## Delivery Status Flow (State Machine)

```
CREATED / WAITING_ASSIGNMENT
        ↓  [assign driver]
     ASSIGNED
        ↓  [accept]
     ACCEPTED
        ↓  [arrived at restaurant]
  ARRIVED_AT_RESTAURANT
        ↓  [pickup — order must be READY]
     PICKED_UP
        ↓  [start]
     ON_THE_WAY
        ↓  [complete]
     DELIVERED  ← terminal

Any non-terminal state → CANCELLED (terminal)
Any non-terminal state → FAILED    (terminal)
```

---

## Pages / Views to Build

### 1. Delivery Dashboard (`/deliveries`)
- Table of all deliveries with columns: Order #, Customer, Driver, Status (badge), Created At, Actions
- Filter by `DeliveryStatus`
- Row click → Delivery Detail page
- "Create Delivery" button (opens modal/form)

### 2. Delivery Detail (`/deliveries/:id`)
- Full delivery card with all timestamp fields
- Driver info card (name, phone, vehicle)
- Timeline component showing status progression with timestamps
- Action buttons that appear based on current status:
  - `WAITING_ASSIGNMENT` → **Assign Driver** button
  - `ASSIGNED` → **Reassign Driver** button
  - `ON_THE_WAY` → **Complete** button
  - non-terminal → **Cancel** button
- Embedded map component showing the GPS trail (if tracking points exist)

### 3. Assign Driver Modal
- Dropdown populated from `GET /api/drivers/available`
- Shows driver name, vehicle type, vehicle plate
- Optional reason field
- Submit calls `POST /api/deliveries/{id}/assign`

### 4. Reassign Driver Modal
- Same as Assign but reason is **required**
- Submit calls `POST /api/deliveries/{id}/reassign`

### 5. Driver Management (`/drivers`)
- Table: Name, Phone, Vehicle, Status badge, Is Online toggle, Actions
- Status badge colours:
  - `ONLINE` → green
  - `OFFLINE` → grey
  - `BUSY` → orange
  - `BREAK` → yellow
- "Change Status" dropdown per row → calls `PATCH /api/drivers/{id}/status`
- Row click → Driver Detail page

### 6. Driver Detail (`/drivers/:id`)
- Profile card
- Current active delivery card (if any) — from `GET /api/drivers/{id}/current-delivery`
- Delivery history table — from `GET /api/drivers/{id}/deliveries`

### 7. Live Tracking View (embedded in Delivery Detail)
- Map showing the most recent driver location
- Polyline of full GPS trail
- Polling `GET /api/deliveries/{id}/tracking/latest` every 10 seconds when delivery is `ON_THE_WAY`
- Show speed and heading if available

---

## Status Badge Colour Mapping

```ts
const DELIVERY_STATUS_COLORS: Record<DeliveryStatus, string> = {
  CREATED:               'gray',
  WAITING_ASSIGNMENT:    'yellow',
  ASSIGNED:              'blue',
  ACCEPTED:              'blue',
  ARRIVED_AT_RESTAURANT: 'indigo',
  PICKED_UP:             'purple',
  ON_THE_WAY:            'orange',
  DELIVERED:             'green',
  FAILED:                'red',
  CANCELLED:             'red',
};
```

---

## Business Rules to Enforce in UI

- Disable "Create Delivery" button if order type is not `DELIVERY`
- Disable "Assign" if no available drivers exist
- Show "Reassign" only when a driver is already assigned and delivery is not terminal
- Show "Pickup" only when delivery is `ARRIVED_AT_RESTAURANT` (backend also validates order is `READY`)
- Disable all action buttons for terminal statuses: `DELIVERED`, `CANCELLED`, `FAILED`
- Tracking map is only shown when status is `ON_THE_WAY` or later (until terminal)

---

## Error Handling

The backend returns `400 Bad Request` with message keys for business rule violations. Map these keys to user-friendly messages:

| Message Key | Display Text |
|---|---|
| `msg_order_not_delivery_type` | This order is not a delivery order |
| `msg_delivery_already_exists` | A delivery already exists for this order |
| `msg_driver_not_available` | Driver is not available for assignment |
| `msg_driver_has_active_delivery` | Driver already has an active delivery |
| `msg_delivery_in_terminal_state` | This delivery is already closed |
| `msg_order_not_ready_for_pickup` | The order is not ready for pickup yet |
| `msg_driver_busy_cannot_change_status` | Cannot change status while driver has an active delivery |
| `msg_no_active_delivery` | Driver has no active delivery |
| `msg_no_tracking_data` | No tracking data available yet |
| `msg_delivery_not_found` | Delivery not found |
| `msg_driver_not_found` | Driver not found |

---

## Output

Generate the implementation step by step:

1. TypeScript types / interfaces
2. API service layer (axios/fetch functions for every endpoint)
3. State management (Redux slice or React Query hooks — choose the one already used in the project)
4. Delivery Dashboard page
5. Delivery Detail page with status timeline and action buttons
6. Assign / Reassign Driver modal
7. Driver Management page
8. Driver Detail page
9. Live Tracking map component (use Leaflet or Google Maps — match existing project)
10. Status badge component
11. Route configuration additions

Do not skip any files. Use the same component library and patterns already in use in the project.
