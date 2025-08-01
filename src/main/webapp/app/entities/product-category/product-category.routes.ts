import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ProductCategoryResolve from './route/product-category-routing-resolve.service';

const productCategoryRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/product-category.component').then(m => m.ProductCategoryComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/product-category-detail.component').then(m => m.ProductCategoryDetailComponent),
    resolve: {
      productCategory: ProductCategoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/product-category-update.component').then(m => m.ProductCategoryUpdateComponent),
    resolve: {
      productCategory: ProductCategoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/product-category-update.component').then(m => m.ProductCategoryUpdateComponent),
    resolve: {
      productCategory: ProductCategoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default productCategoryRoute;
