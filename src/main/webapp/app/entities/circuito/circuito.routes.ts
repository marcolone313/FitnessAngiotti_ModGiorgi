import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import CircuitoResolve from './route/circuito-routing-resolve.service';

const circuitoRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/circuito.component').then(m => m.CircuitoComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/circuito-detail.component').then(m => m.CircuitoDetailComponent),
    resolve: {
      circuito: CircuitoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/circuito-update.component').then(m => m.CircuitoUpdateComponent),
    resolve: {
      circuito: CircuitoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/circuito-update.component').then(m => m.CircuitoUpdateComponent),
    resolve: {
      circuito: CircuitoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default circuitoRoute;
