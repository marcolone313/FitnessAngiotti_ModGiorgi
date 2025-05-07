import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICorsa, NewCorsa } from '../corsa.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICorsa for edit and NewCorsaFormGroupInput for create.
 */
type CorsaFormGroupInput = ICorsa | PartialWithRequiredKeyOf<NewCorsa>;

type CorsaFormDefaults = Pick<NewCorsa, 'id' | 'svolto' | 'completato'>;

type CorsaFormGroupContent = {
  id: FormControl<ICorsa['id'] | NewCorsa['id']>;
  distanzaDaPercorrere: FormControl<ICorsa['distanzaDaPercorrere']>;
  tempoImpiegato: FormControl<ICorsa['tempoImpiegato']>;
  svolto: FormControl<ICorsa['svolto']>;
  completato: FormControl<ICorsa['completato']>;
  feedback: FormControl<ICorsa['feedback']>;
  allenamentoGiornaliero: FormControl<ICorsa['allenamentoGiornaliero']>;
};

export type CorsaFormGroup = FormGroup<CorsaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CorsaFormService {
  createCorsaFormGroup(corsa: CorsaFormGroupInput = { id: null }): CorsaFormGroup {
    const corsaRawValue = {
      ...this.getFormDefaults(),
      ...corsa,
    };
    return new FormGroup<CorsaFormGroupContent>({
      id: new FormControl(
        { value: corsaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      distanzaDaPercorrere: new FormControl(corsaRawValue.distanzaDaPercorrere, {
        validators: [Validators.required],
      }),
      tempoImpiegato: new FormControl(corsaRawValue.tempoImpiegato),
      svolto: new FormControl(corsaRawValue.svolto),
      completato: new FormControl(corsaRawValue.completato),
      feedback: new FormControl(corsaRawValue.feedback),
      allenamentoGiornaliero: new FormControl(corsaRawValue.allenamentoGiornaliero, {
        validators: [Validators.required],
      }),
    });
  }

  getCorsa(form: CorsaFormGroup): ICorsa | NewCorsa {
    return form.getRawValue() as ICorsa | NewCorsa;
  }

  resetForm(form: CorsaFormGroup, corsa: CorsaFormGroupInput): void {
    const corsaRawValue = { ...this.getFormDefaults(), ...corsa };
    form.reset(
      {
        ...corsaRawValue,
        id: { value: corsaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CorsaFormDefaults {
    return {
      id: null,
      svolto: false,
      completato: false,
    };
  }
}
