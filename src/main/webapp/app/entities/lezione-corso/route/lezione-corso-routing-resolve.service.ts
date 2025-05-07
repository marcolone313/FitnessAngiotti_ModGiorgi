import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILezioneCorso } from '../lezione-corso.model';
import { LezioneCorsoService } from '../service/lezione-corso.service';

const lezioneCorsoResolve = (route: ActivatedRouteSnapshot): Observable<null | ILezioneCorso> => {
  const id = route.params.id;
  if (id) {
    return inject(LezioneCorsoService)
      .find(id)
      .pipe(
        mergeMap((lezioneCorso: HttpResponse<ILezioneCorso>) => {
          if (lezioneCorso.body) {
            return of(lezioneCorso.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default lezioneCorsoResolve;
