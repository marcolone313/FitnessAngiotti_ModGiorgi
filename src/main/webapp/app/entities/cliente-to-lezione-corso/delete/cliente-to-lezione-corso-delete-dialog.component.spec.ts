jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, fakeAsync, inject, tick } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ClienteToLezioneCorsoService } from '../service/cliente-to-lezione-corso.service';

import { ClienteToLezioneCorsoDeleteDialogComponent } from './cliente-to-lezione-corso-delete-dialog.component';

describe('ClienteToLezioneCorso Management Delete Component', () => {
  let comp: ClienteToLezioneCorsoDeleteDialogComponent;
  let fixture: ComponentFixture<ClienteToLezioneCorsoDeleteDialogComponent>;
  let service: ClienteToLezioneCorsoService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ClienteToLezioneCorsoDeleteDialogComponent],
      providers: [provideHttpClient(), NgbActiveModal],
    })
      .overrideTemplate(ClienteToLezioneCorsoDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ClienteToLezioneCorsoDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ClienteToLezioneCorsoService);
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
