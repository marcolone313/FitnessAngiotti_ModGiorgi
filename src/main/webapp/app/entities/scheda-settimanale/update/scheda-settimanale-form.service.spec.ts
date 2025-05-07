import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../scheda-settimanale.test-samples';

import { SchedaSettimanaleFormService } from './scheda-settimanale-form.service';

describe('SchedaSettimanale Form Service', () => {
  let service: SchedaSettimanaleFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SchedaSettimanaleFormService);
  });

  describe('Service methods', () => {
    describe('createSchedaSettimanaleFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSchedaSettimanaleFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            anno: expect.any(Object),
            mese: expect.any(Object),
            settimana: expect.any(Object),
            dataCreazione: expect.any(Object),
            cliente: expect.any(Object),
          }),
        );
      });

      it('passing ISchedaSettimanale should create a new form with FormGroup', () => {
        const formGroup = service.createSchedaSettimanaleFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            anno: expect.any(Object),
            mese: expect.any(Object),
            settimana: expect.any(Object),
            dataCreazione: expect.any(Object),
            cliente: expect.any(Object),
          }),
        );
      });
    });

    describe('getSchedaSettimanale', () => {
      it('should return NewSchedaSettimanale for default SchedaSettimanale initial value', () => {
        const formGroup = service.createSchedaSettimanaleFormGroup(sampleWithNewData);

        const schedaSettimanale = service.getSchedaSettimanale(formGroup) as any;

        expect(schedaSettimanale).toMatchObject(sampleWithNewData);
      });

      it('should return NewSchedaSettimanale for empty SchedaSettimanale initial value', () => {
        const formGroup = service.createSchedaSettimanaleFormGroup();

        const schedaSettimanale = service.getSchedaSettimanale(formGroup) as any;

        expect(schedaSettimanale).toMatchObject({});
      });

      it('should return ISchedaSettimanale', () => {
        const formGroup = service.createSchedaSettimanaleFormGroup(sampleWithRequiredData);

        const schedaSettimanale = service.getSchedaSettimanale(formGroup) as any;

        expect(schedaSettimanale).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISchedaSettimanale should not enable id FormControl', () => {
        const formGroup = service.createSchedaSettimanaleFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSchedaSettimanale should disable id FormControl', () => {
        const formGroup = service.createSchedaSettimanaleFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
