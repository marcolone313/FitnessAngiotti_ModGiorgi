import dayjs from 'dayjs/esm';
import { ISchedaSettimanale } from 'app/entities/scheda-settimanale/scheda-settimanale.model';
import { TipoAllenamento } from 'app/entities/enumerations/tipo-allenamento.model';

export interface IAllenamentoGiornaliero {
  id: number;
  tipo?: keyof typeof TipoAllenamento | null;
  giorno?: number | null;
  notaTrainer?: string | null;
  svolto?: boolean | null;
  dataAllenamento?: dayjs.Dayjs | null;
  schedaSettimanale?: Pick<ISchedaSettimanale, 'id'> | null;
}

export type NewAllenamentoGiornaliero = Omit<IAllenamentoGiornaliero, 'id'> & { id: null };
