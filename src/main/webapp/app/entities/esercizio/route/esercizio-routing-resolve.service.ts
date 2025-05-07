import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEsercizio } from '../esercizio.model';
import { EsercizioService } from '../service/esercizio.service';

const esercizioResolve = (route: ActivatedRouteSnapshot): Observable<null | IEsercizio> => {
  const id = route.params.id;
  if (id) {
    return inject(EsercizioService)
      .find(id)
      .pipe(
        mergeMap((esercizio: HttpResponse<IEsercizio>) => {
          if (esercizio.body) {
            return of(esercizio.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default esercizioResolve;
