import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICircuitoToEsercizio } from '../circuito-to-esercizio.model';
import { CircuitoToEsercizioService } from '../service/circuito-to-esercizio.service';

const circuitoToEsercizioResolve = (route: ActivatedRouteSnapshot): Observable<null | ICircuitoToEsercizio> => {
  const id = route.params.id;
  if (id) {
    return inject(CircuitoToEsercizioService)
      .find(id)
      .pipe(
        mergeMap((circuitoToEsercizio: HttpResponse<ICircuitoToEsercizio>) => {
          if (circuitoToEsercizio.body) {
            return of(circuitoToEsercizio.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default circuitoToEsercizioResolve;
