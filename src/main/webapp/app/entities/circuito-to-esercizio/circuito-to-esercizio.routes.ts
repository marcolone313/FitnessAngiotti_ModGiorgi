import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import CircuitoToEsercizioResolve from './route/circuito-to-esercizio-routing-resolve.service';

const circuitoToEsercizioRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/circuito-to-esercizio.component').then(m => m.CircuitoToEsercizioComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/circuito-to-esercizio-detail.component').then(m => m.CircuitoToEsercizioDetailComponent),
    resolve: {
      circuitoToEsercizio: CircuitoToEsercizioResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/circuito-to-esercizio-update.component').then(m => m.CircuitoToEsercizioUpdateComponent),
    resolve: {
      circuitoToEsercizio: CircuitoToEsercizioResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/circuito-to-esercizio-update.component').then(m => m.CircuitoToEsercizioUpdateComponent),
    resolve: {
      circuitoToEsercizio: CircuitoToEsercizioResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default circuitoToEsercizioRoute;
