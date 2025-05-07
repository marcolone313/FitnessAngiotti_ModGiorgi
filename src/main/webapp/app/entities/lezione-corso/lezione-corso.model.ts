import { ICorsoAcademy } from 'app/entities/corso-academy/corso-academy.model';

export interface ILezioneCorso {
  id: number;
  titolo?: string | null;
  descrizione?: string | null;
  puntiFocali?: string | null;
  videoUrl?: string | null;
  corsoAcademy?: Pick<ICorsoAcademy, 'id' | 'titolo'> | null;
}

export type NewLezioneCorso = Omit<ILezioneCorso, 'id'> & { id: null };
