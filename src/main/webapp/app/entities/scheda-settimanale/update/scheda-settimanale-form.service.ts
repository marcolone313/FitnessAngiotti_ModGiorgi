import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ISchedaSettimanale, NewSchedaSettimanale } from '../scheda-settimanale.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISchedaSettimanale for edit and NewSchedaSettimanaleFormGroupInput for create.
 */
type SchedaSettimanaleFormGroupInput = ISchedaSettimanale | PartialWithRequiredKeyOf<NewSchedaSettimanale>;

type SchedaSettimanaleFormDefaults = Pick<NewSchedaSettimanale, 'id'>;

type SchedaSettimanaleFormGroupContent = {
  id: FormControl<ISchedaSettimanale['id'] | NewSchedaSettimanale['id']>;
  anno: FormControl<ISchedaSettimanale['anno']>;
  mese: FormControl<ISchedaSettimanale['mese']>;
  settimana: FormControl<ISchedaSettimanale['settimana']>;
  dataCreazione: FormControl<ISchedaSettimanale['dataCreazione']>;
  cliente: FormControl<ISchedaSettimanale['cliente']>;
};

export type SchedaSettimanaleFormGroup = FormGroup<SchedaSettimanaleFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SchedaSettimanaleFormService {
  createSchedaSettimanaleFormGroup(schedaSettimanale: SchedaSettimanaleFormGroupInput = { id: null }): SchedaSettimanaleFormGroup {
    const schedaSettimanaleRawValue = {
      ...this.getFormDefaults(),
      ...schedaSettimanale,
    };
    return new FormGroup<SchedaSettimanaleFormGroupContent>({
      id: new FormControl(
        { value: schedaSettimanaleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      anno: new FormControl(schedaSettimanaleRawValue.anno, {
        validators: [Validators.required, Validators.min(0)],
      }),
      mese: new FormControl(schedaSettimanaleRawValue.mese, {
        validators: [Validators.required, Validators.min(1), Validators.max(12)],
      }),
      settimana: new FormControl(schedaSettimanaleRawValue.settimana, {
        validators: [Validators.required, Validators.min(1), Validators.max(4)],
      }),
      dataCreazione: new FormControl(schedaSettimanaleRawValue.dataCreazione, {
        validators: [Validators.required],
      }),
      cliente: new FormControl(schedaSettimanaleRawValue.cliente, {
        validators: [Validators.required],
      }),
    });
  }

  getSchedaSettimanale(form: SchedaSettimanaleFormGroup): ISchedaSettimanale | NewSchedaSettimanale {
    return form.getRawValue() as ISchedaSettimanale | NewSchedaSettimanale;
  }

  resetForm(form: SchedaSettimanaleFormGroup, schedaSettimanale: SchedaSettimanaleFormGroupInput): void {
    const schedaSettimanaleRawValue = { ...this.getFormDefaults(), ...schedaSettimanale };
    form.reset(
      {
        ...schedaSettimanaleRawValue,
        id: { value: schedaSettimanaleRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SchedaSettimanaleFormDefaults {
    return {
      id: null,
    };
  }
}
