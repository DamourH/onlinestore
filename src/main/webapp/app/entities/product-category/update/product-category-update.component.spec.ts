import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { ProductCategoryService } from '../service/product-category.service';
import { IProductCategory } from '../product-category.model';
import { ProductCategoryFormService } from './product-category-form.service';

import { ProductCategoryUpdateComponent } from './product-category-update.component';

describe('ProductCategory Management Update Component', () => {
  let comp: ProductCategoryUpdateComponent;
  let fixture: ComponentFixture<ProductCategoryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let productCategoryFormService: ProductCategoryFormService;
  let productCategoryService: ProductCategoryService;
  let productService: ProductService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ProductCategoryUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ProductCategoryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProductCategoryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productCategoryFormService = TestBed.inject(ProductCategoryFormService);
    productCategoryService = TestBed.inject(ProductCategoryService);
    productService = TestBed.inject(ProductService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Product query and add missing value', () => {
      const productCategory: IProductCategory = { id: 19244 };
      const products: IProduct[] = [{ id: 21536 }];
      productCategory.products = products;

      const productCollection: IProduct[] = [{ id: 21536 }];
      jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [...products];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productCategory });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(expect.objectContaining),
      );
      expect(comp.productsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const productCategory: IProductCategory = { id: 19244 };
      const product: IProduct = { id: 21536 };
      productCategory.products = [product];

      activatedRoute.data = of({ productCategory });
      comp.ngOnInit();

      expect(comp.productsSharedCollection).toContainEqual(product);
      expect(comp.productCategory).toEqual(productCategory);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductCategory>>();
      const productCategory = { id: 29286 };
      jest.spyOn(productCategoryFormService, 'getProductCategory').mockReturnValue(productCategory);
      jest.spyOn(productCategoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: productCategory }));
      saveSubject.complete();

      // THEN
      expect(productCategoryFormService.getProductCategory).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productCategoryService.update).toHaveBeenCalledWith(expect.objectContaining(productCategory));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductCategory>>();
      const productCategory = { id: 29286 };
      jest.spyOn(productCategoryFormService, 'getProductCategory').mockReturnValue({ id: null });
      jest.spyOn(productCategoryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productCategory: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: productCategory }));
      saveSubject.complete();

      // THEN
      expect(productCategoryFormService.getProductCategory).toHaveBeenCalled();
      expect(productCategoryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductCategory>>();
      const productCategory = { id: 29286 };
      jest.spyOn(productCategoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productCategoryService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareProduct', () => {
      it('should forward to productService', () => {
        const entity = { id: 21536 };
        const entity2 = { id: 11926 };
        jest.spyOn(productService, 'compareProduct');
        comp.compareProduct(entity, entity2);
        expect(productService.compareProduct).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
