import { ItemCategory } from './item-category.model';
import { ItemFoodAddOnsAssociation } from './item-food-add-ons-association.model';
import { ItemFoodAddOns } from './item-food-add-ons.model';
import { ItemFoodVariant } from './item-food-variant.model';
import { Kitchen } from './kitchen.model';
import { MenuType } from './menu-type.model';

export interface ItemFood {
  id: number;
  name: string;
  descrip?: string;
  component?: string;
  note?: string;
  image?: string;
  position?: number;
  isGroup: boolean;
  cookedTime?: number;
  offerIsAvailable: boolean;
  offerRate?: number;
  offerStartDate?: string;
  offerEndDate?: string;
  status: boolean;
  category?: ItemCategory;
  categoryId?: number;
  kitchen?: Kitchen;
  kitchenId?: number;
  isCustomQty: boolean;
  isSpecial: boolean;
  productVat?: number;
  tax0?: number;
  tax1?: number;
  menuType?: MenuType;
  menuTypeId?: number;
  variants?: ItemFoodVariant[];
  addOnsAssociations?: ItemFoodAddOnsAssociation[];
}
