import dayjs from 'dayjs/esm';
import { ICliente } from 'app/entities/cliente/cliente.model';

export interface IPesoCliente {
  id: number;
  peso?: number | null;
  mese?: number | null;
  dataInserimento?: dayjs.Dayjs | null;
  cliente?: Pick<ICliente, 'id' | 'email'> | null;
}

export type NewPesoCliente = Omit<IPesoCliente, 'id'> & { id: null };
