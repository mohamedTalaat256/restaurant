export type DeliveryStatus =
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

export const TERMINAL_DELIVERY_STATUSES: DeliveryStatus[] = ['DELIVERED', 'CANCELLED', 'FAILED'];

type Severity = 'success' | 'secondary' | 'info' | 'warn' | 'danger' | 'contrast';

export const DELIVERY_STATUS_SEVERITY: Record<DeliveryStatus, Severity> = {
  CREATED: 'secondary',
  WAITING_ASSIGNMENT: 'warn',
  ASSIGNED: 'info',
  ACCEPTED: 'info',
  ARRIVED_AT_RESTAURANT: 'info',
  PICKED_UP: 'secondary',
  ON_THE_WAY: 'warn',
  DELIVERED: 'success',
  FAILED: 'danger',
  CANCELLED: 'danger',
};

export interface DeliveryDriver {
  id: number;
  fullName: string;
  phone: string;
  vehiclePlate: string;
  vehicleType: string;
}

export interface Delivery {
  id: number;
  orderId: number;
  orderNumber: string;
  driver: DeliveryDriver | null;
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

export interface DeliveryTrackingPoint {
  id: number;
  deliveryId: number;
  latitude: number;
  longitude: number;
  speed: number | null;
  heading: number | null;
  createdAt: string;
}

export interface CreateDeliveryRequest {
  orderId: number;
  deliveryFee?: number;
  estimatedDeliveryTime?: string;
  notes?: string;
}

export interface AssignDriverRequest {
  driverId: number;
  reason?: string | null;
}

export interface ReassignDriverRequest {
  newDriverId: number;
  reason: string;
}

export interface CancelDeliveryRequest {
  reason: string;
}
