import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../report-settimanale.test-samples';

import { ReportSettimanaleFormService } from './report-settimanale-form.service';

describe('ReportSettimanale Form Service', () => {
  let service: ReportSettimanaleFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReportSettimanaleFormService);
  });

  describe('Service methods', () => {
    describe('createReportSettimanaleFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createReportSettimanaleFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            voto: expect.any(Object),
            commentoAllenamento: expect.any(Object),
            giorniDieta: expect.any(Object),
            pesoMedio: expect.any(Object),
            qualitaSonno: expect.any(Object),
            mediaOreSonno: expect.any(Object),
            dataCreazione: expect.any(Object),
            dataScadenza: expect.any(Object),
            dataCompletamento: expect.any(Object),
            puntuale: expect.any(Object),
            analisiReport: expect.any(Object),
            schedaSettimanale: expect.any(Object),
            cliente: expect.any(Object),
          }),
        );
      });

      it('passing IReportSettimanale should create a new form with FormGroup', () => {
        const formGroup = service.createReportSettimanaleFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            voto: expect.any(Object),
            commentoAllenamento: expect.any(Object),
            giorniDieta: expect.any(Object),
            pesoMedio: expect.any(Object),
            qualitaSonno: expect.any(Object),
            mediaOreSonno: expect.any(Object),
            dataCreazione: expect.any(Object),
            dataScadenza: expect.any(Object),
            dataCompletamento: expect.any(Object),
            puntuale: expect.any(Object),
            analisiReport: expect.any(Object),
            schedaSettimanale: expect.any(Object),
            cliente: expect.any(Object),
          }),
        );
      });
    });

    describe('getReportSettimanale', () => {
      it('should return NewReportSettimanale for default ReportSettimanale initial value', () => {
        const formGroup = service.createReportSettimanaleFormGroup(sampleWithNewData);

        const reportSettimanale = service.getReportSettimanale(formGroup) as any;

        expect(reportSettimanale).toMatchObject(sampleWithNewData);
      });

      it('should return NewReportSettimanale for empty ReportSettimanale initial value', () => {
        const formGroup = service.createReportSettimanaleFormGroup();

        const reportSettimanale = service.getReportSettimanale(formGroup) as any;

        expect(reportSettimanale).toMatchObject({});
      });

      it('should return IReportSettimanale', () => {
        const formGroup = service.createReportSettimanaleFormGroup(sampleWithRequiredData);

        const reportSettimanale = service.getReportSettimanale(formGroup) as any;

        expect(reportSettimanale).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IReportSettimanale should not enable id FormControl', () => {
        const formGroup = service.createReportSettimanaleFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewReportSettimanale should disable id FormControl', () => {
        const formGroup = service.createReportSettimanaleFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
