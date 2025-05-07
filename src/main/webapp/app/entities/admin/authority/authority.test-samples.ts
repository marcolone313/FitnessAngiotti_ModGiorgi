import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: 'e9fb5857-632e-4d42-9832-1f886df14e25',
};

export const sampleWithPartialData: IAuthority = {
  name: '1eb2f423-5bc8-4a7c-b4ca-fe53ae565693',
};

export const sampleWithFullData: IAuthority = {
  name: 'e25229d0-59b4-45d3-821e-ac9d4a9b45a5',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
