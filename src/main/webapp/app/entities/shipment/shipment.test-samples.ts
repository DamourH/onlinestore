import dayjs from 'dayjs/esm';

import { IShipment, NewShipment } from './shipment.model';

export const sampleWithRequiredData: IShipment = {
  id: 27253,
  date: dayjs('2025-07-31T19:38'),
};

export const sampleWithPartialData: IShipment = {
  id: 10898,
  trackingCode: 'switch shiny',
  date: dayjs('2025-08-01T00:01'),
  details: 'strong though',
};

export const sampleWithFullData: IShipment = {
  id: 31355,
  trackingCode: 'toady crackle',
  date: dayjs('2025-08-01T05:53'),
  details: 'blind onto offensively',
};

export const sampleWithNewData: NewShipment = {
  date: dayjs('2025-07-31T17:53'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
