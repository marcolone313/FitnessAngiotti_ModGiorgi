import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IEsercizio, NewEsercizio } from '../esercizio.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEsercizio for edit and NewEsercizioFormGroupInput for create.
 */
type EsercizioFormGroupInput = IEsercizio | PartialWithRequiredKeyOf<NewEsercizio>;

type EsercizioFormDefaults = Pick<NewEsercizio, 'id'>;

type EsercizioFormGroupContent = {
  id: FormControl<IEsercizio['id'] | NewEsercizio['id']>;
  nome: FormControl<IEsercizio['nome']>;
  tipo: FormControl<IEsercizio['tipo']>;
  videoUrl: FormControl<IEsercizio['videoUrl']>;
  descrizione: FormControl<IEsercizio['descrizione']>;
  foto: FormControl<IEsercizio['foto']>;
  fotoContentType: FormControl<IEsercizio['fotoContentType']>;
};

export type EsercizioFormGroup = FormGroup<EsercizioFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EsercizioFormService {
  createEsercizioFormGroup(esercizio: EsercizioFormGroupInput = { id: null }): EsercizioFormGroup {
    const esercizioRawValue = {
      ...this.getFormDefaults(),
      ...esercizio,
    };
    return new FormGroup<EsercizioFormGroupContent>({
      id: new FormControl(
        { value: esercizioRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nome: new FormControl(esercizioRawValue.nome, {
        validators: [Validators.required],
      }),
      tipo: new FormControl(esercizioRawValue.tipo, {
        validators: [Validators.required],
      }),
      videoUrl: new FormControl(esercizioRawValue.videoUrl),
      descrizione: new FormControl(esercizioRawValue.descrizione),
      foto: new FormControl(esercizioRawValue.foto),
      fotoContentType: new FormControl(esercizioRawValue.fotoContentType),
    });
  }

  getEsercizio(form: EsercizioFormGroup): IEsercizio | NewEsercizio {
    return form.getRawValue() as IEsercizio | NewEsercizio;
  }

  resetForm(form: EsercizioFormGroup, esercizio: EsercizioFormGroupInput): void {
    const esercizioRawValue = { ...this.getFormDefaults(), ...esercizio };
    form.reset(
      {
        ...esercizioRawValue,
        id: { value: esercizioRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): EsercizioFormDefaults {
    return {
      id: null,
    };
  }
}
