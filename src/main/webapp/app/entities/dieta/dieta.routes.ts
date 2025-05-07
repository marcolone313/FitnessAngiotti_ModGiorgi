import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import DietaResolve from './route/dieta-routing-resolve.service';

const dietaRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/dieta.component').then(m => m.DietaComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/dieta-detail.component').then(m => m.DietaDetailComponent),
    resolve: {
      dieta: DietaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/dieta-update.component').then(m => m.DietaUpdateComponent),
    resolve: {
      dieta: DietaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/dieta-update.component').then(m => m.DietaUpdateComponent),
    resolve: {
      dieta: DietaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default dietaRoute;
