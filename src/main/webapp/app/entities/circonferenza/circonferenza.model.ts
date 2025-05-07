import dayjs from 'dayjs/esm';
import { ICliente } from 'app/entities/cliente/cliente.model';

export interface ICirconferenza {
  id: number;
  torace?: number | null;
  braccio?: number | null;
  avambraccio?: number | null;
  ombelico?: number | null;
  fianchi?: number | null;
  sottoOmbelico?: number | null;
  vita?: number | null;
  coscia?: number | null;
  mese?: number | null;
  dataInserimento?: dayjs.Dayjs | null;
  cliente?: Pick<ICliente, 'id' | 'email'> | null;
}

export type NewCirconferenza = Omit<ICirconferenza, 'id'> & { id: null };
