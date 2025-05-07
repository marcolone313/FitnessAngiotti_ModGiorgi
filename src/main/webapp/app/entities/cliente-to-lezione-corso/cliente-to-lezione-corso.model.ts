import dayjs from 'dayjs/esm';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { ILezioneCorso } from 'app/entities/lezione-corso/lezione-corso.model';

export interface IClienteToLezioneCorso {
  id: number;
  completato?: boolean | null;
  dataCompletamento?: dayjs.Dayjs | null;
  cliente?: Pick<ICliente, 'id' | 'email'> | null;
  lezioneCorso?: Pick<ILezioneCorso, 'id' | 'titolo'> | null;
}

export type NewClienteToLezioneCorso = Omit<IClienteToLezioneCorso, 'id'> & { id: null };
