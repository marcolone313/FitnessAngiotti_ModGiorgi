import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPesoCliente, NewPesoCliente } from '../peso-cliente.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPesoCliente for edit and NewPesoClienteFormGroupInput for create.
 */
type PesoClienteFormGroupInput = IPesoCliente | PartialWithRequiredKeyOf<NewPesoCliente>;

type PesoClienteFormDefaults = Pick<NewPesoCliente, 'id'>;

type PesoClienteFormGroupContent = {
  id: FormControl<IPesoCliente['id'] | NewPesoCliente['id']>;
  peso: FormControl<IPesoCliente['peso']>;
  mese: FormControl<IPesoCliente['mese']>;
  dataInserimento: FormControl<IPesoCliente['dataInserimento']>;
  cliente: FormControl<IPesoCliente['cliente']>;
};

export type PesoClienteFormGroup = FormGroup<PesoClienteFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PesoClienteFormService {
  createPesoClienteFormGroup(pesoCliente: PesoClienteFormGroupInput = { id: null }): PesoClienteFormGroup {
    const pesoClienteRawValue = {
      ...this.getFormDefaults(),
      ...pesoCliente,
    };
    return new FormGroup<PesoClienteFormGroupContent>({
      id: new FormControl(
        { value: pesoClienteRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      peso: new FormControl(pesoClienteRawValue.peso, {
        validators: [Validators.required, Validators.min(0)],
      }),
      mese: new FormControl(pesoClienteRawValue.mese, {
        validators: [Validators.required, Validators.min(1), Validators.max(12)],
      }),
      dataInserimento: new FormControl(pesoClienteRawValue.dataInserimento, {
        validators: [Validators.required],
      }),
      cliente: new FormControl(pesoClienteRawValue.cliente, {
        validators: [Validators.required],
      }),
    });
  }

  getPesoCliente(form: PesoClienteFormGroup): IPesoCliente | NewPesoCliente {
    return form.getRawValue() as IPesoCliente | NewPesoCliente;
  }

  resetForm(form: PesoClienteFormGroup, pesoCliente: PesoClienteFormGroupInput): void {
    const pesoClienteRawValue = { ...this.getFormDefaults(), ...pesoCliente };
    form.reset(
      {
        ...pesoClienteRawValue,
        id: { value: pesoClienteRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PesoClienteFormDefaults {
    return {
      id: null,
    };
  }
}
