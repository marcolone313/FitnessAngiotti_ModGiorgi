import dayjs from 'dayjs/esm';

import { IPesoCliente, NewPesoCliente } from './peso-cliente.model';

export const sampleWithRequiredData: IPesoCliente = {
  id: 28328,
  peso: 542.6,
  mese: 5,
  dataInserimento: dayjs('2024-12-12'),
};

export const sampleWithPartialData: IPesoCliente = {
  id: 1982,
  peso: 13085.67,
  mese: 7,
  dataInserimento: dayjs('2024-12-12'),
};

export const sampleWithFullData: IPesoCliente = {
  id: 28353,
  peso: 7879.52,
  mese: 7,
  dataInserimento: dayjs('2024-12-12'),
};

export const sampleWithNewData: NewPesoCliente = {
  peso: 1708.75,
  mese: 12,
  dataInserimento: dayjs('2024-12-12'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
