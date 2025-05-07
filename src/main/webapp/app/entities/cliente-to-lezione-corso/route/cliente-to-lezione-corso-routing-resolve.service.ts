import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IClienteToLezioneCorso } from '../cliente-to-lezione-corso.model';
import { ClienteToLezioneCorsoService } from '../service/cliente-to-lezione-corso.service';

const clienteToLezioneCorsoResolve = (route: ActivatedRouteSnapshot): Observable<null | IClienteToLezioneCorso> => {
  const id = route.params.id;
  if (id) {
    return inject(ClienteToLezioneCorsoService)
      .find(id)
      .pipe(
        mergeMap((clienteToLezioneCorso: HttpResponse<IClienteToLezioneCorso>) => {
          if (clienteToLezioneCorso.body) {
            return of(clienteToLezioneCorso.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default clienteToLezioneCorsoResolve;
