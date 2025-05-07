import { IAllenamentoGiornaliero } from 'app/entities/allenamento-giornaliero/allenamento-giornaliero.model';

export interface ICorsa {
  id: number;
  distanzaDaPercorrere?: number | null;
  tempoImpiegato?: string | null;
  svolto?: boolean | null;
  completato?: boolean | null;
  feedback?: string | null;
  allenamentoGiornaliero?: Pick<IAllenamentoGiornaliero, 'id'> | null;
}

export type NewCorsa = Omit<ICorsa, 'id'> & { id: null };
