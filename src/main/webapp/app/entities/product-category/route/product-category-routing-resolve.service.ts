import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProductCategory } from '../product-category.model';
import { ProductCategoryService } from '../service/product-category.service';

const productCategoryResolve = (route: ActivatedRouteSnapshot): Observable<null | IProductCategory> => {
  const id = route.params.id;
  if (id) {
    return inject(ProductCategoryService)
      .find(id)
      .pipe(
        mergeMap((productCategory: HttpResponse<IProductCategory>) => {
          if (productCategory.body) {
            return of(productCategory.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default productCategoryResolve;
