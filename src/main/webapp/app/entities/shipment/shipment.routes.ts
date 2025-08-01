import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ShipmentResolve from './route/shipment-routing-resolve.service';

const shipmentRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/shipment.component').then(m => m.ShipmentComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/shipment-detail.component').then(m => m.ShipmentDetailComponent),
    resolve: {
      shipment: ShipmentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/shipment-update.component').then(m => m.ShipmentUpdateComponent),
    resolve: {
      shipment: ShipmentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/shipment-update.component').then(m => m.ShipmentUpdateComponent),
    resolve: {
      shipment: ShipmentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default shipmentRoute;
