import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { CirconferenzaDetailComponent } from './circonferenza-detail.component';

describe('Circonferenza Management Detail Component', () => {
  let comp: CirconferenzaDetailComponent;
  let fixture: ComponentFixture<CirconferenzaDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CirconferenzaDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./circonferenza-detail.component').then(m => m.CirconferenzaDetailComponent),
              resolve: { circonferenza: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(CirconferenzaDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CirconferenzaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load circonferenza on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CirconferenzaDetailComponent);

      // THEN
      expect(instance.circonferenza()).toEqual(expect.objectContaining({ id: 123 }));
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
