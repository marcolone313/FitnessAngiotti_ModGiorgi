import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IGym } from '../gym.model';
import { GymService } from '../service/gym.service';

const gymResolve = (route: ActivatedRouteSnapshot): Observable<null | IGym> => {
  const id = route.params.id;
  if (id) {
    return inject(GymService)
      .find(id)
      .pipe(
        mergeMap((gym: HttpResponse<IGym>) => {
          if (gym.body) {
            return of(gym.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default gymResolve;
