import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import AllenamentoGiornalieroResolve from './route/allenamento-giornaliero-routing-resolve.service';

const allenamentoGiornalieroRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/allenamento-giornaliero.component').then(m => m.AllenamentoGiornalieroComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/allenamento-giornaliero-detail.component').then(m => m.AllenamentoGiornalieroDetailComponent),
    resolve: {
      allenamentoGiornaliero: AllenamentoGiornalieroResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/allenamento-giornaliero-update.component').then(m => m.AllenamentoGiornalieroUpdateComponent),
    resolve: {
      allenamentoGiornaliero: AllenamentoGiornalieroResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/allenamento-giornaliero-update.component').then(m => m.AllenamentoGiornalieroUpdateComponent),
    resolve: {
      allenamentoGiornaliero: AllenamentoGiornalieroResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default allenamentoGiornalieroRoute;
