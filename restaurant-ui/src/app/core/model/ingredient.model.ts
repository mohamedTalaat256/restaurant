import { Uom } from './unit-of-measuremrnt.model';

export interface Ingredient {
  id: number;
  name: string;
  uom: Uom;
  uomId: number;
  stockQuantity: number;
  minStockQuantity: number;
  status: boolean;
}
