import { IProduct, NewProduct } from './product.model';

export const sampleWithRequiredData: IProduct = {
  id: 11737,
  name: 'hm sleepily',
  price: 17510.56,
  prosuctSize: 'S',
};

export const sampleWithPartialData: IProduct = {
  id: 22728,
  name: 'sore ack unsteady',
  price: 6707.04,
  prosuctSize: 'M',
  image: '../fake-data/blob/hipster.png',
  imageContentType: 'unknown',
};

export const sampleWithFullData: IProduct = {
  id: 4403,
  name: 'cafe',
  description: 'along amongst',
  price: 12917.62,
  prosuctSize: 'S',
  image: '../fake-data/blob/hipster.png',
  imageContentType: 'unknown',
};

export const sampleWithNewData: NewProduct = {
  name: 'anti inject why',
  price: 26562.23,
  prosuctSize: 'S',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
