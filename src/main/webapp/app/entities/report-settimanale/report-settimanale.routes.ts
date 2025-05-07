import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ReportSettimanaleResolve from './route/report-settimanale-routing-resolve.service';

const reportSettimanaleRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/report-settimanale.component').then(m => m.ReportSettimanaleComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/report-settimanale-detail.component').then(m => m.ReportSettimanaleDetailComponent),
    resolve: {
      reportSettimanale: ReportSettimanaleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/report-settimanale-update.component').then(m => m.ReportSettimanaleUpdateComponent),
    resolve: {
      reportSettimanale: ReportSettimanaleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/report-settimanale-update.component').then(m => m.ReportSettimanaleUpdateComponent),
    resolve: {
      reportSettimanale: ReportSettimanaleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default reportSettimanaleRoute;
