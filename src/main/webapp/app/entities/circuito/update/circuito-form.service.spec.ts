import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../circuito.test-samples';

import { CircuitoFormService } from './circuito-form.service';

describe('Circuito Form Service', () => {
  let service: CircuitoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CircuitoFormService);
  });

  describe('Service methods', () => {
    describe('createCircuitoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCircuitoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            tempoLimite: expect.any(Object),
            tempoImpiegato: expect.any(Object),
            catenaRipetizioni: expect.any(Object),
            circuitiCompletati: expect.any(Object),
            svolto: expect.any(Object),
            completato: expect.any(Object),
            feedback: expect.any(Object),
            allenamentoGiornaliero: expect.any(Object),
          }),
        );
      });

      it('passing ICircuito should create a new form with FormGroup', () => {
        const formGroup = service.createCircuitoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            tempoLimite: expect.any(Object),
            tempoImpiegato: expect.any(Object),
            catenaRipetizioni: expect.any(Object),
            circuitiCompletati: expect.any(Object),
            svolto: expect.any(Object),
            completato: expect.any(Object),
            feedback: expect.any(Object),
            allenamentoGiornaliero: expect.any(Object),
          }),
        );
      });
    });

    describe('getCircuito', () => {
      it('should return NewCircuito for default Circuito initial value', () => {
        const formGroup = service.createCircuitoFormGroup(sampleWithNewData);

        const circuito = service.getCircuito(formGroup) as any;

        expect(circuito).toMatchObject(sampleWithNewData);
      });

      it('should return NewCircuito for empty Circuito initial value', () => {
        const formGroup = service.createCircuitoFormGroup();

        const circuito = service.getCircuito(formGroup) as any;

        expect(circuito).toMatchObject({});
      });

      it('should return ICircuito', () => {
        const formGroup = service.createCircuitoFormGroup(sampleWithRequiredData);

        const circuito = service.getCircuito(formGroup) as any;

        expect(circuito).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICircuito should not enable id FormControl', () => {
        const formGroup = service.createCircuitoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCircuito should disable id FormControl', () => {
        const formGroup = service.createCircuitoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
