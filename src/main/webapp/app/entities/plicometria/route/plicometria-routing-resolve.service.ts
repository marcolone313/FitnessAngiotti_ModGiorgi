import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPlicometria } from '../plicometria.model';
import { PlicometriaService } from '../service/plicometria.service';

const plicometriaResolve = (route: ActivatedRouteSnapshot): Observable<null | IPlicometria> => {
  const id = route.params.id;
  if (id) {
    return inject(PlicometriaService)
      .find(id)
      .pipe(
        mergeMap((plicometria: HttpResponse<IPlicometria>) => {
          if (plicometria.body) {
            return of(plicometria.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default plicometriaResolve;
