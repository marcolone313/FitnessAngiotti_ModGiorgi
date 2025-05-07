import { LivelloCorso } from 'app/entities/enumerations/livello-corso.model';

export interface ICorsoAcademy {
  id: number;
  livello?: keyof typeof LivelloCorso | null;
  titolo?: string | null;
}

export type NewCorsoAcademy = Omit<ICorsoAcademy, 'id'> & { id: null };
