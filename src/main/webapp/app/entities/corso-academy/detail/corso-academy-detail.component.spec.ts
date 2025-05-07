import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { CorsoAcademyDetailComponent } from './corso-academy-detail.component';

describe('CorsoAcademy Management Detail Component', () => {
  let comp: CorsoAcademyDetailComponent;
  let fixture: ComponentFixture<CorsoAcademyDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CorsoAcademyDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./corso-academy-detail.component').then(m => m.CorsoAcademyDetailComponent),
              resolve: { corsoAcademy: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(CorsoAcademyDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CorsoAcademyDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load corsoAcademy on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CorsoAcademyDetailComponent);

      // THEN
      expect(instance.corsoAcademy()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
