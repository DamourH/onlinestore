import { ICustomer, NewCustomer } from './customer.model';

export const sampleWithRequiredData: ICustomer = {
  id: 3366,
  firstName: 'Lenora',
  lastName: 'Little',
  gender: 'MALE',
  email: '6vnZ@kJa6.OGBF:G',
  phone: '471-942-4032 x9322',
  addressLine1: 'card concrete by',
  city: 'Port Luluburgh',
  country: 'Bangladesh',
};

export const sampleWithPartialData: ICustomer = {
  id: 29642,
  firstName: 'Lyla',
  lastName: 'Gibson',
  gender: 'MALE',
  email: 'w^YN,C@CX,.Ja}C{g',
  phone: '787.688.7860 x82397',
  addressLine1: 'lively midst obsess',
  city: 'Port Wava',
  country: 'Venezuela',
};

export const sampleWithFullData: ICustomer = {
  id: 4149,
  firstName: 'Lyla',
  lastName: 'Langosh',
  gender: 'OTHER',
  email: '&b@/:=dp.L%^lz',
  phone: '(826) 532-1028',
  addressLine1: 'pace',
  addressLine2: 'fencing near',
  city: 'Rennermouth',
  country: 'Barbados',
};

export const sampleWithNewData: NewCustomer = {
  firstName: 'Irma',
  lastName: 'Davis',
  gender: 'FEMALE',
  email: 'LDvdzx@+]./l',
  phone: '724-719-5737 x718',
  addressLine1: 'rationale premeditation',
  city: 'Collierville',
  country: 'Suriname',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
