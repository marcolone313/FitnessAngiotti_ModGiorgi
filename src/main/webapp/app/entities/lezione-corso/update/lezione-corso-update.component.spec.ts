import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ICorsoAcademy } from 'app/entities/corso-academy/corso-academy.model';
import { CorsoAcademyService } from 'app/entities/corso-academy/service/corso-academy.service';
import { LezioneCorsoService } from '../service/lezione-corso.service';
import { ILezioneCorso } from '../lezione-corso.model';
import { LezioneCorsoFormService } from './lezione-corso-form.service';

import { LezioneCorsoUpdateComponent } from './lezione-corso-update.component';

describe('LezioneCorso Management Update Component', () => {
  let comp: LezioneCorsoUpdateComponent;
  let fixture: ComponentFixture<LezioneCorsoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let lezioneCorsoFormService: LezioneCorsoFormService;
  let lezioneCorsoService: LezioneCorsoService;
  let corsoAcademyService: CorsoAcademyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [LezioneCorsoUpdateComponent],
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
      .overrideTemplate(LezioneCorsoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LezioneCorsoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    lezioneCorsoFormService = TestBed.inject(LezioneCorsoFormService);
    lezioneCorsoService = TestBed.inject(LezioneCorsoService);
    corsoAcademyService = TestBed.inject(CorsoAcademyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call CorsoAcademy query and add missing value', () => {
      const lezioneCorso: ILezioneCorso = { id: 456 };
      const corsoAcademy: ICorsoAcademy = { id: 28437 };
      lezioneCorso.corsoAcademy = corsoAcademy;

      const corsoAcademyCollection: ICorsoAcademy[] = [{ id: 28447 }];
      jest.spyOn(corsoAcademyService, 'query').mockReturnValue(of(new HttpResponse({ body: corsoAcademyCollection })));
      const additionalCorsoAcademies = [corsoAcademy];
      const expectedCollection: ICorsoAcademy[] = [...additionalCorsoAcademies, ...corsoAcademyCollection];
      jest.spyOn(corsoAcademyService, 'addCorsoAcademyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ lezioneCorso });
      comp.ngOnInit();

      expect(corsoAcademyService.query).toHaveBeenCalled();
      expect(corsoAcademyService.addCorsoAcademyToCollectionIfMissing).toHaveBeenCalledWith(
        corsoAcademyCollection,
        ...additionalCorsoAcademies.map(expect.objectContaining),
      );
      expect(comp.corsoAcademiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const lezioneCorso: ILezioneCorso = { id: 456 };
      const corsoAcademy: ICorsoAcademy = { id: 6612 };
      lezioneCorso.corsoAcademy = corsoAcademy;

      activatedRoute.data = of({ lezioneCorso });
      comp.ngOnInit();

      expect(comp.corsoAcademiesSharedCollection).toContain(corsoAcademy);
      expect(comp.lezioneCorso).toEqual(lezioneCorso);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILezioneCorso>>();
      const lezioneCorso = { id: 123 };
      jest.spyOn(lezioneCorsoFormService, 'getLezioneCorso').mockReturnValue(lezioneCorso);
      jest.spyOn(lezioneCorsoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ lezioneCorso });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: lezioneCorso }));
      saveSubject.complete();

      // THEN
      expect(lezioneCorsoFormService.getLezioneCorso).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(lezioneCorsoService.update).toHaveBeenCalledWith(expect.objectContaining(lezioneCorso));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILezioneCorso>>();
      const lezioneCorso = { id: 123 };
      jest.spyOn(lezioneCorsoFormService, 'getLezioneCorso').mockReturnValue({ id: null });
      jest.spyOn(lezioneCorsoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ lezioneCorso: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: lezioneCorso }));
      saveSubject.complete();

      // THEN
      expect(lezioneCorsoFormService.getLezioneCorso).toHaveBeenCalled();
      expect(lezioneCorsoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILezioneCorso>>();
      const lezioneCorso = { id: 123 };
      jest.spyOn(lezioneCorsoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ lezioneCorso });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(lezioneCorsoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCorsoAcademy', () => {
      it('Should forward to corsoAcademyService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(corsoAcademyService, 'compareCorsoAcademy');
        comp.compareCorsoAcademy(entity, entity2);
        expect(corsoAcademyService.compareCorsoAcademy).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
