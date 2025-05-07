import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICirconferenza } from '../circonferenza.model';
import { CirconferenzaService } from '../service/circonferenza.service';

const circonferenzaResolve = (route: ActivatedRouteSnapshot): Observable<null | ICirconferenza> => {
  const id = route.params.id;
  if (id) {
    return inject(CirconferenzaService)
      .find(id)
      .pipe(
        mergeMap((circonferenza: HttpResponse<ICirconferenza>) => {
          if (circonferenza.body) {
            return of(circonferenza.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default circonferenzaResolve;
