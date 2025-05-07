import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICliente, NewCliente } from '../cliente.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICliente for edit and NewClienteFormGroupInput for create.
 */
type ClienteFormGroupInput = ICliente | PartialWithRequiredKeyOf<NewCliente>;

type ClienteFormDefaults = Pick<NewCliente, 'id'>;

type ClienteFormGroupContent = {
  id: FormControl<ICliente['id'] | NewCliente['id']>;
  nome: FormControl<ICliente['nome']>;
  cognome: FormControl<ICliente['cognome']>;
  dataDiNascita: FormControl<ICliente['dataDiNascita']>;
  codiceFiscale: FormControl<ICliente['codiceFiscale']>;
  codiceCliente: FormControl<ICliente['codiceCliente']>;
  email: FormControl<ICliente['email']>;
  telefono: FormControl<ICliente['telefono']>;
  foto: FormControl<ICliente['foto']>;
  fotoContentType: FormControl<ICliente['fotoContentType']>;
  user: FormControl<ICliente['user']>;
};

export type ClienteFormGroup = FormGroup<ClienteFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ClienteFormService {
  createClienteFormGroup(cliente: ClienteFormGroupInput = { id: null }): ClienteFormGroup {
    const clienteRawValue = {
      ...this.getFormDefaults(),
      ...cliente,
    };
    return new FormGroup<ClienteFormGroupContent>({
      id: new FormControl(
        { value: clienteRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nome: new FormControl(clienteRawValue.nome, {
        validators: [Validators.required],
      }),
      cognome: new FormControl(clienteRawValue.cognome, {
        validators: [Validators.required],
      }),
      dataDiNascita: new FormControl(clienteRawValue.dataDiNascita, {
        validators: [Validators.required],
      }),
      codiceFiscale: new FormControl(clienteRawValue.codiceFiscale, {
        validators: [Validators.required, Validators.minLength(16), Validators.maxLength(16)],
      }),
      codiceCliente: new FormControl(clienteRawValue.codiceCliente, {
        validators: [Validators.required],
      }),
      email: new FormControl(clienteRawValue.email, {
        validators: [Validators.required],
      }),
      telefono: new FormControl(clienteRawValue.telefono),
      foto: new FormControl(clienteRawValue.foto),
      fotoContentType: new FormControl(clienteRawValue.fotoContentType),
      user: new FormControl(clienteRawValue.user, {
        validators: [Validators.required],
      }),
    });
  }

  getCliente(form: ClienteFormGroup): ICliente | NewCliente {
    return form.getRawValue() as ICliente | NewCliente;
  }

  resetForm(form: ClienteFormGroup, cliente: ClienteFormGroupInput): void {
    const clienteRawValue = { ...this.getFormDefaults(), ...cliente };
    form.reset(
      {
        ...clienteRawValue,
        id: { value: clienteRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ClienteFormDefaults {
    return {
      id: null,
    };
  }
}
