import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ClienteToLezioneCorsoDetailComponent } from './cliente-to-lezione-corso-detail.component';

describe('ClienteToLezioneCorso Management Detail Component', () => {
  let comp: ClienteToLezioneCorsoDetailComponent;
  let fixture: ComponentFixture<ClienteToLezioneCorsoDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClienteToLezioneCorsoDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./cliente-to-lezione-corso-detail.component').then(m => m.ClienteToLezioneCorsoDetailComponent),
              resolve: { clienteToLezioneCorso: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ClienteToLezioneCorsoDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ClienteToLezioneCorsoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load clienteToLezioneCorso on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ClienteToLezioneCorsoDetailComponent);

      // THEN
      expect(instance.clienteToLezioneCorso()).toEqual(expect.objectContaining({ id: 123 }));
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
