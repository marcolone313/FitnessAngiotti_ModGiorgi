import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IAllenamentoGiornaliero, NewAllenamentoGiornaliero } from '../allenamento-giornaliero.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAllenamentoGiornaliero for edit and NewAllenamentoGiornalieroFormGroupInput for create.
 */
type AllenamentoGiornalieroFormGroupInput = IAllenamentoGiornaliero | PartialWithRequiredKeyOf<NewAllenamentoGiornaliero>;

type AllenamentoGiornalieroFormDefaults = Pick<NewAllenamentoGiornaliero, 'id' | 'svolto'>;

type AllenamentoGiornalieroFormGroupContent = {
  id: FormControl<IAllenamentoGiornaliero['id'] | NewAllenamentoGiornaliero['id']>;
  tipo: FormControl<IAllenamentoGiornaliero['tipo']>;
  giorno: FormControl<IAllenamentoGiornaliero['giorno']>;
  notaTrainer: FormControl<IAllenamentoGiornaliero['notaTrainer']>;
  svolto: FormControl<IAllenamentoGiornaliero['svolto']>;
  dataAllenamento: FormControl<IAllenamentoGiornaliero['dataAllenamento']>;
  schedaSettimanale: FormControl<IAllenamentoGiornaliero['schedaSettimanale']>;
};

export type AllenamentoGiornalieroFormGroup = FormGroup<AllenamentoGiornalieroFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AllenamentoGiornalieroFormService {
  createAllenamentoGiornalieroFormGroup(
    allenamentoGiornaliero: AllenamentoGiornalieroFormGroupInput = { id: null },
  ): AllenamentoGiornalieroFormGroup {
    const allenamentoGiornalieroRawValue = {
      ...this.getFormDefaults(),
      ...allenamentoGiornaliero,
    };
    return new FormGroup<AllenamentoGiornalieroFormGroupContent>({
      id: new FormControl(
        { value: allenamentoGiornalieroRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      tipo: new FormControl(allenamentoGiornalieroRawValue.tipo, {
        validators: [Validators.required],
      }),
      giorno: new FormControl(allenamentoGiornalieroRawValue.giorno, {
        validators: [Validators.required, Validators.min(1), Validators.max(6)],
      }),
      notaTrainer: new FormControl(allenamentoGiornalieroRawValue.notaTrainer),
      svolto: new FormControl(allenamentoGiornalieroRawValue.svolto),
      dataAllenamento: new FormControl(allenamentoGiornalieroRawValue.dataAllenamento),
      schedaSettimanale: new FormControl(allenamentoGiornalieroRawValue.schedaSettimanale, {
        validators: [Validators.required],
      }),
    });
  }

  getAllenamentoGiornaliero(form: AllenamentoGiornalieroFormGroup): IAllenamentoGiornaliero | NewAllenamentoGiornaliero {
    return form.getRawValue() as IAllenamentoGiornaliero | NewAllenamentoGiornaliero;
  }

  resetForm(form: AllenamentoGiornalieroFormGroup, allenamentoGiornaliero: AllenamentoGiornalieroFormGroupInput): void {
    const allenamentoGiornalieroRawValue = { ...this.getFormDefaults(), ...allenamentoGiornaliero };
    form.reset(
      {
        ...allenamentoGiornalieroRawValue,
        id: { value: allenamentoGiornalieroRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AllenamentoGiornalieroFormDefaults {
    return {
      id: null,
      svolto: false,
    };
  }
}
