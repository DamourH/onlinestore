package com.mycompany.store.repository;

import com.mycompany.store.domain.ProductCategory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface ProductCategoryRepositoryWithBagRelationships {
    Optional<ProductCategory> fetchBagRelationships(Optional<ProductCategory> productCategory);

    List<ProductCategory> fetchBagRelationships(List<ProductCategory> productCategories);

    Page<ProductCategory> fetchBagRelationships(Page<ProductCategory> productCategories);
}
