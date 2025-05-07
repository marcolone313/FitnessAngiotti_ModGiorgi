import { IEsercizio } from 'app/entities/esercizio/esercizio.model';
import { ICircuito } from 'app/entities/circuito/circuito.model';

export interface ICircuitoToEsercizio {
  id: number;
  reps?: number | null;
  esercizio?: Pick<IEsercizio, 'id' | 'nome'> | null;
  circuito?: Pick<ICircuito, 'id'> | null;
}

export type NewCircuitoToEsercizio = Omit<ICircuitoToEsercizio, 'id'> & { id: null };
