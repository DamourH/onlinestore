import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IProductCategory } from '../product-category.model';
import { ProductCategoryService } from '../service/product-category.service';
import { ProductCategoryFormGroup, ProductCategoryFormService } from './product-category-form.service';

@Component({
  selector: 'jhi-product-category-update',
  templateUrl: './product-category-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProductCategoryUpdateComponent implements OnInit {
  isSaving = false;
  productCategory: IProductCategory | null = null;

  productsSharedCollection: IProduct[] = [];

  protected productCategoryService = inject(ProductCategoryService);
  protected productCategoryFormService = inject(ProductCategoryFormService);
  protected productService = inject(ProductService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProductCategoryFormGroup = this.productCategoryFormService.createProductCategoryFormGroup();

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productCategory }) => {
      this.productCategory = productCategory;
      if (productCategory) {
        this.updateForm(productCategory);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const productCategory = this.productCategoryFormService.getProductCategory(this.editForm);
    if (productCategory.id !== null) {
      this.subscribeToSaveResponse(this.productCategoryService.update(productCategory));
    } else {
      this.subscribeToSaveResponse(this.productCategoryService.create(productCategory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProductCategory>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(productCategory: IProductCategory): void {
    this.productCategory = productCategory;
    this.productCategoryFormService.resetForm(this.editForm, productCategory);

    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing<IProduct>(
      this.productsSharedCollection,
      ...(productCategory.products ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) =>
          this.productService.addProductToCollectionIfMissing<IProduct>(products, ...(this.productCategory?.products ?? [])),
        ),
      )
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));
  }
}
