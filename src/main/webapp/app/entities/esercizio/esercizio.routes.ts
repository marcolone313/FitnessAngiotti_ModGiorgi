import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import EsercizioResolve from './route/esercizio-routing-resolve.service';

const esercizioRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/esercizio.component').then(m => m.EsercizioComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/esercizio-detail.component').then(m => m.EsercizioDetailComponent),
    resolve: {
      esercizio: EsercizioResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/esercizio-update.component').then(m => m.EsercizioUpdateComponent),
    resolve: {
      esercizio: EsercizioResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/esercizio-update.component').then(m => m.EsercizioUpdateComponent),
    resolve: {
      esercizio: EsercizioResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default esercizioRoute;
