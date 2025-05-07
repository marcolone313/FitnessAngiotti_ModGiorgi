import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IAllenamentoGiornaliero } from 'app/entities/allenamento-giornaliero/allenamento-giornaliero.model';
import { AllenamentoGiornalieroService } from 'app/entities/allenamento-giornaliero/service/allenamento-giornaliero.service';
import { CorsaService } from '../service/corsa.service';
import { ICorsa } from '../corsa.model';
import { CorsaFormService } from './corsa-form.service';

import { CorsaUpdateComponent } from './corsa-update.component';

describe('Corsa Management Update Component', () => {
  let comp: CorsaUpdateComponent;
  let fixture: ComponentFixture<CorsaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let corsaFormService: CorsaFormService;
  let corsaService: CorsaService;
  let allenamentoGiornalieroService: AllenamentoGiornalieroService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CorsaUpdateComponent],
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
      .overrideTemplate(CorsaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CorsaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    corsaFormService = TestBed.inject(CorsaFormService);
    corsaService = TestBed.inject(CorsaService);
    allenamentoGiornalieroService = TestBed.inject(AllenamentoGiornalieroService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call allenamentoGiornaliero query and add missing value', () => {
      const corsa: ICorsa = { id: 456 };
      const allenamentoGiornaliero: IAllenamentoGiornaliero = { id: 9126 };
      corsa.allenamentoGiornaliero = allenamentoGiornaliero;

      const allenamentoGiornalieroCollection: IAllenamentoGiornaliero[] = [{ id: 25280 }];
      jest.spyOn(allenamentoGiornalieroService, 'query').mockReturnValue(of(new HttpResponse({ body: allenamentoGiornalieroCollection })));
      const expectedCollection: IAllenamentoGiornaliero[] = [allenamentoGiornaliero, ...allenamentoGiornalieroCollection];
      jest.spyOn(allenamentoGiornalieroService, 'addAllenamentoGiornalieroToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ corsa });
      comp.ngOnInit();

      expect(allenamentoGiornalieroService.query).toHaveBeenCalled();
      expect(allenamentoGiornalieroService.addAllenamentoGiornalieroToCollectionIfMissing).toHaveBeenCalledWith(
        allenamentoGiornalieroCollection,
        allenamentoGiornaliero,
      );
      expect(comp.allenamentoGiornalierosCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const corsa: ICorsa = { id: 456 };
      const allenamentoGiornaliero: IAllenamentoGiornaliero = { id: 12528 };
      corsa.allenamentoGiornaliero = allenamentoGiornaliero;

      activatedRoute.data = of({ corsa });
      comp.ngOnInit();

      expect(comp.allenamentoGiornalierosCollection).toContain(allenamentoGiornaliero);
      expect(comp.corsa).toEqual(corsa);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICorsa>>();
      const corsa = { id: 123 };
      jest.spyOn(corsaFormService, 'getCorsa').mockReturnValue(corsa);
      jest.spyOn(corsaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ corsa });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: corsa }));
      saveSubject.complete();

      // THEN
      expect(corsaFormService.getCorsa).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(corsaService.update).toHaveBeenCalledWith(expect.objectContaining(corsa));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICorsa>>();
      const corsa = { id: 123 };
      jest.spyOn(corsaFormService, 'getCorsa').mockReturnValue({ id: null });
      jest.spyOn(corsaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ corsa: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: corsa }));
      saveSubject.complete();

      // THEN
      expect(corsaFormService.getCorsa).toHaveBeenCalled();
      expect(corsaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICorsa>>();
      const corsa = { id: 123 };
      jest.spyOn(corsaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ corsa });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(corsaService.update).toHaveBeenCalled();
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
