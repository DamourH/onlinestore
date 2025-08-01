import { IProduct } from 'app/entities/product/product.model';

export interface IProductCategory {
  id: number;
  name?: string | null;
  description?: string | null;
  products?: IProduct[] | null;
}

export type NewProductCategory = Omit<IProductCategory, 'id'> & { id: null };
