import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { CorsoAcademyService } from '../service/corso-academy.service';
import { ICorsoAcademy } from '../corso-academy.model';
import { CorsoAcademyFormService } from './corso-academy-form.service';

import { CorsoAcademyUpdateComponent } from './corso-academy-update.component';

describe('CorsoAcademy Management Update Component', () => {
  let comp: CorsoAcademyUpdateComponent;
  let fixture: ComponentFixture<CorsoAcademyUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let corsoAcademyFormService: CorsoAcademyFormService;
  let corsoAcademyService: CorsoAcademyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CorsoAcademyUpdateComponent],
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
      .overrideTemplate(CorsoAcademyUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CorsoAcademyUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    corsoAcademyFormService = TestBed.inject(CorsoAcademyFormService);
    corsoAcademyService = TestBed.inject(CorsoAcademyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const corsoAcademy: ICorsoAcademy = { id: 456 };

      activatedRoute.data = of({ corsoAcademy });
      comp.ngOnInit();

      expect(comp.corsoAcademy).toEqual(corsoAcademy);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICorsoAcademy>>();
      const corsoAcademy = { id: 123 };
      jest.spyOn(corsoAcademyFormService, 'getCorsoAcademy').mockReturnValue(corsoAcademy);
      jest.spyOn(corsoAcademyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ corsoAcademy });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: corsoAcademy }));
      saveSubject.complete();

      // THEN
      expect(corsoAcademyFormService.getCorsoAcademy).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(corsoAcademyService.update).toHaveBeenCalledWith(expect.objectContaining(corsoAcademy));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICorsoAcademy>>();
      const corsoAcademy = { id: 123 };
      jest.spyOn(corsoAcademyFormService, 'getCorsoAcademy').mockReturnValue({ id: null });
      jest.spyOn(corsoAcademyService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ corsoAcademy: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: corsoAcademy }));
      saveSubject.complete();

      // THEN
      expect(corsoAcademyFormService.getCorsoAcademy).toHaveBeenCalled();
      expect(corsoAcademyService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICorsoAcademy>>();
      const corsoAcademy = { id: 123 };
      jest.spyOn(corsoAcademyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ corsoAcademy });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(corsoAcademyService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
