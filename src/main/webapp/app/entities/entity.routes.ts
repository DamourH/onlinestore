import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'storeApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'product',
    data: { pageTitle: 'storeApp.product.home.title' },
    loadChildren: () => import('./product/product.routes'),
  },
  {
    path: 'product-category',
    data: { pageTitle: 'storeApp.productCategory.home.title' },
    loadChildren: () => import('./product-category/product-category.routes'),
  },
  {
    path: 'customer',
    data: { pageTitle: 'storeApp.customer.home.title' },
    loadChildren: () => import('./customer/customer.routes'),
  },
  {
    path: 'product-order',
    data: { pageTitle: 'storeApp.productOrder.home.title' },
    loadChildren: () => import('./product-order/product-order.routes'),
  },
  {
    path: 'order-item',
    data: { pageTitle: 'storeApp.orderItem.home.title' },
    loadChildren: () => import('./order-item/order-item.routes'),
  },
  {
    path: 'invoice',
    data: { pageTitle: 'storeApp.invoice.home.title' },
    loadChildren: () => import('./invoice/invoice.routes'),
  },
  {
    path: 'shipment',
    data: { pageTitle: 'storeApp.shipment.home.title' },
    loadChildren: () => import('./shipment/shipment.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
