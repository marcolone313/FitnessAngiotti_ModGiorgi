import dayjs from 'dayjs/esm';
import { ICliente } from 'app/entities/cliente/cliente.model';

export interface IPlicometria {
  id: number;
  tricipite?: number | null;
  petto?: number | null;
  ascella?: number | null;
  sottoscapolare?: number | null;
  soprailliaca?: number | null;
  addome?: number | null;
  coscia?: number | null;
  mese?: number | null;
  dataInserimento?: dayjs.Dayjs | null;
  cliente?: Pick<ICliente, 'id' | 'email'> | null;
}

export type NewPlicometria = Omit<IPlicometria, 'id'> & { id: null };
