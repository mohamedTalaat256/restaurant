import { Floor } from './floor.model';

export interface RestaurantTable {
  id: number;
  name: string;
  capacity: number;
  icon?: string;
  floor?: Floor;
  floorId?: number;
  status: boolean;
}
