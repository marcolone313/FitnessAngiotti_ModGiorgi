import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../corso-academy.test-samples';

import { CorsoAcademyFormService } from './corso-academy-form.service';

describe('CorsoAcademy Form Service', () => {
  let service: CorsoAcademyFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CorsoAcademyFormService);
  });

  describe('Service methods', () => {
    describe('createCorsoAcademyFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCorsoAcademyFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            livello: expect.any(Object),
            titolo: expect.any(Object),
          }),
        );
      });

      it('passing ICorsoAcademy should create a new form with FormGroup', () => {
        const formGroup = service.createCorsoAcademyFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            livello: expect.any(Object),
            titolo: expect.any(Object),
          }),
        );
      });
    });

    describe('getCorsoAcademy', () => {
      it('should return NewCorsoAcademy for default CorsoAcademy initial value', () => {
        const formGroup = service.createCorsoAcademyFormGroup(sampleWithNewData);

        const corsoAcademy = service.getCorsoAcademy(formGroup) as any;

        expect(corsoAcademy).toMatchObject(sampleWithNewData);
      });

      it('should return NewCorsoAcademy for empty CorsoAcademy initial value', () => {
        const formGroup = service.createCorsoAcademyFormGroup();

        const corsoAcademy = service.getCorsoAcademy(formGroup) as any;

        expect(corsoAcademy).toMatchObject({});
      });

      it('should return ICorsoAcademy', () => {
        const formGroup = service.createCorsoAcademyFormGroup(sampleWithRequiredData);

        const corsoAcademy = service.getCorsoAcademy(formGroup) as any;

        expect(corsoAcademy).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICorsoAcademy should not enable id FormControl', () => {
        const formGroup = service.createCorsoAcademyFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCorsoAcademy should disable id FormControl', () => {
        const formGroup = service.createCorsoAcademyFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
