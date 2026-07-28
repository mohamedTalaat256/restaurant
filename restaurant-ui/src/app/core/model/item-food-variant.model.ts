import { ItemFood } from './item-food.model';

export interface ItemFoodVariant {
  id: number;
  name: string;
  price: number;
  itemFood?: ItemFood;
  itemFoodId?: number;
}
