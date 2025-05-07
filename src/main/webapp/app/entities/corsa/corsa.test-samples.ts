import { ICorsa, NewCorsa } from './corsa.model';

export const sampleWithRequiredData: ICorsa = {
  id: 1643,
  distanzaDaPercorrere: 25342.12,
};

export const sampleWithPartialData: ICorsa = {
  id: 14756,
  distanzaDaPercorrere: 29175.69,
  completato: false,
  feedback: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: ICorsa = {
  id: 17784,
  distanzaDaPercorrere: 19794.08,
  tempoImpiegato: '20724',
  svolto: false,
  completato: true,
  feedback: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewCorsa = {
  distanzaDaPercorrere: 20317.21,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
