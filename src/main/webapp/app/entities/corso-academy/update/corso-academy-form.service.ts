import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICorsoAcademy, NewCorsoAcademy } from '../corso-academy.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICorsoAcademy for edit and NewCorsoAcademyFormGroupInput for create.
 */
type CorsoAcademyFormGroupInput = ICorsoAcademy | PartialWithRequiredKeyOf<NewCorsoAcademy>;

type CorsoAcademyFormDefaults = Pick<NewCorsoAcademy, 'id'>;

type CorsoAcademyFormGroupContent = {
  id: FormControl<ICorsoAcademy['id'] | NewCorsoAcademy['id']>;
  livello: FormControl<ICorsoAcademy['livello']>;
  titolo: FormControl<ICorsoAcademy['titolo']>;
};

export type CorsoAcademyFormGroup = FormGroup<CorsoAcademyFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CorsoAcademyFormService {
  createCorsoAcademyFormGroup(corsoAcademy: CorsoAcademyFormGroupInput = { id: null }): CorsoAcademyFormGroup {
    const corsoAcademyRawValue = {
      ...this.getFormDefaults(),
      ...corsoAcademy,
    };
    return new FormGroup<CorsoAcademyFormGroupContent>({
      id: new FormControl(
        { value: corsoAcademyRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      livello: new FormControl(corsoAcademyRawValue.livello, {
        validators: [Validators.required],
      }),
      titolo: new FormControl(corsoAcademyRawValue.titolo, {
        validators: [Validators.required],
      }),
    });
  }

  getCorsoAcademy(form: CorsoAcademyFormGroup): ICorsoAcademy | NewCorsoAcademy {
    return form.getRawValue() as ICorsoAcademy | NewCorsoAcademy;
  }

  resetForm(form: CorsoAcademyFormGroup, corsoAcademy: CorsoAcademyFormGroupInput): void {
    const corsoAcademyRawValue = { ...this.getFormDefaults(), ...corsoAcademy };
    form.reset(
      {
        ...corsoAcademyRawValue,
        id: { value: corsoAcademyRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CorsoAcademyFormDefaults {
    return {
      id: null,
    };
  }
}
