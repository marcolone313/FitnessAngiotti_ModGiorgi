import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IAllenamentoGiornaliero } from 'app/entities/allenamento-giornaliero/allenamento-giornaliero.model';
import { AllenamentoGiornalieroService } from 'app/entities/allenamento-giornaliero/service/allenamento-giornaliero.service';
import { CircuitoService } from '../service/circuito.service';
import { ICircuito } from '../circuito.model';
import { CircuitoFormService } from './circuito-form.service';

import { CircuitoUpdateComponent } from './circuito-update.component';

describe('Circuito Management Update Component', () => {
  let comp: CircuitoUpdateComponent;
  let fixture: ComponentFixture<CircuitoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let circuitoFormService: CircuitoFormService;
  let circuitoService: CircuitoService;
  let allenamentoGiornalieroService: AllenamentoGiornalieroService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CircuitoUpdateComponent],
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
      .overrideTemplate(CircuitoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CircuitoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    circuitoFormService = TestBed.inject(CircuitoFormService);
    circuitoService = TestBed.inject(CircuitoService);
    allenamentoGiornalieroService = TestBed.inject(AllenamentoGiornalieroService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call allenamentoGiornaliero query and add missing value', () => {
      const circuito: ICircuito = { id: 456 };
      const allenamentoGiornaliero: IAllenamentoGiornaliero = { id: 24439 };
      circuito.allenamentoGiornaliero = allenamentoGiornaliero;

      const allenamentoGiornalieroCollection: IAllenamentoGiornaliero[] = [{ id: 25934 }];
      jest.spyOn(allenamentoGiornalieroService, 'query').mockReturnValue(of(new HttpResponse({ body: allenamentoGiornalieroCollection })));
      const expectedCollection: IAllenamentoGiornaliero[] = [allenamentoGiornaliero, ...allenamentoGiornalieroCollection];
      jest.spyOn(allenamentoGiornalieroService, 'addAllenamentoGiornalieroToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ circuito });
      comp.ngOnInit();

      expect(allenamentoGiornalieroService.query).toHaveBeenCalled();
      expect(allenamentoGiornalieroService.addAllenamentoGiornalieroToCollectionIfMissing).toHaveBeenCalledWith(
        allenamentoGiornalieroCollection,
        allenamentoGiornaliero,
      );
      expect(comp.allenamentoGiornalierosCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const circuito: ICircuito = { id: 456 };
      const allenamentoGiornaliero: IAllenamentoGiornaliero = { id: 20379 };
      circuito.allenamentoGiornaliero = allenamentoGiornaliero;

      activatedRoute.data = of({ circuito });
      comp.ngOnInit();

      expect(comp.allenamentoGiornalierosCollection).toContain(allenamentoGiornaliero);
      expect(comp.circuito).toEqual(circuito);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICircuito>>();
      const circuito = { id: 123 };
      jest.spyOn(circuitoFormService, 'getCircuito').mockReturnValue(circuito);
      jest.spyOn(circuitoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ circuito });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: circuito }));
      saveSubject.complete();

      // THEN
      expect(circuitoFormService.getCircuito).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(circuitoService.update).toHaveBeenCalledWith(expect.objectContaining(circuito));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICircuito>>();
      const circuito = { id: 123 };
      jest.spyOn(circuitoFormService, 'getCircuito').mockReturnValue({ id: null });
      jest.spyOn(circuitoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ circuito: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: circuito }));
      saveSubject.complete();

      // THEN
      expect(circuitoFormService.getCircuito).toHaveBeenCalled();
      expect(circuitoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICircuito>>();
      const circuito = { id: 123 };
      jest.spyOn(circuitoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ circuito });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(circuitoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAllenamentoGiornaliero', () => {
      it('Should forward to allenamentoGiornalieroService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(allenamentoGiornalieroService, 'compareAllenamentoGiornaliero');
        comp.compareAllenamentoGiornaliero(entity, entity2);
        expect(allenamentoGiornalieroService.compareAllenamentoGiornaliero).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
