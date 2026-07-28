export interface Product {
  id?: number;
  name?: string;
  description?: string;
  price?: number;
  categoryDto?: any;
  amountInStock?: number;
  productDetails?: string;
  imageUrl?: string;
  createdAt?: string;
  updatedAt?: string;
  createdBy?: string;
  updatedBy?: string;
}

export interface ProductPage {
  content: Product[];
  empty: boolean;
  first: boolean;
  last: boolean;
  number: number;
  numberOfElements: number;
  pageable: {
    offset: number;
    pageNumber: number;
    pageSize: number;
    paged: boolean;
    sort: any[];
    unpaged: boolean;
  };
  size: number;
  sort: any[];
  totalElements: number;
  totalPages: number;
}
