jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, fakeAsync, inject, tick } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { SchedaSettimanaleService } from '../service/scheda-settimanale.service';

import { SchedaSettimanaleDeleteDialogComponent } from './scheda-settimanale-delete-dialog.component';

describe('SchedaSettimanale Management Delete Component', () => {
  let comp: SchedaSettimanaleDeleteDialogComponent;
  let fixture: ComponentFixture<SchedaSettimanaleDeleteDialogComponent>;
  let service: SchedaSettimanaleService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SchedaSettimanaleDeleteDialogComponent],
      providers: [provideHttpClient(), NgbActiveModal],
    })
      .overrideTemplate(SchedaSettimanaleDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SchedaSettimanaleDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(SchedaSettimanaleService);
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
