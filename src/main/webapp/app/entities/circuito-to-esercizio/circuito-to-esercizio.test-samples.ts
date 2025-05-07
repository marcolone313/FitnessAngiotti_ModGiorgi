import { ICircuitoToEsercizio, NewCircuitoToEsercizio } from './circuito-to-esercizio.model';

export const sampleWithRequiredData: ICircuitoToEsercizio = {
  id: 29922,
  reps: 5655,
};

export const sampleWithPartialData: ICircuitoToEsercizio = {
  id: 26764,
  reps: 4753,
};

export const sampleWithFullData: ICircuitoToEsercizio = {
  id: 8442,
  reps: 10456,
};

export const sampleWithNewData: NewCircuitoToEsercizio = {
  reps: 226,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
