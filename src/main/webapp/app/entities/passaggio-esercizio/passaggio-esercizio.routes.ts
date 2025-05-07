import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import PassaggioEsercizioResolve from './route/passaggio-esercizio-routing-resolve.service';

const passaggioEsercizioRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/passaggio-esercizio.component').then(m => m.PassaggioEsercizioComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/passaggio-esercizio-detail.component').then(m => m.PassaggioEsercizioDetailComponent),
    resolve: {
      passaggioEsercizio: PassaggioEsercizioResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/passaggio-esercizio-update.component').then(m => m.PassaggioEsercizioUpdateComponent),
    resolve: {
      passaggioEsercizio: PassaggioEsercizioResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/passaggio-esercizio-update.component').then(m => m.PassaggioEsercizioUpdateComponent),
    resolve: {
      passaggioEsercizio: PassaggioEsercizioResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default passaggioEsercizioRoute;
