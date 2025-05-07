import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICircuito, NewCircuito } from '../circuito.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICircuito for edit and NewCircuitoFormGroupInput for create.
 */
type CircuitoFormGroupInput = ICircuito | PartialWithRequiredKeyOf<NewCircuito>;

type CircuitoFormDefaults = Pick<NewCircuito, 'id' | 'svolto' | 'completato'>;

type CircuitoFormGroupContent = {
  id: FormControl<ICircuito['id'] | NewCircuito['id']>;
  tempoLimite: FormControl<ICircuito['tempoLimite']>;
  tempoImpiegato: FormControl<ICircuito['tempoImpiegato']>;
  catenaRipetizioni: FormControl<ICircuito['catenaRipetizioni']>;
  circuitiCompletati: FormControl<ICircuito['circuitiCompletati']>;
  svolto: FormControl<ICircuito['svolto']>;
  completato: FormControl<ICircuito['completato']>;
  feedback: FormControl<ICircuito['feedback']>;
  allenamentoGiornaliero: FormControl<ICircuito['allenamentoGiornaliero']>;
};

export type CircuitoFormGroup = FormGroup<CircuitoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CircuitoFormService {
  createCircuitoFormGroup(circuito: CircuitoFormGroupInput = { id: null }): CircuitoFormGroup {
    const circuitoRawValue = {
      ...this.getFormDefaults(),
      ...circuito,
    };
    return new FormGroup<CircuitoFormGroupContent>({
      id: new FormControl(
        { value: circuitoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      tempoLimite: new FormControl(circuitoRawValue.tempoLimite),
      tempoImpiegato: new FormControl(circuitoRawValue.tempoImpiegato),
      catenaRipetizioni: new FormControl(circuitoRawValue.catenaRipetizioni),
      circuitiCompletati: new FormControl(circuitoRawValue.circuitiCompletati),
      svolto: new FormControl(circuitoRawValue.svolto),
      completato: new FormControl(circuitoRawValue.completato),
      feedback: new FormControl(circuitoRawValue.feedback),
      allenamentoGiornaliero: new FormControl(circuitoRawValue.allenamentoGiornaliero, {
        validators: [Validators.required],
      }),
    });
  }

  getCircuito(form: CircuitoFormGroup): ICircuito | NewCircuito {
    return form.getRawValue() as ICircuito | NewCircuito;
  }

  resetForm(form: CircuitoFormGroup, circuito: CircuitoFormGroupInput): void {
    const circuitoRawValue = { ...this.getFormDefaults(), ...circuito };
    form.reset(
      {
        ...circuitoRawValue,
        id: { value: circuitoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CircuitoFormDefaults {
    return {
      id: null,
      svolto: false,
      completato: false,
    };
  }
}
