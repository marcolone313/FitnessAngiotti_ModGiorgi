import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../esercizio.test-samples';

import { EsercizioFormService } from './esercizio-form.service';

describe('Esercizio Form Service', () => {
  let service: EsercizioFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EsercizioFormService);
  });

  describe('Service methods', () => {
    describe('createEsercizioFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEsercizioFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nome: expect.any(Object),
            tipo: expect.any(Object),
            videoUrl: expect.any(Object),
            descrizione: expect.any(Object),
            foto: expect.any(Object),
          }),
        );
      });

      it('passing IEsercizio should create a new form with FormGroup', () => {
        const formGroup = service.createEsercizioFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nome: expect.any(Object),
            tipo: expect.any(Object),
            videoUrl: expect.any(Object),
            descrizione: expect.any(Object),
            foto: expect.any(Object),
          }),
        );
      });
    });

    describe('getEsercizio', () => {
      it('should return NewEsercizio for default Esercizio initial value', () => {
        const formGroup = service.createEsercizioFormGroup(sampleWithNewData);

        const esercizio = service.getEsercizio(formGroup) as any;

        expect(esercizio).toMatchObject(sampleWithNewData);
      });

      it('should return NewEsercizio for empty Esercizio initial value', () => {
        const formGroup = service.createEsercizioFormGroup();

        const esercizio = service.getEsercizio(formGroup) as any;

        expect(esercizio).toMatchObject({});
      });

      it('should return IEsercizio', () => {
        const formGroup = service.createEsercizioFormGroup(sampleWithRequiredData);

        const esercizio = service.getEsercizio(formGroup) as any;

        expect(esercizio).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEsercizio should not enable id FormControl', () => {
        const formGroup = service.createEsercizioFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEsercizio should disable id FormControl', () => {
        const formGroup = service.createEsercizioFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
