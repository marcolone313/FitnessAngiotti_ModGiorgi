import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IEsercizio } from 'app/entities/esercizio/esercizio.model';
import { EsercizioService } from 'app/entities/esercizio/service/esercizio.service';
import { PassaggioEsercizioService } from '../service/passaggio-esercizio.service';
import { IPassaggioEsercizio } from '../passaggio-esercizio.model';
import { PassaggioEsercizioFormService } from './passaggio-esercizio-form.service';

import { PassaggioEsercizioUpdateComponent } from './passaggio-esercizio-update.component';

describe('PassaggioEsercizio Management Update Component', () => {
  let comp: PassaggioEsercizioUpdateComponent;
  let fixture: ComponentFixture<PassaggioEsercizioUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let passaggioEsercizioFormService: PassaggioEsercizioFormService;
  let passaggioEsercizioService: PassaggioEsercizioService;
  let esercizioService: EsercizioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [PassaggioEsercizioUpdateComponent],
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
      .overrideTemplate(PassaggioEsercizioUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PassaggioEsercizioUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    passaggioEsercizioFormService = TestBed.inject(PassaggioEsercizioFormService);
    passaggioEsercizioService = TestBed.inject(PassaggioEsercizioService);
    esercizioService = TestBed.inject(EsercizioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Esercizio query and add missing value', () => {
      const passaggioEsercizio: IPassaggioEsercizio = { id: 456 };
      const esercizio: IEsercizio = { id: 3861 };
      passaggioEsercizio.esercizio = esercizio;

      const esercizioCollection: IEsercizio[] = [{ id: 9485 }];
      jest.spyOn(esercizioService, 'query').mockReturnValue(of(new HttpResponse({ body: esercizioCollection })));
      const additionalEsercizios = [esercizio];
      const expectedCollection: IEsercizio[] = [...additionalEsercizios, ...esercizioCollection];
      jest.spyOn(esercizioService, 'addEsercizioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ passaggioEsercizio });
      comp.ngOnInit();

      expect(esercizioService.query).toHaveBeenCalled();
      expect(esercizioService.addEsercizioToCollectionIfMissing).toHaveBeenCalledWith(
        esercizioCollection,
        ...additionalEsercizios.map(expect.objectContaining),
      );
      expect(comp.eserciziosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const passaggioEsercizio: IPassaggioEsercizio = { id: 456 };
      const esercizio: IEsercizio = { id: 25292 };
      passaggioEsercizio.esercizio = esercizio;

      activatedRoute.data = of({ passaggioEsercizio });
      comp.ngOnInit();

      expect(comp.eserciziosSharedCollection).toContain(esercizio);
      expect(comp.passaggioEsercizio).toEqual(passaggioEsercizio);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPassaggioEsercizio>>();
      const passaggioEsercizio = { id: 123 };
      jest.spyOn(passaggioEsercizioFormService, 'getPassaggioEsercizio').mockReturnValue(passaggioEsercizio);
      jest.spyOn(passaggioEsercizioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ passaggioEsercizio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: passaggioEsercizio }));
      saveSubject.complete();

      // THEN
      expect(passaggioEsercizioFormService.getPassaggioEsercizio).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(passaggioEsercizioService.update).toHaveBeenCalledWith(expect.objectContaining(passaggioEsercizio));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPassaggioEsercizio>>();
      const passaggioEsercizio = { id: 123 };
      jest.spyOn(passaggioEsercizioFormService, 'getPassaggioEsercizio').mockReturnValue({ id: null });
      jest.spyOn(passaggioEsercizioService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ passaggioEsercizio: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: passaggioEsercizio }));
      saveSubject.complete();

      // THEN
      expect(passaggioEsercizioFormService.getPassaggioEsercizio).toHaveBeenCalled();
      expect(passaggioEsercizioService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPassaggioEsercizio>>();
      const passaggioEsercizio = { id: 123 };
      jest.spyOn(passaggioEsercizioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ passaggioEsercizio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(passaggioEsercizioService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEsercizio', () => {
      it('Should forward to esercizioService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(esercizioService, 'compareEsercizio');
        comp.compareEsercizio(entity, entity2);
        expect(esercizioService.compareEsercizio).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
