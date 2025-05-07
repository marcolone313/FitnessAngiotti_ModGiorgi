import dayjs from 'dayjs/esm';

import { IAllenamentoGiornaliero, NewAllenamentoGiornaliero } from './allenamento-giornaliero.model';

export const sampleWithRequiredData: IAllenamentoGiornaliero = {
  id: 20683,
  tipo: 'GYM',
  giorno: 5,
};

export const sampleWithPartialData: IAllenamentoGiornaliero = {
  id: 17697,
  tipo: 'CIRCUITO',
  giorno: 5,
  svolto: true,
};

export const sampleWithFullData: IAllenamentoGiornaliero = {
  id: 16838,
  tipo: 'CIRCUITO',
  giorno: 6,
  notaTrainer: '../fake-data/blob/hipster.txt',
  svolto: false,
  dataAllenamento: dayjs('2024-12-12'),
};

export const sampleWithNewData: NewAllenamentoGiornaliero = {
  tipo: 'GYM',
  giorno: 1,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
