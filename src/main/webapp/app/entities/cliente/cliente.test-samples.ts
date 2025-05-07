import dayjs from 'dayjs/esm';

import { ICliente, NewCliente } from './cliente.model';

export const sampleWithRequiredData: ICliente = {
  id: 25913,
  nome: 'like stigmatize angelic',
  cognome: 'wash than apprehensive',
  dataDiNascita: dayjs('2024-12-13'),
  codiceFiscale: 'bend geeXXXXXXXX',
  codiceCliente: 'until bookend',
  email: 'Isidoro.Scaglione@yahoo.com',
};

export const sampleWithPartialData: ICliente = {
  id: 19925,
  nome: 'provided minister turret',
  cognome: 'firm',
  dataDiNascita: dayjs('2024-12-13'),
  codiceFiscale: 'resprayXXXXXXXXX',
  codiceCliente: 'table',
  email: 'Fernanda48@gmail.com',
  foto: '../fake-data/blob/hipster.png',
  fotoContentType: 'unknown',
};

export const sampleWithFullData: ICliente = {
  id: 5802,
  nome: 'ambitious hastily oof',
  cognome: 'cash officially',
  dataDiNascita: dayjs('2024-12-13'),
  codiceFiscale: 'failing ifXXXXXX',
  codiceCliente: 'reclassify',
  email: 'Robaldo73@libero.it',
  telefono: 'hence decryption',
  foto: '../fake-data/blob/hipster.png',
  fotoContentType: 'unknown',
};

export const sampleWithNewData: NewCliente = {
  nome: 'where',
  cognome: 'garage verify',
  dataDiNascita: dayjs('2024-12-12'),
  codiceFiscale: 'outrankXXXXXXXXX',
  codiceCliente: 'crowded scoff',
  email: 'Amilcare86@email.it',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
