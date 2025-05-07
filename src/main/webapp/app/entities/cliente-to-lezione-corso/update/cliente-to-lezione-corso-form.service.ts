import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IClienteToLezioneCorso, NewClienteToLezioneCorso } from '../cliente-to-lezione-corso.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IClienteToLezioneCorso for edit and NewClienteToLezioneCorsoFormGroupInput for create.
 */
type ClienteToLezioneCorsoFormGroupInput = IClienteToLezioneCorso | PartialWithRequiredKeyOf<NewClienteToLezioneCorso>;

type ClienteToLezioneCorsoFormDefaults = Pick<NewClienteToLezioneCorso, 'id' | 'completato'>;

type ClienteToLezioneCorsoFormGroupContent = {
  id: FormControl<IClienteToLezioneCorso['id'] | NewClienteToLezioneCorso['id']>;
  completato: FormControl<IClienteToLezioneCorso['completato']>;
  dataCompletamento: FormControl<IClienteToLezioneCorso['dataCompletamento']>;
  cliente: FormControl<IClienteToLezioneCorso['cliente']>;
  lezioneCorso: FormControl<IClienteToLezioneCorso['lezioneCorso']>;
};

export type ClienteToLezioneCorsoFormGroup = FormGroup<ClienteToLezioneCorsoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ClienteToLezioneCorsoFormService {
  createClienteToLezioneCorsoFormGroup(
    clienteToLezioneCorso: ClienteToLezioneCorsoFormGroupInput = { id: null },
  ): ClienteToLezioneCorsoFormGroup {
    const clienteToLezioneCorsoRawValue = {
      ...this.getFormDefaults(),
      ...clienteToLezioneCorso,
    };
    return new FormGroup<ClienteToLezioneCorsoFormGroupContent>({
      id: new FormControl(
        { value: clienteToLezioneCorsoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      completato: new FormControl(clienteToLezioneCorsoRawValue.completato),
      dataCompletamento: new FormControl(clienteToLezioneCorsoRawValue.dataCompletamento),
      cliente: new FormControl(clienteToLezioneCorsoRawValue.cliente, {
        validators: [Validators.required],
      }),
      lezioneCorso: new FormControl(clienteToLezioneCorsoRawValue.lezioneCorso, {
        validators: [Validators.required],
      }),
    });
  }

  getClienteToLezioneCorso(form: ClienteToLezioneCorsoFormGroup): IClienteToLezioneCorso | NewClienteToLezioneCorso {
    return form.getRawValue() as IClienteToLezioneCorso | NewClienteToLezioneCorso;
  }

  resetForm(form: ClienteToLezioneCorsoFormGroup, clienteToLezioneCorso: ClienteToLezioneCorsoFormGroupInput): void {
    const clienteToLezioneCorsoRawValue = { ...this.getFormDefaults(), ...clienteToLezioneCorso };
    form.reset(
      {
        ...clienteToLezioneCorsoRawValue,
        id: { value: clienteToLezioneCorsoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ClienteToLezioneCorsoFormDefaults {
    return {
      id: null,
      completato: false,
    };
  }
}
