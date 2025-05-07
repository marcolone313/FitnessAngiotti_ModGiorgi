import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { CircuitoToEsercizioDetailComponent } from './circuito-to-esercizio-detail.component';

describe('CircuitoToEsercizio Management Detail Component', () => {
  let comp: CircuitoToEsercizioDetailComponent;
  let fixture: ComponentFixture<CircuitoToEsercizioDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CircuitoToEsercizioDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./circuito-to-esercizio-detail.component').then(m => m.CircuitoToEsercizioDetailComponent),
              resolve: { circuitoToEsercizio: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(CircuitoToEsercizioDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CircuitoToEsercizioDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load circuitoToEsercizio on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CircuitoToEsercizioDetailComponent);

      // THEN
      expect(instance.circuitoToEsercizio()).toEqual(expect.objectContaining({ id: 123 }));
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
