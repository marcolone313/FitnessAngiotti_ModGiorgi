import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICorsoAcademy } from '../corso-academy.model';
import { CorsoAcademyService } from '../service/corso-academy.service';

const corsoAcademyResolve = (route: ActivatedRouteSnapshot): Observable<null | ICorsoAcademy> => {
  const id = route.params.id;
  if (id) {
    return inject(CorsoAcademyService)
      .find(id)
      .pipe(
        mergeMap((corsoAcademy: HttpResponse<ICorsoAcademy>) => {
          if (corsoAcademy.body) {
            return of(corsoAcademy.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default corsoAcademyResolve;
