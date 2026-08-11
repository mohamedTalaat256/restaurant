export type VehicleType = 'BICYCLE' | 'MOTORCYCLE' | 'CAR' | 'VAN' | 'TRUCK';
export type DriverStatus = 'ONLINE' | 'OFFLINE' | 'BUSY' | 'BREAK';

type Severity = 'success' | 'secondary' | 'info' | 'warn' | 'danger' | 'contrast';

export const DRIVER_STATUS_SEVERITY: Record<DriverStatus, Severity> = {
  ONLINE: 'success',
  OFFLINE: 'secondary',
  BUSY: 'warn',
  BREAK: 'info',
};

export interface Driver {
  id: number;
  userId: number;
  fullName: string;
  email: string;
  phone: string;
  vehicleType: VehicleType;
  vehiclePlate: string;
  isOnline: boolean;
  status: DriverStatus;
  createdAt: string;
}

export interface ChangeDriverStatusRequest {
  status: DriverStatus;
}
