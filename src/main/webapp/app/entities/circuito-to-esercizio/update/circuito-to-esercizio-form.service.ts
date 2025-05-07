import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICircuitoToEsercizio, NewCircuitoToEsercizio } from '../circuito-to-esercizio.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICircuitoToEsercizio for edit and NewCircuitoToEsercizioFormGroupInput for create.
 */
type CircuitoToEsercizioFormGroupInput = ICircuitoToEsercizio | PartialWithRequiredKeyOf<NewCircuitoToEsercizio>;

type CircuitoToEsercizioFormDefaults = Pick<NewCircuitoToEsercizio, 'id'>;

type CircuitoToEsercizioFormGroupContent = {
  id: FormControl<ICircuitoToEsercizio['id'] | NewCircuitoToEsercizio['id']>;
  reps: FormControl<ICircuitoToEsercizio['reps']>;
  esercizio: FormControl<ICircuitoToEsercizio['esercizio']>;
  circuito: FormControl<ICircuitoToEsercizio['circuito']>;
};

export type CircuitoToEsercizioFormGroup = FormGroup<CircuitoToEsercizioFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CircuitoToEsercizioFormService {
  createCircuitoToEsercizioFormGroup(circuitoToEsercizio: CircuitoToEsercizioFormGroupInput = { id: null }): CircuitoToEsercizioFormGroup {
    const circuitoToEsercizioRawValue = {
      ...this.getFormDefaults(),
      ...circuitoToEsercizio,
    };
    return new FormGroup<CircuitoToEsercizioFormGroupContent>({
      id: new FormControl(
        { value: circuitoToEsercizioRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      reps: new FormControl(circuitoToEsercizioRawValue.reps, {
        validators: [Validators.required],
      }),
      esercizio: new FormControl(circuitoToEsercizioRawValue.esercizio, {
        validators: [Validators.required],
      }),
      circuito: new FormControl(circuitoToEsercizioRawValue.circuito, {
        validators: [Validators.required],
      }),
    });
  }

  getCircuitoToEsercizio(form: CircuitoToEsercizioFormGroup): ICircuitoToEsercizio | NewCircuitoToEsercizio {
    return form.getRawValue() as ICircuitoToEsercizio | NewCircuitoToEsercizio;
  }

  resetForm(form: CircuitoToEsercizioFormGroup, circuitoToEsercizio: CircuitoToEsercizioFormGroupInput): void {
    const circuitoToEsercizioRawValue = { ...this.getFormDefaults(), ...circuitoToEsercizio };
    form.reset(
      {
        ...circuitoToEsercizioRawValue,
        id: { value: circuitoToEsercizioRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CircuitoToEsercizioFormDefaults {
    return {
      id: null,
    };
  }
}
