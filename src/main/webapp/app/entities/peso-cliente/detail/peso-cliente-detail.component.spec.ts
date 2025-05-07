import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { PesoClienteDetailComponent } from './peso-cliente-detail.component';

describe('PesoCliente Management Detail Component', () => {
  let comp: PesoClienteDetailComponent;
  let fixture: ComponentFixture<PesoClienteDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PesoClienteDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./peso-cliente-detail.component').then(m => m.PesoClienteDetailComponent),
              resolve: { pesoCliente: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(PesoClienteDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PesoClienteDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load pesoCliente on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PesoClienteDetailComponent);

      // THEN
      expect(instance.pesoCliente()).toEqual(expect.objectContaining({ id: 123 }));
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
