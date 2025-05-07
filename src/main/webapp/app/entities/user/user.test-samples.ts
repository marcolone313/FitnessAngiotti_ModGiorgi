import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 8124,
  login: 'fSh',
};

export const sampleWithPartialData: IUser = {
  id: 10155,
  login: 'Ikp',
};

export const sampleWithFullData: IUser = {
  id: 29100,
  login: 'RC22z',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
