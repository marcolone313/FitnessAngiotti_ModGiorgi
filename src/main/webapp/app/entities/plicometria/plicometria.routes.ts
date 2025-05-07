import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import PlicometriaResolve from './route/plicometria-routing-resolve.service';

const plicometriaRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/plicometria.component').then(m => m.PlicometriaComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/plicometria-detail.component').then(m => m.PlicometriaDetailComponent),
    resolve: {
      plicometria: PlicometriaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/plicometria-update.component').then(m => m.PlicometriaUpdateComponent),
    resolve: {
      plicometria: PlicometriaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/plicometria-update.component').then(m => m.PlicometriaUpdateComponent),
    resolve: {
      plicometria: PlicometriaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default plicometriaRoute;
