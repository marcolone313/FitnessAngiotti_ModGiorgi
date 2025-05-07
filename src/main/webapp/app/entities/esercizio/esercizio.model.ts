import { TipoEsercizio } from 'app/entities/enumerations/tipo-esercizio.model';

export interface IEsercizio {
  id: number;
  nome?: string | null;
  tipo?: keyof typeof TipoEsercizio | null;
  videoUrl?: string | null;
  descrizione?: string | null;
  foto?: string | null;
  fotoContentType?: string | null;
}

export type NewEsercizio = Omit<IEsercizio, 'id'> & { id: null };
