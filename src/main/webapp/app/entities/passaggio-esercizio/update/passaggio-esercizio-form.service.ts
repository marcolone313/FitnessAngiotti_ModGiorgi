import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPassaggioEsercizio, NewPassaggioEsercizio } from '../passaggio-esercizio.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPassaggioEsercizio for edit and NewPassaggioEsercizioFormGroupInput for create.
 */
type PassaggioEsercizioFormGroupInput = IPassaggioEsercizio | PartialWithRequiredKeyOf<NewPassaggioEsercizio>;

type PassaggioEsercizioFormDefaults = Pick<NewPassaggioEsercizio, 'id'>;

type PassaggioEsercizioFormGroupContent = {
  id: FormControl<IPassaggioEsercizio['id'] | NewPassaggioEsercizio['id']>;
  indice: FormControl<IPassaggioEsercizio['indice']>;
  descrizione: FormControl<IPassaggioEsercizio['descrizione']>;
  esercizio: FormControl<IPassaggioEsercizio['esercizio']>;
};

export type PassaggioEsercizioFormGroup = FormGroup<PassaggioEsercizioFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PassaggioEsercizioFormService {
  createPassaggioEsercizioFormGroup(passaggioEsercizio: PassaggioEsercizioFormGroupInput = { id: null }): PassaggioEsercizioFormGroup {
    const passaggioEsercizioRawValue = {
      ...this.getFormDefaults(),
      ...passaggioEsercizio,
    };
    return new FormGroup<PassaggioEsercizioFormGroupContent>({
      id: new FormControl(
        { value: passaggioEsercizioRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      indice: new FormControl(passaggioEsercizioRawValue.indice, {
        validators: [Validators.required, Validators.min(1)],
      }),
      descrizione: new FormControl(passaggioEsercizioRawValue.descrizione, {
        validators: [Validators.required],
      }),
      esercizio: new FormControl(passaggioEsercizioRawValue.esercizio, {
        validators: [Validators.required],
      }),
    });
  }

  getPassaggioEsercizio(form: PassaggioEsercizioFormGroup): IPassaggioEsercizio | NewPassaggioEsercizio {
    return form.getRawValue() as IPassaggioEsercizio | NewPassaggioEsercizio;
  }

  resetForm(form: PassaggioEsercizioFormGroup, passaggioEsercizio: PassaggioEsercizioFormGroupInput): void {
    const passaggioEsercizioRawValue = { ...this.getFormDefaults(), ...passaggioEsercizio };
    form.reset(
      {
        ...passaggioEsercizioRawValue,
        id: { value: passaggioEsercizioRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PassaggioEsercizioFormDefaults {
    return {
      id: null,
    };
  }
}
