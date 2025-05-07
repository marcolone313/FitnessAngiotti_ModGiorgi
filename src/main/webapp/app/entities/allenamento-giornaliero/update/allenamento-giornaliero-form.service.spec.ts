import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../allenamento-giornaliero.test-samples';

import { AllenamentoGiornalieroFormService } from './allenamento-giornaliero-form.service';

describe('AllenamentoGiornaliero Form Service', () => {
  let service: AllenamentoGiornalieroFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AllenamentoGiornalieroFormService);
  });

  describe('Service methods', () => {
    describe('createAllenamentoGiornalieroFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAllenamentoGiornalieroFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            tipo: expect.any(Object),
            giorno: expect.any(Object),
            notaTrainer: expect.any(Object),
            svolto: expect.any(Object),
            dataAllenamento: expect.any(Object),
            schedaSettimanale: expect.any(Object),
          }),
        );
      });

      it('passing IAllenamentoGiornaliero should create a new form with FormGroup', () => {
        const formGroup = service.createAllenamentoGiornalieroFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            tipo: expect.any(Object),
            giorno: expect.any(Object),
            notaTrainer: expect.any(Object),
            svolto: expect.any(Object),
            dataAllenamento: expect.any(Object),
            schedaSettimanale: expect.any(Object),
          }),
        );
      });
    });

    describe('getAllenamentoGiornaliero', () => {
      it('should return NewAllenamentoGiornaliero for default AllenamentoGiornaliero initial value', () => {
        const formGroup = service.createAllenamentoGiornalieroFormGroup(sampleWithNewData);

        const allenamentoGiornaliero = service.getAllenamentoGiornaliero(formGroup) as any;

        expect(allenamentoGiornaliero).toMatchObject(sampleWithNewData);
      });

      it('should return NewAllenamentoGiornaliero for empty AllenamentoGiornaliero initial value', () => {
        const formGroup = service.createAllenamentoGiornalieroFormGroup();

        const allenamentoGiornaliero = service.getAllenamentoGiornaliero(formGroup) as any;

        expect(allenamentoGiornaliero).toMatchObject({});
      });

      it('should return IAllenamentoGiornaliero', () => {
        const formGroup = service.createAllenamentoGiornalieroFormGroup(sampleWithRequiredData);

        const allenamentoGiornaliero = service.getAllenamentoGiornaliero(formGroup) as any;

        expect(allenamentoGiornaliero).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAllenamentoGiornaliero should not enable id FormControl', () => {
        const formGroup = service.createAllenamentoGiornalieroFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAllenamentoGiornaliero should disable id FormControl', () => {
        const formGroup = service.createAllenamentoGiornalieroFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
