import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICorsa } from '../corsa.model';
import { CorsaService } from '../service/corsa.service';

const corsaResolve = (route: ActivatedRouteSnapshot): Observable<null | ICorsa> => {
  const id = route.params.id;
  if (id) {
    return inject(CorsaService)
      .find(id)
      .pipe(
        mergeMap((corsa: HttpResponse<ICorsa>) => {
          if (corsa.body) {
            return of(corsa.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default corsaResolve;
