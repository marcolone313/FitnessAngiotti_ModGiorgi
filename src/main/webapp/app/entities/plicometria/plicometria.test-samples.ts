import dayjs from 'dayjs/esm';

import { IPlicometria, NewPlicometria } from './plicometria.model';

export const sampleWithRequiredData: IPlicometria = {
  id: 28396,
  mese: 3,
  dataInserimento: dayjs('2024-12-12'),
};

export const sampleWithPartialData: IPlicometria = {
  id: 386,
  petto: 10848.66,
  mese: 8,
  dataInserimento: dayjs('2024-12-12'),
};

export const sampleWithFullData: IPlicometria = {
  id: 2492,
  tricipite: 17781.63,
  petto: 10331.39,
  ascella: 28833.91,
  sottoscapolare: 22014.7,
  soprailliaca: 11769.91,
  addome: 3049.2,
  coscia: 23909.4,
  mese: 9,
  dataInserimento: dayjs('2024-12-13'),
};

export const sampleWithNewData: NewPlicometria = {
  mese: 8,
  dataInserimento: dayjs('2024-12-13'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
