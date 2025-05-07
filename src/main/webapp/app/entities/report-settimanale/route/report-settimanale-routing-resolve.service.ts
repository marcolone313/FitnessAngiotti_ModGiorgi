import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IReportSettimanale } from '../report-settimanale.model';
import { ReportSettimanaleService } from '../service/report-settimanale.service';

const reportSettimanaleResolve = (route: ActivatedRouteSnapshot): Observable<null | IReportSettimanale> => {
  const id = route.params.id;
  if (id) {
    return inject(ReportSettimanaleService)
      .find(id)
      .pipe(
        mergeMap((reportSettimanale: HttpResponse<IReportSettimanale>) => {
          if (reportSettimanale.body) {
            return of(reportSettimanale.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default reportSettimanaleResolve;
