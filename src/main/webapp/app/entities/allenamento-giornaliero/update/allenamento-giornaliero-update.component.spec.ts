import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ISchedaSettimanale } from 'app/entities/scheda-settimanale/scheda-settimanale.model';
import { SchedaSettimanaleService } from 'app/entities/scheda-settimanale/service/scheda-settimanale.service';
import { AllenamentoGiornalieroService } from '../service/allenamento-giornaliero.service';
import { IAllenamentoGiornaliero } from '../allenamento-giornaliero.model';
import { AllenamentoGiornalieroFormService } from './allenamento-giornaliero-form.service';

import { AllenamentoGiornalieroUpdateComponent } from './allenamento-giornaliero-update.component';

describe('AllenamentoGiornaliero Management Update Component', () => {
  let comp: AllenamentoGiornalieroUpdateComponent;
  let fixture: ComponentFixture<AllenamentoGiornalieroUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let allenamentoGiornalieroFormService: AllenamentoGiornalieroFormService;
  let allenamentoGiornalieroService: AllenamentoGiornalieroService;
  let schedaSettimanaleService: SchedaSettimanaleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AllenamentoGiornalieroUpdateComponent],
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
      .overrideTemplate(AllenamentoGiornalieroUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AllenamentoGiornalieroUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    allenamentoGiornalieroFormService = TestBed.inject(AllenamentoGiornalieroFormService);
    allenamentoGiornalieroService = TestBed.inject(AllenamentoGiornalieroService);
    schedaSettimanaleService = TestBed.inject(SchedaSettimanaleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call SchedaSettimanale query and add missing value', () => {
      const allenamentoGiornaliero: IAllenamentoGiornaliero = { id: 456 };
      const schedaSettimanale: ISchedaSettimanale = { id: 23164 };
      allenamentoGiornaliero.schedaSettimanale = schedaSettimanale;

      const schedaSettimanaleCollection: ISchedaSettimanale[] = [{ id: 29059 }];
      jest.spyOn(schedaSettimanaleService, 'query').mockReturnValue(of(new HttpResponse({ body: schedaSettimanaleCollection })));
      const additionalSchedaSettimanales = [schedaSettimanale];
      const expectedCollection: ISchedaSettimanale[] = [...additionalSchedaSettimanales, ...schedaSettimanaleCollection];
      jest.spyOn(schedaSettimanaleService, 'addSchedaSettimanaleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ allenamentoGiornaliero });
      comp.ngOnInit();

      expect(schedaSettimanaleService.query).toHaveBeenCalled();
      expect(schedaSettimanaleService.addSchedaSettimanaleToCollectionIfMissing).toHaveBeenCalledWith(
        schedaSettimanaleCollection,
        ...additionalSchedaSettimanales.map(expect.objectContaining),
      );
      expect(comp.schedaSettimanalesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const allenamentoGiornaliero: IAllenamentoGiornaliero = { id: 456 };
      const schedaSettimanale: ISchedaSettimanale = { id: 3398 };
      allenamentoGiornaliero.schedaSettimanale = schedaSettimanale;

      activatedRoute.data = of({ allenamentoGiornaliero });
      comp.ngOnInit();

      expect(comp.schedaSettimanalesSharedCollection).toContain(schedaSettimanale);
      expect(comp.allenamentoGiornaliero).toEqual(allenamentoGiornaliero);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAllenamentoGiornaliero>>();
      const allenamentoGiornaliero = { id: 123 };
      jest.spyOn(allenamentoGiornalieroFormService, 'getAllenamentoGiornaliero').mockReturnValue(allenamentoGiornaliero);
      jest.spyOn(allenamentoGiornalieroService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ allenamentoGiornaliero });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: allenamentoGiornaliero }));
      saveSubject.complete();

      // THEN
      expect(allenamentoGiornalieroFormService.getAllenamentoGiornaliero).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(allenamentoGiornalieroService.update).toHaveBeenCalledWith(expect.objectContaining(allenamentoGiornaliero));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAllenamentoGiornaliero>>();
      const allenamentoGiornaliero = { id: 123 };
      jest.spyOn(allenamentoGiornalieroFormService, 'getAllenamentoGiornaliero').mockReturnValue({ id: null });
      jest.spyOn(allenamentoGiornalieroService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ allenamentoGiornaliero: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: allenamentoGiornaliero }));
      saveSubject.complete();

      // THEN
      expect(allenamentoGiornalieroFormService.getAllenamentoGiornaliero).toHaveBeenCalled();
      expect(allenamentoGiornalieroService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAllenamentoGiornaliero>>();
      const allenamentoGiornaliero = { id: 123 };
      jest.spyOn(allenamentoGiornalieroService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ allenamentoGiornaliero });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(allenamentoGiornalieroService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareSchedaSettimanale', () => {
      it('Should forward to schedaSettimanaleService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(schedaSettimanaleService, 'compareSchedaSettimanale');
        comp.compareSchedaSettimanale(entity, entity2);
        expect(schedaSettimanaleService.compareSchedaSettimanale).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
