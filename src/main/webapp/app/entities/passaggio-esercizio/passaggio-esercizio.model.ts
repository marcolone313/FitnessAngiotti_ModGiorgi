import { IEsercizio } from 'app/entities/esercizio/esercizio.model';

export interface IPassaggioEsercizio {
  id: number;
  indice?: number | null;
  descrizione?: string | null;
  esercizio?: Pick<IEsercizio, 'id' | 'nome'> | null;
}

export type NewPassaggioEsercizio = Omit<IPassaggioEsercizio, 'id'> & { id: null };
