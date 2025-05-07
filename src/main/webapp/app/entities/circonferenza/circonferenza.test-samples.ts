import dayjs from 'dayjs/esm';

import { ICirconferenza, NewCirconferenza } from './circonferenza.model';

export const sampleWithRequiredData: ICirconferenza = {
  id: 28372,
  mese: 7,
  dataInserimento: dayjs('2024-12-12'),
};

export const sampleWithPartialData: ICirconferenza = {
  id: 26413,
  torace: 21319.3,
  vita: 31896.12,
  mese: 2,
  dataInserimento: dayjs('2024-12-12'),
};

export const sampleWithFullData: ICirconferenza = {
  id: 551,
  torace: 22810.52,
  braccio: 24404.67,
  avambraccio: 24350.67,
  ombelico: 1868.3,
  fianchi: 18578.61,
  sottoOmbelico: 25304.82,
  vita: 1389.74,
  coscia: 29868.46,
  mese: 6,
  dataInserimento: dayjs('2024-12-12'),
};

export const sampleWithNewData: NewCirconferenza = {
  mese: 11,
  dataInserimento: dayjs('2024-12-13'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
