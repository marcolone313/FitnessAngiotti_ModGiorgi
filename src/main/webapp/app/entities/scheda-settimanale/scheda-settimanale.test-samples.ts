import dayjs from 'dayjs/esm';

import { ISchedaSettimanale, NewSchedaSettimanale } from './scheda-settimanale.model';

export const sampleWithRequiredData: ISchedaSettimanale = {
  id: 28693,
  anno: 3377,
  mese: 8,
  settimana: 1,
  dataCreazione: dayjs('2024-12-12'),
};

export const sampleWithPartialData: ISchedaSettimanale = {
  id: 28239,
  anno: 21602,
  mese: 4,
  settimana: 3,
  dataCreazione: dayjs('2024-12-13'),
};

export const sampleWithFullData: ISchedaSettimanale = {
  id: 29785,
  anno: 10177,
  mese: 11,
  settimana: 4,
  dataCreazione: dayjs('2024-12-13'),
};

export const sampleWithNewData: NewSchedaSettimanale = {
  anno: 4889,
  mese: 1,
  settimana: 3,
  dataCreazione: dayjs('2024-12-12'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
