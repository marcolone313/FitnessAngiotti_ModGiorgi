import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IEsercizio } from 'app/entities/esercizio/esercizio.model';
import { EsercizioService } from 'app/entities/esercizio/service/esercizio.service';
import { ICircuito } from 'app/entities/circuito/circuito.model';
import { CircuitoService } from 'app/entities/circuito/service/circuito.service';
import { ICircuitoToEsercizio } from '../circuito-to-esercizio.model';
import { CircuitoToEsercizioService } from '../service/circuito-to-esercizio.service';
import { CircuitoToEsercizioFormService } from './circuito-to-esercizio-form.service';

import { CircuitoToEsercizioUpdateComponent } from './circuito-to-esercizio-update.component';

describe('CircuitoToEsercizio Management Update Component', () => {
  let comp: CircuitoToEsercizioUpdateComponent;
  let fixture: ComponentFixture<CircuitoToEsercizioUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let circuitoToEsercizioFormService: CircuitoToEsercizioFormService;
  let circuitoToEsercizioService: CircuitoToEsercizioService;
  let esercizioService: EsercizioService;
  let circuitoService: CircuitoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CircuitoToEsercizioUpdateComponent],
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
      .overrideTemplate(CircuitoToEsercizioUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CircuitoToEsercizioUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    circuitoToEsercizioFormService = TestBed.inject(CircuitoToEsercizioFormService);
    circuitoToEsercizioService = TestBed.inject(CircuitoToEsercizioService);
    esercizioService = TestBed.inject(EsercizioService);
    circuitoService = TestBed.inject(CircuitoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Esercizio query and add missing value', () => {
      const circuitoToEsercizio: ICircuitoToEsercizio = { id: 456 };
      const esercizio: IEsercizio = { id: 28833 };
      circuitoToEsercizio.esercizio = esercizio;

      const esercizioCollection: IEsercizio[] = [{ id: 14486 }];
      jest.spyOn(esercizioService, 'query').mockReturnValue(of(new HttpResponse({ body: esercizioCollection })));
      const additionalEsercizios = [esercizio];
      const expectedCollection: IEsercizio[] = [...additionalEsercizios, ...esercizioCollection];
      jest.spyOn(esercizioService, 'addEsercizioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ circuitoToEsercizio });
      comp.ngOnInit();

      expect(esercizioService.query).toHaveBeenCalled();
      expect(esercizioService.addEsercizioToCollectionIfMissing).toHaveBeenCalledWith(
        esercizioCollection,
        ...additionalEsercizios.map(expect.objectContaining),
      );
      expect(comp.eserciziosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Circuito query and add missing value', () => {
      const circuitoToEsercizio: ICircuitoToEsercizio = { id: 456 };
      const circuito: ICircuito = { id: 17959 };
      circuitoToEsercizio.circuito = circuito;

      const circuitoCollection: ICircuito[] = [{ id: 11016 }];
      jest.spyOn(circuitoService, 'query').mockReturnValue(of(new HttpResponse({ body: circuitoCollection })));
      const additionalCircuitos = [circuito];
      const expectedCollection: ICircuito[] = [...additionalCircuitos, ...circuitoCollection];
      jest.spyOn(circuitoService, 'addCircuitoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ circuitoToEsercizio });
      comp.ngOnInit();

      expect(circuitoService.query).toHaveBeenCalled();
      expect(circuitoService.addCircuitoToCollectionIfMissing).toHaveBeenCalledWith(
        circuitoCollection,
        ...additionalCircuitos.map(expect.objectContaining),
      );
      expect(comp.circuitosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const circuitoToEsercizio: ICircuitoToEsercizio = { id: 456 };
      const esercizio: IEsercizio = { id: 10236 };
      circuitoToEsercizio.esercizio = esercizio;
      const circuito: ICircuito = { id: 4108 };
      circuitoToEsercizio.circuito = circuito;

      activatedRoute.data = of({ circuitoToEsercizio });
      comp.ngOnInit();

      expect(comp.eserciziosSharedCollection).toContain(esercizio);
      expect(comp.circuitosSharedCollection).toContain(circuito);
      expect(comp.circuitoToEsercizio).toEqual(circuitoToEsercizio);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICircuitoToEsercizio>>();
      const circuitoToEsercizio = { id: 123 };
      jest.spyOn(circuitoToEsercizioFormService, 'getCircuitoToEsercizio').mockReturnValue(circuitoToEsercizio);
      jest.spyOn(circuitoToEsercizioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ circuitoToEsercizio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: circuitoToEsercizio }));
      saveSubject.complete();

      // THEN
      expect(circuitoToEsercizioFormService.getCircuitoToEsercizio).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(circuitoToEsercizioService.update).toHaveBeenCalledWith(expect.objectContaining(circuitoToEsercizio));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICircuitoToEsercizio>>();
      const circuitoToEsercizio = { id: 123 };
      jest.spyOn(circuitoToEsercizioFormService, 'getCircuitoToEsercizio').mockReturnValue({ id: null });
      jest.spyOn(circuitoToEsercizioService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ circuitoToEsercizio: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: circuitoToEsercizio }));
      saveSubject.complete();

      // THEN
      expect(circuitoToEsercizioFormService.getCircuitoToEsercizio).toHaveBeenCalled();
      expect(circuitoToEsercizioService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICircuitoToEsercizio>>();
      const circuitoToEsercizio = { id: 123 };
      jest.spyOn(circuitoToEsercizioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ circuitoToEsercizio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(circuitoToEsercizioService.update).toHaveBeenCalled();
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

    describe('compareCircuito', () => {
      it('Should forward to circuitoService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(circuitoService, 'compareCircuito');
        comp.compareCircuito(entity, entity2);
        expect(circuitoService.compareCircuito).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
