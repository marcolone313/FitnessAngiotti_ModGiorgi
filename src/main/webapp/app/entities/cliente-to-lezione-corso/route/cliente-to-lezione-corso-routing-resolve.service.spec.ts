import { TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { ActivatedRoute, ActivatedRouteSnapshot, Router, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';

import { IClienteToLezioneCorso } from '../cliente-to-lezione-corso.model';
import { ClienteToLezioneCorsoService } from '../service/cliente-to-lezione-corso.service';

import clienteToLezioneCorsoResolve from './cliente-to-lezione-corso-routing-resolve.service';

describe('ClienteToLezioneCorso routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: ClienteToLezioneCorsoService;
  let resultClienteToLezioneCorso: IClienteToLezioneCorso | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    service = TestBed.inject(ClienteToLezioneCorsoService);
    resultClienteToLezioneCorso = undefined;
  });

  describe('resolve', () => {
    it('should return IClienteToLezioneCorso returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        clienteToLezioneCorsoResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultClienteToLezioneCorso = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith(123);
      expect(resultClienteToLezioneCorso).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        clienteToLezioneCorsoResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultClienteToLezioneCorso = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toHaveBeenCalled();
      expect(resultClienteToLezioneCorso).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IClienteToLezioneCorso>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        clienteToLezioneCorsoResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultClienteToLezioneCorso = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith(123);
      expect(resultClienteToLezioneCorso).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
