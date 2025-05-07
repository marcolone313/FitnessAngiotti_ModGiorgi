import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ILezioneCorso, NewLezioneCorso } from '../lezione-corso.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILezioneCorso for edit and NewLezioneCorsoFormGroupInput for create.
 */
type LezioneCorsoFormGroupInput = ILezioneCorso | PartialWithRequiredKeyOf<NewLezioneCorso>;

type LezioneCorsoFormDefaults = Pick<NewLezioneCorso, 'id'>;

type LezioneCorsoFormGroupContent = {
  id: FormControl<ILezioneCorso['id'] | NewLezioneCorso['id']>;
  titolo: FormControl<ILezioneCorso['titolo']>;
  descrizione: FormControl<ILezioneCorso['descrizione']>;
  puntiFocali: FormControl<ILezioneCorso['puntiFocali']>;
  videoUrl: FormControl<ILezioneCorso['videoUrl']>;
  corsoAcademy: FormControl<ILezioneCorso['corsoAcademy']>;
};

export type LezioneCorsoFormGroup = FormGroup<LezioneCorsoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class LezioneCorsoFormService {
  createLezioneCorsoFormGroup(lezioneCorso: LezioneCorsoFormGroupInput = { id: null }): LezioneCorsoFormGroup {
    const lezioneCorsoRawValue = {
      ...this.getFormDefaults(),
      ...lezioneCorso,
    };
    return new FormGroup<LezioneCorsoFormGroupContent>({
      id: new FormControl(
        { value: lezioneCorsoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      titolo: new FormControl(lezioneCorsoRawValue.titolo, {
        validators: [Validators.required],
      }),
      descrizione: new FormControl(lezioneCorsoRawValue.descrizione),
      puntiFocali: new FormControl(lezioneCorsoRawValue.puntiFocali),
      videoUrl: new FormControl(lezioneCorsoRawValue.videoUrl),
      corsoAcademy: new FormControl(lezioneCorsoRawValue.corsoAcademy, {
        validators: [Validators.required],
      }),
    });
  }

  getLezioneCorso(form: LezioneCorsoFormGroup): ILezioneCorso | NewLezioneCorso {
    return form.getRawValue() as ILezioneCorso | NewLezioneCorso;
  }

  resetForm(form: LezioneCorsoFormGroup, lezioneCorso: LezioneCorsoFormGroupInput): void {
    const lezioneCorsoRawValue = { ...this.getFormDefaults(), ...lezioneCorso };
    form.reset(
      {
        ...lezioneCorsoRawValue,
        id: { value: lezioneCorsoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): LezioneCorsoFormDefaults {
    return {
      id: null,
    };
  }
}
