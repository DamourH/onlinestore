import dayjs from 'dayjs/esm';

import { IProductOrder, NewProductOrder } from './product-order.model';

export const sampleWithRequiredData: IProductOrder = {
  id: 21512,
  placedDate: dayjs('2025-08-01T03:48'),
  status: 'CANCELLED',
  code: 'annually angrily tankful',
};

export const sampleWithPartialData: IProductOrder = {
  id: 5807,
  placedDate: dayjs('2025-07-31T22:57'),
  status: 'CANCELLED',
  code: 'bleak hmph',
};

export const sampleWithFullData: IProductOrder = {
  id: 12877,
  placedDate: dayjs('2025-07-31T14:07'),
  status: 'CANCELLED',
  code: 'parched kissingly',
};

export const sampleWithNewData: NewProductOrder = {
  placedDate: dayjs('2025-08-01T02:15'),
  status: 'PENDING',
  code: 'quickly unabashedly',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
