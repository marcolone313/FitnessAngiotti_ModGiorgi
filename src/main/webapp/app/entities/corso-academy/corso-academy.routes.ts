import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import CorsoAcademyResolve from './route/corso-academy-routing-resolve.service';

const corsoAcademyRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/corso-academy.component').then(m => m.CorsoAcademyComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/corso-academy-detail.component').then(m => m.CorsoAcademyDetailComponent),
    resolve: {
      corsoAcademy: CorsoAcademyResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/corso-academy-update.component').then(m => m.CorsoAcademyUpdateComponent),
    resolve: {
      corsoAcademy: CorsoAcademyResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/corso-academy-update.component').then(m => m.CorsoAcademyUpdateComponent),
    resolve: {
      corsoAcademy: CorsoAcademyResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default corsoAcademyRoute;
