import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ProductOrderResolve from './route/product-order-routing-resolve.service';

const productOrderRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/product-order.component').then(m => m.ProductOrderComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/product-order-detail.component').then(m => m.ProductOrderDetailComponent),
    resolve: {
      productOrder: ProductOrderResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/product-order-update.component').then(m => m.ProductOrderUpdateComponent),
    resolve: {
      productOrder: ProductOrderResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/product-order-update.component').then(m => m.ProductOrderUpdateComponent),
    resolve: {
      productOrder: ProductOrderResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default productOrderRoute;
