import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { SchedaSettimanaleDetailComponent } from './scheda-settimanale-detail.component';

describe('SchedaSettimanale Management Detail Component', () => {
  let comp: SchedaSettimanaleDetailComponent;
  let fixture: ComponentFixture<SchedaSettimanaleDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SchedaSettimanaleDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./scheda-settimanale-detail.component').then(m => m.SchedaSettimanaleDetailComponent),
              resolve: { schedaSettimanale: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(SchedaSettimanaleDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SchedaSettimanaleDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load schedaSettimanale on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', SchedaSettimanaleDetailComponent);

      // THEN
      expect(instance.schedaSettimanale()).toEqual(expect.objectContaining({ id: 123 }));
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
