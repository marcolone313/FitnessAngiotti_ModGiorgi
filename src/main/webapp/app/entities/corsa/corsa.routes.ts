import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import CorsaResolve from './route/corsa-routing-resolve.service';

const corsaRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/corsa.component').then(m => m.CorsaComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/corsa-detail.component').then(m => m.CorsaDetailComponent),
    resolve: {
      corsa: CorsaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/corsa-update.component').then(m => m.CorsaUpdateComponent),
    resolve: {
      corsa: CorsaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/corsa-update.component').then(m => m.CorsaUpdateComponent),
    resolve: {
      corsa: CorsaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default corsaRoute;
