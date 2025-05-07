import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../peso-cliente.test-samples';

import { PesoClienteFormService } from './peso-cliente-form.service';

describe('PesoCliente Form Service', () => {
  let service: PesoClienteFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PesoClienteFormService);
  });

  describe('Service methods', () => {
    describe('createPesoClienteFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPesoClienteFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            peso: expect.any(Object),
            mese: expect.any(Object),
            dataInserimento: expect.any(Object),
            cliente: expect.any(Object),
          }),
        );
      });

      it('passing IPesoCliente should create a new form with FormGroup', () => {
        const formGroup = service.createPesoClienteFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            peso: expect.any(Object),
            mese: expect.any(Object),
            dataInserimento: expect.any(Object),
            cliente: expect.any(Object),
          }),
        );
      });
    });

    describe('getPesoCliente', () => {
      it('should return NewPesoCliente for default PesoCliente initial value', () => {
        const formGroup = service.createPesoClienteFormGroup(sampleWithNewData);

        const pesoCliente = service.getPesoCliente(formGroup) as any;

        expect(pesoCliente).toMatchObject(sampleWithNewData);
      });

      it('should return NewPesoCliente for empty PesoCliente initial value', () => {
        const formGroup = service.createPesoClienteFormGroup();

        const pesoCliente = service.getPesoCliente(formGroup) as any;

        expect(pesoCliente).toMatchObject({});
      });

      it('should return IPesoCliente', () => {
        const formGroup = service.createPesoClienteFormGroup(sampleWithRequiredData);

        const pesoCliente = service.getPesoCliente(formGroup) as any;

        expect(pesoCliente).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPesoCliente should not enable id FormControl', () => {
        const formGroup = service.createPesoClienteFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPesoCliente should disable id FormControl', () => {
        const formGroup = service.createPesoClienteFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
