import { ICorsoAcademy, NewCorsoAcademy } from './corso-academy.model';

export const sampleWithRequiredData: ICorsoAcademy = {
  id: 28077,
  livello: 'PRINCIPIANTE',
  titolo: 'sedately gladly',
};

export const sampleWithPartialData: ICorsoAcademy = {
  id: 29121,
  livello: 'AVANZATO',
  titolo: 'throughout down',
};

export const sampleWithFullData: ICorsoAcademy = {
  id: 6740,
  livello: 'PRINCIPIANTE',
  titolo: 'till jubilant black-and-white',
};

export const sampleWithNewData: NewCorsoAcademy = {
  livello: 'INTERMEDIO',
  titolo: 'pulse eek upwardly',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
