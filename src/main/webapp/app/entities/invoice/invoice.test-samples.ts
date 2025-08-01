import dayjs from 'dayjs/esm';

import { IInvoice, NewInvoice } from './invoice.model';

export const sampleWithRequiredData: IInvoice = {
  id: 29050,
  date: dayjs('2025-07-31T18:14'),
  status: 'ISSUED',
  paymentMethod: 'PAYPAL',
  paymentDate: dayjs('2025-07-31T22:31'),
  paymentAmount: 22670.56,
};

export const sampleWithPartialData: IInvoice = {
  id: 9408,
  date: dayjs('2025-07-31T20:59'),
  status: 'ISSUED',
  paymentMethod: 'CASH_ON_DELIVERY',
  paymentDate: dayjs('2025-07-31T21:45'),
  paymentAmount: 4754.07,
};

export const sampleWithFullData: IInvoice = {
  id: 19628,
  date: dayjs('2025-07-31T19:01'),
  details: 'in fess',
  status: 'PAID',
  paymentMethod: 'CASH_ON_DELIVERY',
  paymentDate: dayjs('2025-07-31T10:01'),
  paymentAmount: 25939.3,
};

export const sampleWithNewData: NewInvoice = {
  date: dayjs('2025-07-31T18:56'),
  status: 'ISSUED',
  paymentMethod: 'CASH_ON_DELIVERY',
  paymentDate: dayjs('2025-08-01T02:55'),
  paymentAmount: 31792.67,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
