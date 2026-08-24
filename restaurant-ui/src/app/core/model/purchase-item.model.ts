import { Ingredient } from './ingredient.model';

export interface PurchaseItem {
  id: number;
  ingredientId: number;
  ingredient?: Ingredient;
  quantity: number;
  price: number;
  productionDate?: string;
  expiryDate?: string;
}
