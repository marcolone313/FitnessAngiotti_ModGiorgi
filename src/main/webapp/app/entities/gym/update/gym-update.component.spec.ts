import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IAllenamentoGiornaliero } from 'app/entities/allenamento-giornaliero/allenamento-giornaliero.model';
import { AllenamentoGiornalieroService } from 'app/entities/allenamento-giornaliero/service/allenamento-giornaliero.service';
import { IEsercizio } from 'app/entities/esercizio/esercizio.model';
import { EsercizioService } from 'app/entities/esercizio/service/esercizio.service';
import { IGym } from '../gym.model';
import { GymService } from '../service/gym.service';
import { GymFormService } from './gym-form.service';

import { GymUpdateComponent } from './gym-update.component';

describe('Gym Management Update Component', () => {
  let comp: GymUpdateComponent;
  let fixture: ComponentFixture<GymUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let gymFormService: GymFormService;
  let gymService: GymService;
  let allenamentoGiornalieroService: AllenamentoGiornalieroService;
  let esercizioService: EsercizioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [GymUpdateComponent],
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
      .overrideTemplate(GymUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GymUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    gymFormService = TestBed.inject(GymFormService);
    gymService = TestBed.inject(GymService);
    allenamentoGiornalieroService = TestBed.inject(AllenamentoGiornalieroService);
    esercizioService = TestBed.inject(EsercizioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call AllenamentoGiornaliero query and add missing value', () => {
      const gym: IGym = { id: 456 };
      const allenamentoGiornaliero: IAllenamentoGiornaliero = { id: 7760 };
      gym.allenamentoGiornaliero = allenamentoGiornaliero;

      const allenamentoGiornalieroCollection: IAllenamentoGiornaliero[] = [{ id: 26929 }];
      jest.spyOn(allenamentoGiornalieroService, 'query').mockReturnValue(of(new HttpResponse({ body: allenamentoGiornalieroCollection })));
      const additionalAllenamentoGiornalieros = [allenamentoGiornaliero];
      const expectedCollection: IAllenamentoGiornaliero[] = [...additionalAllenamentoGiornalieros, ...allenamentoGiornalieroCollection];
      jest.spyOn(allenamentoGiornalieroService, 'addAllenamentoGiornalieroToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ gym });
      comp.ngOnInit();

      expect(allenamentoGiornalieroService.query).toHaveBeenCalled();
      expect(allenamentoGiornalieroService.addAllenamentoGiornalieroToCollectionIfMissing).toHaveBeenCalledWith(
        allenamentoGiornalieroCollection,
        ...additionalAllenamentoGiornalieros.map(expect.objectContaining),
      );
      expect(comp.allenamentoGiornalierosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Esercizio query and add missing value', () => {
      const gym: IGym = { id: 456 };
      const esercizio: IEsercizio = { id: 31246 };
      gym.esercizio = esercizio;

      const esercizioCollection: IEsercizio[] = [{ id: 28294 }];
      jest.spyOn(esercizioService, 'query').mockReturnValue(of(new HttpResponse({ body: esercizioCollection })));
      const additionalEsercizios = [esercizio];
      const expectedCollection: IEsercizio[] = [...additionalEsercizios, ...esercizioCollection];
      jest.spyOn(esercizioService, 'addEsercizioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ gym });
      comp.ngOnInit();

      expect(esercizioService.query).toHaveBeenCalled();
      expect(esercizioService.addEsercizioToCollectionIfMissing).toHaveBeenCalledWith(
        esercizioCollection,
        ...additionalEsercizios.map(expect.objectContaining),
      );
      expect(comp.eserciziosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const gym: IGym = { id: 456 };
      const allenamentoGiornaliero: IAllenamentoGiornaliero = { id: 19418 };
      gym.allenamentoGiornaliero = allenamentoGiornaliero;
      const esercizio: IEsercizio = { id: 18388 };
      gym.esercizio = esercizio;

      activatedRoute.data = of({ gym });
      comp.ngOnInit();

      expect(comp.allenamentoGiornalierosSharedCollection).toContain(allenamentoGiornaliero);
      expect(comp.eserciziosSharedCollection).toContain(esercizio);
      expect(comp.gym).toEqual(gym);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGym>>();
      const gym = { id: 123 };
      jest.spyOn(gymFormService, 'getGym').mockReturnValue(gym);
      jest.spyOn(gymService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gym });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: gym }));
      saveSubject.complete();

      // THEN
      expect(gymFormService.getGym).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(gymService.update).toHaveBeenCalledWith(expect.objectContaining(gym));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGym>>();
      const gym = { id: 123 };
      jest.spyOn(gymFormService, 'getGym').mockReturnValue({ id: null });
      jest.spyOn(gymService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gym: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: gym }));
      saveSubject.complete();

      // THEN
      expect(gymFormService.getGym).toHaveBeenCalled();
      expect(gymService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGym>>();
      const gym = { id: 123 };
      jest.spyOn(gymService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gym });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(gymService.update).toHaveBeenCalled();
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
