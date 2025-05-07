import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../cliente-to-lezione-corso.test-samples';

import { ClienteToLezioneCorsoFormService } from './cliente-to-lezione-corso-form.service';

describe('ClienteToLezioneCorso Form Service', () => {
  let service: ClienteToLezioneCorsoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ClienteToLezioneCorsoFormService);
  });

  describe('Service methods', () => {
    describe('createClienteToLezioneCorsoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createClienteToLezioneCorsoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            completato: expect.any(Object),
            dataCompletamento: expect.any(Object),
            cliente: expect.any(Object),
            lezioneCorso: expect.any(Object),
          }),
        );
      });

      it('passing IClienteToLezioneCorso should create a new form with FormGroup', () => {
        const formGroup = service.createClienteToLezioneCorsoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            completato: expect.any(Object),
            dataCompletamento: expect.any(Object),
            cliente: expect.any(Object),
            lezioneCorso: expect.any(Object),
          }),
        );
      });
    });

    describe('getClienteToLezioneCorso', () => {
      it('should return NewClienteToLezioneCorso for default ClienteToLezioneCorso initial value', () => {
        const formGroup = service.createClienteToLezioneCorsoFormGroup(sampleWithNewData);

        const clienteToLezioneCorso = service.getClienteToLezioneCorso(formGroup) as any;

        expect(clienteToLezioneCorso).toMatchObject(sampleWithNewData);
      });

      it('should return NewClienteToLezioneCorso for empty ClienteToLezioneCorso initial value', () => {
        const formGroup = service.createClienteToLezioneCorsoFormGroup();

        const clienteToLezioneCorso = service.getClienteToLezioneCorso(formGroup) as any;

        expect(clienteToLezioneCorso).toMatchObject({});
      });

      it('should return IClienteToLezioneCorso', () => {
        const formGroup = service.createClienteToLezioneCorsoFormGroup(sampleWithRequiredData);

        const clienteToLezioneCorso = service.getClienteToLezioneCorso(formGroup) as any;

        expect(clienteToLezioneCorso).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IClienteToLezioneCorso should not enable id FormControl', () => {
        const formGroup = service.createClienteToLezioneCorsoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewClienteToLezioneCorso should disable id FormControl', () => {
        const formGroup = service.createClienteToLezioneCorsoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
