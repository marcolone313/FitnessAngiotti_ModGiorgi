import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../corsa.test-samples';

import { CorsaFormService } from './corsa-form.service';

describe('Corsa Form Service', () => {
  let service: CorsaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CorsaFormService);
  });

  describe('Service methods', () => {
    describe('createCorsaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCorsaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            distanzaDaPercorrere: expect.any(Object),
            tempoImpiegato: expect.any(Object),
            svolto: expect.any(Object),
            completato: expect.any(Object),
            feedback: expect.any(Object),
            allenamentoGiornaliero: expect.any(Object),
          }),
        );
      });

      it('passing ICorsa should create a new form with FormGroup', () => {
        const formGroup = service.createCorsaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            distanzaDaPercorrere: expect.any(Object),
            tempoImpiegato: expect.any(Object),
            svolto: expect.any(Object),
            completato: expect.any(Object),
            feedback: expect.any(Object),
            allenamentoGiornaliero: expect.any(Object),
          }),
        );
      });
    });

    describe('getCorsa', () => {
      it('should return NewCorsa for default Corsa initial value', () => {
        const formGroup = service.createCorsaFormGroup(sampleWithNewData);

        const corsa = service.getCorsa(formGroup) as any;

        expect(corsa).toMatchObject(sampleWithNewData);
      });

      it('should return NewCorsa for empty Corsa initial value', () => {
        const formGroup = service.createCorsaFormGroup();

        const corsa = service.getCorsa(formGroup) as any;

        expect(corsa).toMatchObject({});
      });

      it('should return ICorsa', () => {
        const formGroup = service.createCorsaFormGroup(sampleWithRequiredData);

        const corsa = service.getCorsa(formGroup) as any;

        expect(corsa).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICorsa should not enable id FormControl', () => {
        const formGroup = service.createCorsaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCorsa should disable id FormControl', () => {
        const formGroup = service.createCorsaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
