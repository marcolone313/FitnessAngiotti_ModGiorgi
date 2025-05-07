import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICirconferenza, NewCirconferenza } from '../circonferenza.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICirconferenza for edit and NewCirconferenzaFormGroupInput for create.
 */
type CirconferenzaFormGroupInput = ICirconferenza | PartialWithRequiredKeyOf<NewCirconferenza>;

type CirconferenzaFormDefaults = Pick<NewCirconferenza, 'id'>;

type CirconferenzaFormGroupContent = {
  id: FormControl<ICirconferenza['id'] | NewCirconferenza['id']>;
  torace: FormControl<ICirconferenza['torace']>;
  braccio: FormControl<ICirconferenza['braccio']>;
  avambraccio: FormControl<ICirconferenza['avambraccio']>;
  ombelico: FormControl<ICirconferenza['ombelico']>;
  fianchi: FormControl<ICirconferenza['fianchi']>;
  sottoOmbelico: FormControl<ICirconferenza['sottoOmbelico']>;
  vita: FormControl<ICirconferenza['vita']>;
  coscia: FormControl<ICirconferenza['coscia']>;
  mese: FormControl<ICirconferenza['mese']>;
  dataInserimento: FormControl<ICirconferenza['dataInserimento']>;
  cliente: FormControl<ICirconferenza['cliente']>;
};

export type CirconferenzaFormGroup = FormGroup<CirconferenzaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CirconferenzaFormService {
  createCirconferenzaFormGroup(circonferenza: CirconferenzaFormGroupInput = { id: null }): CirconferenzaFormGroup {
    const circonferenzaRawValue = {
      ...this.getFormDefaults(),
      ...circonferenza,
    };
    return new FormGroup<CirconferenzaFormGroupContent>({
      id: new FormControl(
        { value: circonferenzaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      torace: new FormControl(circonferenzaRawValue.torace, {
        validators: [Validators.min(0)],
      }),
      braccio: new FormControl(circonferenzaRawValue.braccio, {
        validators: [Validators.min(0)],
      }),
      avambraccio: new FormControl(circonferenzaRawValue.avambraccio, {
        validators: [Validators.min(0)],
      }),
      ombelico: new FormControl(circonferenzaRawValue.ombelico, {
        validators: [Validators.min(0)],
      }),
      fianchi: new FormControl(circonferenzaRawValue.fianchi, {
        validators: [Validators.min(0)],
      }),
      sottoOmbelico: new FormControl(circonferenzaRawValue.sottoOmbelico, {
        validators: [Validators.min(0)],
      }),
      vita: new FormControl(circonferenzaRawValue.vita, {
        validators: [Validators.min(0)],
      }),
      coscia: new FormControl(circonferenzaRawValue.coscia, {
        validators: [Validators.min(0)],
      }),
      mese: new FormControl(circonferenzaRawValue.mese, {
        validators: [Validators.required, Validators.min(1), Validators.max(12)],
      }),
      dataInserimento: new FormControl(circonferenzaRawValue.dataInserimento, {
        validators: [Validators.required],
      }),
      cliente: new FormControl(circonferenzaRawValue.cliente, {
        validators: [Validators.required],
      }),
    });
  }

  getCirconferenza(form: CirconferenzaFormGroup): ICirconferenza | NewCirconferenza {
    return form.getRawValue() as ICirconferenza | NewCirconferenza;
  }

  resetForm(form: CirconferenzaFormGroup, circonferenza: CirconferenzaFormGroupInput): void {
    const circonferenzaRawValue = { ...this.getFormDefaults(), ...circonferenza };
    form.reset(
      {
        ...circonferenzaRawValue,
        id: { value: circonferenzaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CirconferenzaFormDefaults {
    return {
      id: null,
    };
  }
}
