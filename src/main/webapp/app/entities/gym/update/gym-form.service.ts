import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IGym, NewGym } from '../gym.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IGym for edit and NewGymFormGroupInput for create.
 */
type GymFormGroupInput = IGym | PartialWithRequiredKeyOf<NewGym>;

type GymFormDefaults = Pick<NewGym, 'id' | 'completato' | 'svolto'>;

type GymFormGroupContent = {
  id: FormControl<IGym['id'] | NewGym['id']>;
  sets: FormControl<IGym['sets']>;
  reps: FormControl<IGym['reps']>;
  recupero: FormControl<IGym['recupero']>;
  peso: FormControl<IGym['peso']>;
  completato: FormControl<IGym['completato']>;
  svolto: FormControl<IGym['svolto']>;
  feedback: FormControl<IGym['feedback']>;
  allenamentoGiornaliero: FormControl<IGym['allenamentoGiornaliero']>;
  esercizio: FormControl<IGym['esercizio']>;
};

export type GymFormGroup = FormGroup<GymFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class GymFormService {
  createGymFormGroup(gym: GymFormGroupInput = { id: null }): GymFormGroup {
    const gymRawValue = {
      ...this.getFormDefaults(),
      ...gym,
    };
    return new FormGroup<GymFormGroupContent>({
      id: new FormControl(
        { value: gymRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      sets: new FormControl(gymRawValue.sets, {
        validators: [Validators.required],
      }),
      reps: new FormControl(gymRawValue.reps, {
        validators: [Validators.required],
      }),
      recupero: new FormControl(gymRawValue.recupero, {
        validators: [Validators.required],
      }),
      peso: new FormControl(gymRawValue.peso, {
        validators: [Validators.min(0)],
      }),
      completato: new FormControl(gymRawValue.completato),
      svolto: new FormControl(gymRawValue.svolto),
      feedback: new FormControl(gymRawValue.feedback),
      allenamentoGiornaliero: new FormControl(gymRawValue.allenamentoGiornaliero, {
        validators: [Validators.required],
      }),
      esercizio: new FormControl(gymRawValue.esercizio, {
        validators: [Validators.required],
      }),
    });
  }

  getGym(form: GymFormGroup): IGym | NewGym {
    return form.getRawValue() as IGym | NewGym;
  }

  resetForm(form: GymFormGroup, gym: GymFormGroupInput): void {
    const gymRawValue = { ...this.getFormDefaults(), ...gym };
    form.reset(
      {
        ...gymRawValue,
        id: { value: gymRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): GymFormDefaults {
    return {
      id: null,
      completato: false,
      svolto: false,
    };
  }
}
