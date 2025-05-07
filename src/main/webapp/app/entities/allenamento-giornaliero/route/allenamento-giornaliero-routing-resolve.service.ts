import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAllenamentoGiornaliero } from '../allenamento-giornaliero.model';
import { AllenamentoGiornalieroService } from '../service/allenamento-giornaliero.service';

const allenamentoGiornalieroResolve = (route: ActivatedRouteSnapshot): Observable<null | IAllenamentoGiornaliero> => {
  const id = route.params.id;
  if (id) {
    return inject(AllenamentoGiornalieroService)
      .find(id)
      .pipe(
        mergeMap((allenamentoGiornaliero: HttpResponse<IAllenamentoGiornaliero>) => {
          if (allenamentoGiornaliero.body) {
            return of(allenamentoGiornaliero.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default allenamentoGiornalieroResolve;
