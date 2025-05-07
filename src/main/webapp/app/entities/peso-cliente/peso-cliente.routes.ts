import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import PesoClienteResolve from './route/peso-cliente-routing-resolve.service';

const pesoClienteRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/peso-cliente.component').then(m => m.PesoClienteComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/peso-cliente-detail.component').then(m => m.PesoClienteDetailComponent),
    resolve: {
      pesoCliente: PesoClienteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/peso-cliente-update.component').then(m => m.PesoClienteUpdateComponent),
    resolve: {
      pesoCliente: PesoClienteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/peso-cliente-update.component').then(m => m.PesoClienteUpdateComponent),
    resolve: {
      pesoCliente: PesoClienteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default pesoClienteRoute;
