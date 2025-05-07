import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import GymResolve from './route/gym-routing-resolve.service';

const gymRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/gym.component').then(m => m.GymComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/gym-detail.component').then(m => m.GymDetailComponent),
    resolve: {
      gym: GymResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/gym-update.component').then(m => m.GymUpdateComponent),
    resolve: {
      gym: GymResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/gym-update.component').then(m => m.GymUpdateComponent),
    resolve: {
      gym: GymResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default gymRoute;
