import dayjs from 'dayjs/esm';
import { ICliente } from 'app/entities/cliente/cliente.model';

export interface ISchedaSettimanale {
  id: number;
  anno?: number | null;
  mese?: number | null;
  settimana?: number | null;
  dataCreazione?: dayjs.Dayjs | null;
  cliente?: Pick<ICliente, 'id' | 'email'> | null;
}

export type NewSchedaSettimanale = Omit<ISchedaSettimanale, 'id'> & { id: null };
