import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISchedaSettimanale } from '../scheda-settimanale.model';
import { SchedaSettimanaleService } from '../service/scheda-settimanale.service';

const schedaSettimanaleResolve = (route: ActivatedRouteSnapshot): Observable<null | ISchedaSettimanale> => {
  const id = route.params.id;
  if (id) {
    return inject(SchedaSettimanaleService)
      .find(id)
      .pipe(
        mergeMap((schedaSettimanale: HttpResponse<ISchedaSettimanale>) => {
          if (schedaSettimanale.body) {
            return of(schedaSettimanale.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default schedaSettimanaleResolve;
