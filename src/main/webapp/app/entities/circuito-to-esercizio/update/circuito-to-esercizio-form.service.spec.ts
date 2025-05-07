import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../circuito-to-esercizio.test-samples';

import { CircuitoToEsercizioFormService } from './circuito-to-esercizio-form.service';

describe('CircuitoToEsercizio Form Service', () => {
  let service: CircuitoToEsercizioFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CircuitoToEsercizioFormService);
  });

  describe('Service methods', () => {
    describe('createCircuitoToEsercizioFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCircuitoToEsercizioFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            reps: expect.any(Object),
            esercizio: expect.any(Object),
            circuito: expect.any(Object),
          }),
        );
      });

      it('passing ICircuitoToEsercizio should create a new form with FormGroup', () => {
        const formGroup = service.createCircuitoToEsercizioFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            reps: expect.any(Object),
            esercizio: expect.any(Object),
            circuito: expect.any(Object),
          }),
        );
      });
    });

    describe('getCircuitoToEsercizio', () => {
      it('should return NewCircuitoToEsercizio for default CircuitoToEsercizio initial value', () => {
        const formGroup = service.createCircuitoToEsercizioFormGroup(sampleWithNewData);

        const circuitoToEsercizio = service.getCircuitoToEsercizio(formGroup) as any;

        expect(circuitoToEsercizio).toMatchObject(sampleWithNewData);
      });

      it('should return NewCircuitoToEsercizio for empty CircuitoToEsercizio initial value', () => {
        const formGroup = service.createCircuitoToEsercizioFormGroup();

        const circuitoToEsercizio = service.getCircuitoToEsercizio(formGroup) as any;

        expect(circuitoToEsercizio).toMatchObject({});
      });

      it('should return ICircuitoToEsercizio', () => {
        const formGroup = service.createCircuitoToEsercizioFormGroup(sampleWithRequiredData);

        const circuitoToEsercizio = service.getCircuitoToEsercizio(formGroup) as any;

        expect(circuitoToEsercizio).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICircuitoToEsercizio should not enable id FormControl', () => {
        const formGroup = service.createCircuitoToEsercizioFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCircuitoToEsercizio should disable id FormControl', () => {
        const formGroup = service.createCircuitoToEsercizioFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
