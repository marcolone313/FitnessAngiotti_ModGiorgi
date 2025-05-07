jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, fakeAsync, inject, tick } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { AllenamentoGiornalieroService } from '../service/allenamento-giornaliero.service';

import { AllenamentoGiornalieroDeleteDialogComponent } from './allenamento-giornaliero-delete-dialog.component';

describe('AllenamentoGiornaliero Management Delete Component', () => {
  let comp: AllenamentoGiornalieroDeleteDialogComponent;
  let fixture: ComponentFixture<AllenamentoGiornalieroDeleteDialogComponent>;
  let service: AllenamentoGiornalieroService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AllenamentoGiornalieroDeleteDialogComponent],
      providers: [provideHttpClient(), NgbActiveModal],
    })
      .overrideTemplate(AllenamentoGiornalieroDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AllenamentoGiornalieroDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(AllenamentoGiornalieroService);
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
