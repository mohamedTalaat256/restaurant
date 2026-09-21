export type DeliveryVehicleType =
  | 'MOTORCYCLE'
  | 'BICYCLE'
  | 'CAR'
  | 'VAN'
  | 'SCOOTER'
  | 'ON_FOOT';

export const DELIVERY_VEHICLE_TYPES: DeliveryVehicleType[] = [
  'MOTORCYCLE',
  'BICYCLE',
  'CAR',
  'VAN',
  'SCOOTER',
  'ON_FOOT',
];

export const DELIVERY_VEHICLE_TYPE_OPTIONS: { label: string; value: DeliveryVehicleType }[] = [
  { label: 'label_vehicle_motorcycle', value: 'MOTORCYCLE' },
  { label: 'label_vehicle_bicycle', value: 'BICYCLE' },
  { label: 'label_vehicle_car', value: 'CAR' },
  { label: 'label_vehicle_van', value: 'VAN' },
  { label: 'label_vehicle_scooter', value: 'SCOOTER' },
  { label: 'label_vehicle_on_foot', value: 'ON_FOOT' },
];

export interface DeliveryDetails {
  id: number;
  userId: number;          // linked system user (read-only)
  firstname: string;       // required — synced to users table
  lastname: string;        // required — synced to users table
  email: string;           // required, valid email — synced to users table
  password?: string;       // required on create; optional on update (only if changing)
  image?: string;
  phone: string;           // required
  vehicleType?: DeliveryVehicleType;
  vehicleNumber?: string;
  status?: boolean;        // available/active flag
}
