export interface ItemCategory {
  id: number;
  name: string;
  image?: string;
  position?: number;
  isOffer: boolean;
  offerStartDate?: string;
  offerEndDate?: string;
  status: boolean;
}
