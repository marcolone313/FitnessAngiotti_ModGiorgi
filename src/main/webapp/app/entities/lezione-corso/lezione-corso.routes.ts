import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import LezioneCorsoResolve from './route/lezione-corso-routing-resolve.service';

const lezioneCorsoRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/lezione-corso.component').then(m => m.LezioneCorsoComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/lezione-corso-detail.component').then(m => m.LezioneCorsoDetailComponent),
    resolve: {
      lezioneCorso: LezioneCorsoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/lezione-corso-update.component').then(m => m.LezioneCorsoUpdateComponent),
    resolve: {
      lezioneCorso: LezioneCorsoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/lezione-corso-update.component').then(m => m.LezioneCorsoUpdateComponent),
    resolve: {
      lezioneCorso: LezioneCorsoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default lezioneCorsoRoute;
