import { IAllenamentoGiornaliero } from 'app/entities/allenamento-giornaliero/allenamento-giornaliero.model';

export interface ICircuito {
  id: number;
  tempoLimite?: string | null;
  tempoImpiegato?: string | null;
  catenaRipetizioni?: string | null;
  circuitiCompletati?: number | null;
  svolto?: boolean | null;
  completato?: boolean | null;
  feedback?: string | null;
  allenamentoGiornaliero?: Pick<IAllenamentoGiornaliero, 'id'> | null;
}

export type NewCircuito = Omit<ICircuito, 'id'> & { id: null };
