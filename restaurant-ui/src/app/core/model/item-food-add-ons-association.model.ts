import { ItemFood } from './item-food.model';
import { ItemFoodAddOns } from './item-food-add-ons.model';

export interface ItemFoodAddOnsAssociation {
  id: number;
  itemFood?: ItemFood;
  itemFoodId?: number;
  itemFoodAddOns?: ItemFoodAddOns;
  itemFoodAddOnsId?: number;
  itemFoodAddOnsName?: string;
}
