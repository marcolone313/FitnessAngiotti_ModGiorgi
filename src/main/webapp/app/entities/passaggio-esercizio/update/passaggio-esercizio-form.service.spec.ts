import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../passaggio-esercizio.test-samples';

import { PassaggioEsercizioFormService } from './passaggio-esercizio-form.service';

describe('PassaggioEsercizio Form Service', () => {
  let service: PassaggioEsercizioFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PassaggioEsercizioFormService);
  });

  describe('Service methods', () => {
    describe('createPassaggioEsercizioFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPassaggioEsercizioFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            indice: expect.any(Object),
            descrizione: expect.any(Object),
            esercizio: expect.any(Object),
          }),
        );
      });

      it('passing IPassaggioEsercizio should create a new form with FormGroup', () => {
        const formGroup = service.createPassaggioEsercizioFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            indice: expect.any(Object),
            descrizione: expect.any(Object),
            esercizio: expect.any(Object),
          }),
        );
      });
    });

    describe('getPassaggioEsercizio', () => {
      it('should return NewPassaggioEsercizio for default PassaggioEsercizio initial value', () => {
        const formGroup = service.createPassaggioEsercizioFormGroup(sampleWithNewData);

        const passaggioEsercizio = service.getPassaggioEsercizio(formGroup) as any;

        expect(passaggioEsercizio).toMatchObject(sampleWithNewData);
      });

      it('should return NewPassaggioEsercizio for empty PassaggioEsercizio initial value', () => {
        const formGroup = service.createPassaggioEsercizioFormGroup();

        const passaggioEsercizio = service.getPassaggioEsercizio(formGroup) as any;

        expect(passaggioEsercizio).toMatchObject({});
      });

      it('should return IPassaggioEsercizio', () => {
        const formGroup = service.createPassaggioEsercizioFormGroup(sampleWithRequiredData);

        const passaggioEsercizio = service.getPassaggioEsercizio(formGroup) as any;

        expect(passaggioEsercizio).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPassaggioEsercizio should not enable id FormControl', () => {
        const formGroup = service.createPassaggioEsercizioFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPassaggioEsercizio should disable id FormControl', () => {
        const formGroup = service.createPassaggioEsercizioFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
