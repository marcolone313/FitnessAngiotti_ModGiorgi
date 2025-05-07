import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import SchedaSettimanaleResolve from './route/scheda-settimanale-routing-resolve.service';

const schedaSettimanaleRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/scheda-settimanale.component').then(m => m.SchedaSettimanaleComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/scheda-settimanale-detail.component').then(m => m.SchedaSettimanaleDetailComponent),
    resolve: {
      schedaSettimanale: SchedaSettimanaleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/scheda-settimanale-update.component').then(m => m.SchedaSettimanaleUpdateComponent),
    resolve: {
      schedaSettimanale: SchedaSettimanaleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/scheda-settimanale-update.component').then(m => m.SchedaSettimanaleUpdateComponent),
    resolve: {
      schedaSettimanale: SchedaSettimanaleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default schedaSettimanaleRoute;
