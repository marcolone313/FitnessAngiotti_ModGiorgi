import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';

export interface ICliente {
  id: number;
  nome?: string | null;
  cognome?: string | null;
  dataDiNascita?: dayjs.Dayjs | null;
  codiceFiscale?: string | null;
  codiceCliente?: string | null;
  email?: string | null;
  telefono?: string | null;
  foto?: string | null;
  fotoContentType?: string | null;
  user?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewCliente = Omit<ICliente, 'id'> & { id: null };
