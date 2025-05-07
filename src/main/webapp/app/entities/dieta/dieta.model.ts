import dayjs from 'dayjs/esm';
import { ICliente } from 'app/entities/cliente/cliente.model';

export interface IDieta {
  id: number;
  dataCreazione?: dayjs.Dayjs | null;
  mese?: number | null;
  tipo?: string | null;
  macros?: string | null;
  fabbisognoCalorico?: string | null;
  cliente?: Pick<ICliente, 'id' | 'email'> | null;
}

export type NewDieta = Omit<IDieta, 'id'> & { id: null };
