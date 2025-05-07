import dayjs from 'dayjs/esm';

import { IReportSettimanale, NewReportSettimanale } from './report-settimanale.model';

export const sampleWithRequiredData: IReportSettimanale = {
  id: 13820,
  dataCreazione: dayjs('2024-12-12'),
  dataScadenza: dayjs('2024-12-12'),
};

export const sampleWithPartialData: IReportSettimanale = {
  id: 18547,
  commentoAllenamento: '../fake-data/blob/hipster.txt',
  qualitaSonno: 'BUONO',
  dataCreazione: dayjs('2024-12-12'),
  dataScadenza: dayjs('2024-12-13'),
};

export const sampleWithFullData: IReportSettimanale = {
  id: 20717,
  voto: 'OTTIMO',
  commentoAllenamento: '../fake-data/blob/hipster.txt',
  giorniDieta: 'SUFFICIENTE',
  pesoMedio: 1553.18,
  qualitaSonno: 'INSUFFICIENTE',
  mediaOreSonno: '11299',
  dataCreazione: dayjs('2024-12-12'),
  dataScadenza: dayjs('2024-12-12'),
  dataCompletamento: dayjs('2024-12-12'),
  puntuale: true,
  analisiReport: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewReportSettimanale = {
  dataCreazione: dayjs('2024-12-12'),
  dataScadenza: dayjs('2024-12-13'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
