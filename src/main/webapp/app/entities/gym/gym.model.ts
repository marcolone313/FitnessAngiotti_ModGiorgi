import { IAllenamentoGiornaliero } from 'app/entities/allenamento-giornaliero/allenamento-giornaliero.model';
import { IEsercizio } from 'app/entities/esercizio/esercizio.model';

export interface IGym {
  id: number;
  sets?: number | null;
  reps?: number | null;
  recupero?: string | null;
  peso?: number | null;
  completato?: boolean | null;
  svolto?: boolean | null;
  feedback?: string | null;
  allenamentoGiornaliero?: Pick<IAllenamentoGiornaliero, 'id'> | null;
  esercizio?: Pick<IEsercizio, 'id' | 'nome'> | null;
}

export type NewGym = Omit<IGym, 'id'> & { id: null };
