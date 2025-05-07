import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IDieta, NewDieta } from '../dieta.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDieta for edit and NewDietaFormGroupInput for create.
 */
type DietaFormGroupInput = IDieta | PartialWithRequiredKeyOf<NewDieta>;

type DietaFormDefaults = Pick<NewDieta, 'id'>;

type DietaFormGroupContent = {
  id: FormControl<IDieta['id'] | NewDieta['id']>;
  dataCreazione: FormControl<IDieta['dataCreazione']>;
  mese: FormControl<IDieta['mese']>;
  tipo: FormControl<IDieta['tipo']>;
  macros: FormControl<IDieta['macros']>;
  fabbisognoCalorico: FormControl<IDieta['fabbisognoCalorico']>;
  cliente: FormControl<IDieta['cliente']>;
};

export type DietaFormGroup = FormGroup<DietaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DietaFormService {
  createDietaFormGroup(dieta: DietaFormGroupInput = { id: null }): DietaFormGroup {
    const dietaRawValue = {
      ...this.getFormDefaults(),
      ...dieta,
    };
    return new FormGroup<DietaFormGroupContent>({
      id: new FormControl(
        { value: dietaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      dataCreazione: new FormControl(dietaRawValue.dataCreazione, {
        validators: [Validators.required],
      }),
      mese: new FormControl(dietaRawValue.mese, {
        validators: [Validators.required, Validators.min(1), Validators.max(12)],
      }),
      tipo: new FormControl(dietaRawValue.tipo, {
        validators: [Validators.required],
      }),
      macros: new FormControl(dietaRawValue.macros, {
        validators: [Validators.required],
      }),
      fabbisognoCalorico: new FormControl(dietaRawValue.fabbisognoCalorico, {
        validators: [Validators.required],
      }),
      cliente: new FormControl(dietaRawValue.cliente, {
        validators: [Validators.required],
      }),
    });
  }

  getDieta(form: DietaFormGroup): IDieta | NewDieta {
    return form.getRawValue() as IDieta | NewDieta;
  }

  resetForm(form: DietaFormGroup, dieta: DietaFormGroupInput): void {
    const dietaRawValue = { ...this.getFormDefaults(), ...dieta };
    form.reset(
      {
        ...dietaRawValue,
        id: { value: dietaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DietaFormDefaults {
    return {
      id: null,
    };
  }
}
