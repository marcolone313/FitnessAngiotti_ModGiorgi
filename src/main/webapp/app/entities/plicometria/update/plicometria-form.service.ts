import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPlicometria, NewPlicometria } from '../plicometria.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPlicometria for edit and NewPlicometriaFormGroupInput for create.
 */
type PlicometriaFormGroupInput = IPlicometria | PartialWithRequiredKeyOf<NewPlicometria>;

type PlicometriaFormDefaults = Pick<NewPlicometria, 'id'>;

type PlicometriaFormGroupContent = {
  id: FormControl<IPlicometria['id'] | NewPlicometria['id']>;
  tricipite: FormControl<IPlicometria['tricipite']>;
  petto: FormControl<IPlicometria['petto']>;
  ascella: FormControl<IPlicometria['ascella']>;
  sottoscapolare: FormControl<IPlicometria['sottoscapolare']>;
  soprailliaca: FormControl<IPlicometria['soprailliaca']>;
  addome: FormControl<IPlicometria['addome']>;
  coscia: FormControl<IPlicometria['coscia']>;
  mese: FormControl<IPlicometria['mese']>;
  dataInserimento: FormControl<IPlicometria['dataInserimento']>;
  cliente: FormControl<IPlicometria['cliente']>;
};

export type PlicometriaFormGroup = FormGroup<PlicometriaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PlicometriaFormService {
  createPlicometriaFormGroup(plicometria: PlicometriaFormGroupInput = { id: null }): PlicometriaFormGroup {
    const plicometriaRawValue = {
      ...this.getFormDefaults(),
      ...plicometria,
    };
    return new FormGroup<PlicometriaFormGroupContent>({
      id: new FormControl(
        { value: plicometriaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      tricipite: new FormControl(plicometriaRawValue.tricipite, {
        validators: [Validators.min(0)],
      }),
      petto: new FormControl(plicometriaRawValue.petto, {
        validators: [Validators.min(0)],
      }),
      ascella: new FormControl(plicometriaRawValue.ascella, {
        validators: [Validators.min(0)],
      }),
      sottoscapolare: new FormControl(plicometriaRawValue.sottoscapolare, {
        validators: [Validators.min(0)],
      }),
      soprailliaca: new FormControl(plicometriaRawValue.soprailliaca, {
        validators: [Validators.min(0)],
      }),
      addome: new FormControl(plicometriaRawValue.addome, {
        validators: [Validators.min(0)],
      }),
      coscia: new FormControl(plicometriaRawValue.coscia, {
        validators: [Validators.min(0)],
      }),
      mese: new FormControl(plicometriaRawValue.mese, {
        validators: [Validators.required, Validators.min(1), Validators.max(12)],
      }),
      dataInserimento: new FormControl(plicometriaRawValue.dataInserimento, {
        validators: [Validators.required],
      }),
      cliente: new FormControl(plicometriaRawValue.cliente, {
        validators: [Validators.required],
      }),
    });
  }

  getPlicometria(form: PlicometriaFormGroup): IPlicometria | NewPlicometria {
    return form.getRawValue() as IPlicometria | NewPlicometria;
  }

  resetForm(form: PlicometriaFormGroup, plicometria: PlicometriaFormGroupInput): void {
    const plicometriaRawValue = { ...this.getFormDefaults(), ...plicometria };
    form.reset(
      {
        ...plicometriaRawValue,
        id: { value: plicometriaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PlicometriaFormDefaults {
    return {
      id: null,
    };
  }
}
