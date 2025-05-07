import { IGym, NewGym } from './gym.model';

export const sampleWithRequiredData: IGym = {
  id: 29332,
  sets: 25289,
  reps: 3891,
  recupero: '27024',
};

export const sampleWithPartialData: IGym = {
  id: 26124,
  sets: 16713,
  reps: 3529,
  recupero: '25863',
  svolto: false,
  feedback: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IGym = {
  id: 532,
  sets: 23975,
  reps: 7971,
  recupero: '2771',
  peso: 11876.73,
  completato: false,
  svolto: false,
  feedback: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewGym = {
  sets: 4442,
  reps: 9665,
  recupero: '19712',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
