import { ILezioneCorso, NewLezioneCorso } from './lezione-corso.model';

export const sampleWithRequiredData: ILezioneCorso = {
  id: 22750,
  titolo: 'fraudster yeast',
};

export const sampleWithPartialData: ILezioneCorso = {
  id: 16592,
  titolo: 'if weep to',
  descrizione: '../fake-data/blob/hipster.txt',
  puntiFocali: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: ILezioneCorso = {
  id: 13770,
  titolo: 'phew gosh yowza',
  descrizione: '../fake-data/blob/hipster.txt',
  puntiFocali: '../fake-data/blob/hipster.txt',
  videoUrl: 'warm personalise',
};

export const sampleWithNewData: NewLezioneCorso = {
  titolo: 'oh flickering',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
