import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { PlicometriaDetailComponent } from './plicometria-detail.component';

describe('Plicometria Management Detail Component', () => {
  let comp: PlicometriaDetailComponent;
  let fixture: ComponentFixture<PlicometriaDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PlicometriaDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./plicometria-detail.component').then(m => m.PlicometriaDetailComponent),
              resolve: { plicometria: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(PlicometriaDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PlicometriaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load plicometria on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PlicometriaDetailComponent);

      // THEN
      expect(instance.plicometria()).toEqual(expect.objectContaining({ id: 123 }));
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
