import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../circonferenza.test-samples';

import { CirconferenzaFormService } from './circonferenza-form.service';

describe('Circonferenza Form Service', () => {
  let service: CirconferenzaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CirconferenzaFormService);
  });

  describe('Service methods', () => {
    describe('createCirconferenzaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCirconferenzaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            torace: expect.any(Object),
            braccio: expect.any(Object),
            avambraccio: expect.any(Object),
            ombelico: expect.any(Object),
            fianchi: expect.any(Object),
            sottoOmbelico: expect.any(Object),
            vita: expect.any(Object),
            coscia: expect.any(Object),
            mese: expect.any(Object),
            dataInserimento: expect.any(Object),
            cliente: expect.any(Object),
          }),
        );
      });

      it('passing ICirconferenza should create a new form with FormGroup', () => {
        const formGroup = service.createCirconferenzaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            torace: expect.any(Object),
            braccio: expect.any(Object),
            avambraccio: expect.any(Object),
            ombelico: expect.any(Object),
            fianchi: expect.any(Object),
            sottoOmbelico: expect.any(Object),
            vita: expect.any(Object),
            coscia: expect.any(Object),
            mese: expect.any(Object),
            dataInserimento: expect.any(Object),
            cliente: expect.any(Object),
          }),
        );
      });
    });

    describe('getCirconferenza', () => {
      it('should return NewCirconferenza for default Circonferenza initial value', () => {
        const formGroup = service.createCirconferenzaFormGroup(sampleWithNewData);

        const circonferenza = service.getCirconferenza(formGroup) as any;

        expect(circonferenza).toMatchObject(sampleWithNewData);
      });

      it('should return NewCirconferenza for empty Circonferenza initial value', () => {
        const formGroup = service.createCirconferenzaFormGroup();

        const circonferenza = service.getCirconferenza(formGroup) as any;

        expect(circonferenza).toMatchObject({});
      });

      it('should return ICirconferenza', () => {
        const formGroup = service.createCirconferenzaFormGroup(sampleWithRequiredData);

        const circonferenza = service.getCirconferenza(formGroup) as any;

        expect(circonferenza).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICirconferenza should not enable id FormControl', () => {
        const formGroup = service.createCirconferenzaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCirconferenza should disable id FormControl', () => {
        const formGroup = service.createCirconferenzaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
