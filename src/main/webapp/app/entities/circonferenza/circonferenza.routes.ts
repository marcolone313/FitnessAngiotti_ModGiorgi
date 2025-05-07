import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import CirconferenzaResolve from './route/circonferenza-routing-resolve.service';

const circonferenzaRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/circonferenza.component').then(m => m.CirconferenzaComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/circonferenza-detail.component').then(m => m.CirconferenzaDetailComponent),
    resolve: {
      circonferenza: CirconferenzaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/circonferenza-update.component').then(m => m.CirconferenzaUpdateComponent),
    resolve: {
      circonferenza: CirconferenzaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/circonferenza-update.component').then(m => m.CirconferenzaUpdateComponent),
    resolve: {
      circonferenza: CirconferenzaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default circonferenzaRoute;
