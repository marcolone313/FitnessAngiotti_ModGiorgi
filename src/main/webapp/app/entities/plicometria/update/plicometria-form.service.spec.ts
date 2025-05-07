import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../plicometria.test-samples';

import { PlicometriaFormService } from './plicometria-form.service';

describe('Plicometria Form Service', () => {
  let service: PlicometriaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PlicometriaFormService);
  });

  describe('Service methods', () => {
    describe('createPlicometriaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPlicometriaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            tricipite: expect.any(Object),
            petto: expect.any(Object),
            ascella: expect.any(Object),
            sottoscapolare: expect.any(Object),
            soprailliaca: expect.any(Object),
            addome: expect.any(Object),
            coscia: expect.any(Object),
            mese: expect.any(Object),
            dataInserimento: expect.any(Object),
            cliente: expect.any(Object),
          }),
        );
      });

      it('passing IPlicometria should create a new form with FormGroup', () => {
        const formGroup = service.createPlicometriaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            tricipite: expect.any(Object),
            petto: expect.any(Object),
            ascella: expect.any(Object),
            sottoscapolare: expect.any(Object),
            soprailliaca: expect.any(Object),
            addome: expect.any(Object),
            coscia: expect.any(Object),
            mese: expect.any(Object),
            dataInserimento: expect.any(Object),
            cliente: expect.any(Object),
          }),
        );
      });
    });

    describe('getPlicometria', () => {
      it('should return NewPlicometria for default Plicometria initial value', () => {
        const formGroup = service.createPlicometriaFormGroup(sampleWithNewData);

        const plicometria = service.getPlicometria(formGroup) as any;

        expect(plicometria).toMatchObject(sampleWithNewData);
      });

      it('should return NewPlicometria for empty Plicometria initial value', () => {
        const formGroup = service.createPlicometriaFormGroup();

        const plicometria = service.getPlicometria(formGroup) as any;

        expect(plicometria).toMatchObject({});
      });

      it('should return IPlicometria', () => {
        const formGroup = service.createPlicometriaFormGroup(sampleWithRequiredData);

        const plicometria = service.getPlicometria(formGroup) as any;

        expect(plicometria).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPlicometria should not enable id FormControl', () => {
        const formGroup = service.createPlicometriaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPlicometria should disable id FormControl', () => {
        const formGroup = service.createPlicometriaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
