import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ClienteToLezioneCorsoResolve from './route/cliente-to-lezione-corso-routing-resolve.service';

const clienteToLezioneCorsoRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/cliente-to-lezione-corso.component').then(m => m.ClienteToLezioneCorsoComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/cliente-to-lezione-corso-detail.component').then(m => m.ClienteToLezioneCorsoDetailComponent),
    resolve: {
      clienteToLezioneCorso: ClienteToLezioneCorsoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/cliente-to-lezione-corso-update.component').then(m => m.ClienteToLezioneCorsoUpdateComponent),
    resolve: {
      clienteToLezioneCorso: ClienteToLezioneCorsoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/cliente-to-lezione-corso-update.component').then(m => m.ClienteToLezioneCorsoUpdateComponent),
    resolve: {
      clienteToLezioneCorso: ClienteToLezioneCorsoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default clienteToLezioneCorsoRoute;
