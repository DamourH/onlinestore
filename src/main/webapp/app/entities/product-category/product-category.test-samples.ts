import { IProductCategory, NewProductCategory } from './product-category.model';

export const sampleWithRequiredData: IProductCategory = {
  id: 14928,
  name: 'jam-packed retract',
};

export const sampleWithPartialData: IProductCategory = {
  id: 19631,
  name: 'blindly failing',
};

export const sampleWithFullData: IProductCategory = {
  id: 13570,
  name: 'happy possession widow',
  description: 'pro unexpectedly forenenst',
};

export const sampleWithNewData: NewProductCategory = {
  name: 'suspiciously',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
