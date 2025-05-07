import { IPassaggioEsercizio, NewPassaggioEsercizio } from './passaggio-esercizio.model';

export const sampleWithRequiredData: IPassaggioEsercizio = {
  id: 26519,
  indice: 13061,
  descrizione: '../fake-data/blob/hipster.txt',
};

export const sampleWithPartialData: IPassaggioEsercizio = {
  id: 14568,
  indice: 14525,
  descrizione: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IPassaggioEsercizio = {
  id: 26910,
  indice: 5917,
  descrizione: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewPassaggioEsercizio = {
  indice: 1261,
  descrizione: '../fake-data/blob/hipster.txt',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
