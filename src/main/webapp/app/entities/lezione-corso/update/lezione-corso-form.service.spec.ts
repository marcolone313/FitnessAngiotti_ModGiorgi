import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../lezione-corso.test-samples';

import { LezioneCorsoFormService } from './lezione-corso-form.service';

describe('LezioneCorso Form Service', () => {
  let service: LezioneCorsoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LezioneCorsoFormService);
  });

  describe('Service methods', () => {
    describe('createLezioneCorsoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createLezioneCorsoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            titolo: expect.any(Object),
            descrizione: expect.any(Object),
            puntiFocali: expect.any(Object),
            videoUrl: expect.any(Object),
            corsoAcademy: expect.any(Object),
          }),
        );
      });

      it('passing ILezioneCorso should create a new form with FormGroup', () => {
        const formGroup = service.createLezioneCorsoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            titolo: expect.any(Object),
            descrizione: expect.any(Object),
            puntiFocali: expect.any(Object),
            videoUrl: expect.any(Object),
            corsoAcademy: expect.any(Object),
          }),
        );
      });
    });

    describe('getLezioneCorso', () => {
      it('should return NewLezioneCorso for default LezioneCorso initial value', () => {
        const formGroup = service.createLezioneCorsoFormGroup(sampleWithNewData);

        const lezioneCorso = service.getLezioneCorso(formGroup) as any;

        expect(lezioneCorso).toMatchObject(sampleWithNewData);
      });

      it('should return NewLezioneCorso for empty LezioneCorso initial value', () => {
        const formGroup = service.createLezioneCorsoFormGroup();

        const lezioneCorso = service.getLezioneCorso(formGroup) as any;

        expect(lezioneCorso).toMatchObject({});
      });

      it('should return ILezioneCorso', () => {
        const formGroup = service.createLezioneCorsoFormGroup(sampleWithRequiredData);

        const lezioneCorso = service.getLezioneCorso(formGroup) as any;

        expect(lezioneCorso).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ILezioneCorso should not enable id FormControl', () => {
        const formGroup = service.createLezioneCorsoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewLezioneCorso should disable id FormControl', () => {
        const formGroup = service.createLezioneCorsoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
