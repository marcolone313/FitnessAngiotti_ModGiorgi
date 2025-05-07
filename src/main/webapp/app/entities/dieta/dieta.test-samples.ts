import dayjs from 'dayjs/esm';

import { IDieta, NewDieta } from './dieta.model';

export const sampleWithRequiredData: IDieta = {
  id: 27294,
  dataCreazione: dayjs('2024-12-12'),
  mese: 6,
  tipo: 'phooey suspiciously',
  macros: 'blah geez whose',
  fabbisognoCalorico: 'ostrich spear mockingly',
};

export const sampleWithPartialData: IDieta = {
  id: 12242,
  dataCreazione: dayjs('2024-12-13'),
  mese: 12,
  tipo: 'slowly ha',
  macros: 'always scent likely',
  fabbisognoCalorico: 'including as',
};

export const sampleWithFullData: IDieta = {
  id: 9404,
  dataCreazione: dayjs('2024-12-12'),
  mese: 7,
  tipo: 'boo huzzah',
  macros: 'quicker unless',
  fabbisognoCalorico: 'partial across zowie',
};

export const sampleWithNewData: NewDieta = {
  dataCreazione: dayjs('2024-12-12'),
  mese: 12,
  tipo: 'because why oh',
  macros: 'boo',
  fabbisognoCalorico: 'truthfully to',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
