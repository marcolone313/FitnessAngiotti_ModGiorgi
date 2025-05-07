import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPassaggioEsercizio } from '../passaggio-esercizio.model';
import { PassaggioEsercizioService } from '../service/passaggio-esercizio.service';

const passaggioEsercizioResolve = (route: ActivatedRouteSnapshot): Observable<null | IPassaggioEsercizio> => {
  const id = route.params.id;
  if (id) {
    return inject(PassaggioEsercizioService)
      .find(id)
      .pipe(
        mergeMap((passaggioEsercizio: HttpResponse<IPassaggioEsercizio>) => {
          if (passaggioEsercizio.body) {
            return of(passaggioEsercizio.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default passaggioEsercizioResolve;
