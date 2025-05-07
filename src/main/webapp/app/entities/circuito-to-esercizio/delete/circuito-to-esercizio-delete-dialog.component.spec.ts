jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, fakeAsync, inject, tick } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { CircuitoToEsercizioService } from '../service/circuito-to-esercizio.service';

import { CircuitoToEsercizioDeleteDialogComponent } from './circuito-to-esercizio-delete-dialog.component';

describe('CircuitoToEsercizio Management Delete Component', () => {
  let comp: CircuitoToEsercizioDeleteDialogComponent;
  let fixture: ComponentFixture<CircuitoToEsercizioDeleteDialogComponent>;
  let service: CircuitoToEsercizioService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CircuitoToEsercizioDeleteDialogComponent],
      providers: [provideHttpClient(), NgbActiveModal],
    })
      .overrideTemplate(CircuitoToEsercizioDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CircuitoToEsercizioDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(CircuitoToEsercizioService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      }),
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
