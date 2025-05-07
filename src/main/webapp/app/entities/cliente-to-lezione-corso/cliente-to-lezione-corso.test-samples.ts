import dayjs from 'dayjs/esm';

import { IClienteToLezioneCorso, NewClienteToLezioneCorso } from './cliente-to-lezione-corso.model';

export const sampleWithRequiredData: IClienteToLezioneCorso = {
  id: 32583,
};

export const sampleWithPartialData: IClienteToLezioneCorso = {
  id: 21552,
};

export const sampleWithFullData: IClienteToLezioneCorso = {
  id: 11903,
  completato: false,
  dataCompletamento: dayjs('2024-12-13'),
};

export const sampleWithNewData: NewClienteToLezioneCorso = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
