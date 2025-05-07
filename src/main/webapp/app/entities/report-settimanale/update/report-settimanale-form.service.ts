import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IReportSettimanale, NewReportSettimanale } from '../report-settimanale.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IReportSettimanale for edit and NewReportSettimanaleFormGroupInput for create.
 */
type ReportSettimanaleFormGroupInput = IReportSettimanale | PartialWithRequiredKeyOf<NewReportSettimanale>;

type ReportSettimanaleFormDefaults = Pick<NewReportSettimanale, 'id' | 'puntuale'>;

type ReportSettimanaleFormGroupContent = {
  id: FormControl<IReportSettimanale['id'] | NewReportSettimanale['id']>;
  voto: FormControl<IReportSettimanale['voto']>;
  commentoAllenamento: FormControl<IReportSettimanale['commentoAllenamento']>;
  giorniDieta: FormControl<IReportSettimanale['giorniDieta']>;
  pesoMedio: FormControl<IReportSettimanale['pesoMedio']>;
  qualitaSonno: FormControl<IReportSettimanale['qualitaSonno']>;
  mediaOreSonno: FormControl<IReportSettimanale['mediaOreSonno']>;
  dataCreazione: FormControl<IReportSettimanale['dataCreazione']>;
  dataScadenza: FormControl<IReportSettimanale['dataScadenza']>;
  dataCompletamento: FormControl<IReportSettimanale['dataCompletamento']>;
  puntuale: FormControl<IReportSettimanale['puntuale']>;
  analisiReport: FormControl<IReportSettimanale['analisiReport']>;
  schedaSettimanale: FormControl<IReportSettimanale['schedaSettimanale']>;
  cliente: FormControl<IReportSettimanale['cliente']>;
};

export type ReportSettimanaleFormGroup = FormGroup<ReportSettimanaleFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ReportSettimanaleFormService {
  createReportSettimanaleFormGroup(reportSettimanale: ReportSettimanaleFormGroupInput = { id: null }): ReportSettimanaleFormGroup {
    const reportSettimanaleRawValue = {
      ...this.getFormDefaults(),
      ...reportSettimanale,
    };
    return new FormGroup<ReportSettimanaleFormGroupContent>({
      id: new FormControl(
        { value: reportSettimanaleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      voto: new FormControl(reportSettimanaleRawValue.voto),
      commentoAllenamento: new FormControl(reportSettimanaleRawValue.commentoAllenamento),
      giorniDieta: new FormControl(reportSettimanaleRawValue.giorniDieta),
      pesoMedio: new FormControl(reportSettimanaleRawValue.pesoMedio, {
        validators: [Validators.min(0)],
      }),
      qualitaSonno: new FormControl(reportSettimanaleRawValue.qualitaSonno),
      mediaOreSonno: new FormControl(reportSettimanaleRawValue.mediaOreSonno),
      dataCreazione: new FormControl(reportSettimanaleRawValue.dataCreazione, {
        validators: [Validators.required],
      }),
      dataScadenza: new FormControl(reportSettimanaleRawValue.dataScadenza, {
        validators: [Validators.required],
      }),
      dataCompletamento: new FormControl(reportSettimanaleRawValue.dataCompletamento),
      puntuale: new FormControl(reportSettimanaleRawValue.puntuale),
      analisiReport: new FormControl(reportSettimanaleRawValue.analisiReport),
      schedaSettimanale: new FormControl(reportSettimanaleRawValue.schedaSettimanale, {
        validators: [Validators.required],
      }),
      cliente: new FormControl(reportSettimanaleRawValue.cliente, {
        validators: [Validators.required],
      }),
    });
  }

  getReportSettimanale(form: ReportSettimanaleFormGroup): IReportSettimanale | NewReportSettimanale {
    return form.getRawValue() as IReportSettimanale | NewReportSettimanale;
  }

  resetForm(form: ReportSettimanaleFormGroup, reportSettimanale: ReportSettimanaleFormGroupInput): void {
    const reportSettimanaleRawValue = { ...this.getFormDefaults(), ...reportSettimanale };
    form.reset(
      {
        ...reportSettimanaleRawValue,
        id: { value: reportSettimanaleRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ReportSettimanaleFormDefaults {
    return {
      id: null,
      puntuale: false,
    };
  }
}
