import { ICircuito, NewCircuito } from './circuito.model';

export const sampleWithRequiredData: ICircuito = {
  id: 5979,
};

export const sampleWithPartialData: ICircuito = {
  id: 30156,
  tempoLimite: '21482',
  feedback: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: ICircuito = {
  id: 29616,
  tempoLimite: '234',
  tempoImpiegato: '15017',
  catenaRipetizioni: 'coolly',
  circuitiCompletati: 23779,
  svolto: false,
  completato: false,
  feedback: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewCircuito = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
