import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPesoCliente } from '../peso-cliente.model';
import { PesoClienteService } from '../service/peso-cliente.service';

const pesoClienteResolve = (route: ActivatedRouteSnapshot): Observable<null | IPesoCliente> => {
  const id = route.params.id;
  if (id) {
    return inject(PesoClienteService)
      .find(id)
      .pipe(
        mergeMap((pesoCliente: HttpResponse<IPesoCliente>) => {
          if (pesoCliente.body) {
            return of(pesoCliente.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default pesoClienteResolve;
