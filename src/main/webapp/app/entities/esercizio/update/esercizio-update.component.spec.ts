import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { EsercizioService } from '../service/esercizio.service';
import { IEsercizio } from '../esercizio.model';
import { EsercizioFormService } from './esercizio-form.service';

import { EsercizioUpdateComponent } from './esercizio-update.component';

describe('Esercizio Management Update Component', () => {
  let comp: EsercizioUpdateComponent;
  let fixture: ComponentFixture<EsercizioUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let esercizioFormService: EsercizioFormService;
  let esercizioService: EsercizioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [EsercizioUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(EsercizioUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EsercizioUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    esercizioFormService = TestBed.inject(EsercizioFormService);
    esercizioService = TestBed.inject(EsercizioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const esercizio: IEsercizio = { id: 456 };

      activatedRoute.data = of({ esercizio });
      comp.ngOnInit();

      expect(comp.esercizio).toEqual(esercizio);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEsercizio>>();
      const esercizio = { id: 123 };
      jest.spyOn(esercizioFormService, 'getEsercizio').mockReturnValue(esercizio);
      jest.spyOn(esercizioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ esercizio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: esercizio }));
      saveSubject.complete();

      // THEN
      expect(esercizioFormService.getEsercizio).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(esercizioService.update).toHaveBeenCalledWith(expect.objectContaining(esercizio));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEsercizio>>();
      const esercizio = { id: 123 };
      jest.spyOn(esercizioFormService, 'getEsercizio').mockReturnValue({ id: null });
      jest.spyOn(esercizioService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ esercizio: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: esercizio }));
      saveSubject.complete();

      // THEN
      expect(esercizioFormService.getEsercizio).toHaveBeenCalled();
      expect(esercizioService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEsercizio>>();
      const esercizio = { id: 123 };
      jest.spyOn(esercizioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ esercizio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(esercizioService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
