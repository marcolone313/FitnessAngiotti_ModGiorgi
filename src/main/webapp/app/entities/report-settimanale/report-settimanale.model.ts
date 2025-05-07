import dayjs from 'dayjs/esm';
import { ISchedaSettimanale } from 'app/entities/scheda-settimanale/scheda-settimanale.model';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { Voto } from 'app/entities/enumerations/voto.model';

export interface IReportSettimanale {
  id: number;
  voto?: keyof typeof Voto | null;
  commentoAllenamento?: string | null;
  giorniDieta?: keyof typeof Voto | null;
  pesoMedio?: number | null;
  qualitaSonno?: keyof typeof Voto | null;
  mediaOreSonno?: string | null;
  dataCreazione?: dayjs.Dayjs | null;
  dataScadenza?: dayjs.Dayjs | null;
  dataCompletamento?: dayjs.Dayjs | null;
  puntuale?: boolean | null;
  analisiReport?: string | null;
  schedaSettimanale?: Pick<ISchedaSettimanale, 'id'> | null;
  cliente?: Pick<ICliente, 'id' | 'email'> | null;
}

export type NewReportSettimanale = Omit<IReportSettimanale, 'id'> & { id: null };
