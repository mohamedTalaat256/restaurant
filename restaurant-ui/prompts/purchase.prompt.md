 
---

Here are all the changes the frontend needs to know about:

---

## Frontend Contract Changes — Purchase Module

### 1. New Endpoints

| Method | URL | Description |
|---|---|---|
| `PATCH` | `/api/purchase/purchases/{id}/approve` | اعتماد فاتورة (DRAFT → APPROVED) |
| `PATCH` | `/api/purchase/purchases/{id}/void` | إلغاء فاتورة معتمدة (APPROVED → VOIDED) |

The existing `PUT /api/purchase/purchases/{id}` now uses **Void & Replace** logic automatically — no changes needed in the call itself.

---

### 2. `PurchaseDto` — Changes

**Removed field:**
```json
"expiryDate": "2025-01-01"   // ❌ removed from purchase level
```

**Added field:**
```json
"status": "DRAFT"   // ✅ new — read-only from backend (DRAFT | APPROVED | VOIDED)
```

**Full updated shape:**
```json
{
  "id": 1,
  "invoiceNumber": "INV-001",
  "paymentMethod": "CREDIT",
  "supplierId": 5,
  "supplierName": "ABC Supplier",
  "purchaseDate": "2026-08-24",
  "status": "DRAFT",
  "totalAmount": 500.0,
  "paidAmount": 200.0,
  "note": "...",
  "purchaseItems": [ ... ]
}
```

---

### 3. `PurchaseItemDto` — Changes

**Added fields:**
```json
"productionDate": "2026-06-01",   // ✅ new — nullable
"expiryDate": "2027-06-01"        // ✅ moved here from PurchaseDto
```

**Full updated shape:**
```json
{
  "id": 10,
  "ingredientId": 3,
  "ingredientName": "Tomato",
  "quantity": 10.0,
  "price": 5.0,
  "productionDate": "2026-06-01",
  "expiryDate": "2027-06-01"
}
```

---

### 4. Status Lifecycle & UI Rules

```
DRAFT ──[PATCH /approve]──► APPROVED ──[PATCH /void]──► VOIDED
  │                              │
  └── PUT (edit freely)          └── PUT (triggers Void & Replace automatically)
```

| Status | Edit (PUT) | Approve | Void | Delete |
|---|---|---|---|---|
| `DRAFT` | ✅ | ✅ | ❌ | ✅ |
| `APPROVED` | ✅ (creates new version) | ❌ | ✅ | ❌ |
| `VOIDED` | ❌ | ❌ | ❌ | ❌ |

Made changes.
