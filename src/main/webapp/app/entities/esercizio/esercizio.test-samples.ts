import { IEsercizio, NewEsercizio } from './esercizio.model';

export const sampleWithRequiredData: IEsercizio = {
  id: 4779,
  nome: 'why',
  tipo: 'FULLBODY',
};

export const sampleWithPartialData: IEsercizio = {
  id: 5380,
  nome: 'uniform',
  tipo: 'POSTURALE',
  videoUrl: 'lest handy for',
  descrizione: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IEsercizio = {
  id: 5454,
  nome: 'large',
  tipo: 'POSTURALE',
  videoUrl: 'gosh or while',
  descrizione: '../fake-data/blob/hipster.txt',
  foto: '../fake-data/blob/hipster.png',
  fotoContentType: 'unknown',
};

export const sampleWithNewData: NewEsercizio = {
  nome: 'intensely trust season',
  tipo: 'POSTURALE',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
