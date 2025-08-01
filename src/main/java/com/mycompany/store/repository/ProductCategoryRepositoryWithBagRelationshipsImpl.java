package com.mycompany.store.repository;

import com.mycompany.store.domain.ProductCategory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class ProductCategoryRepositoryWithBagRelationshipsImpl implements ProductCategoryRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String PRODUCTCATEGORIES_PARAMETER = "productCategories";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<ProductCategory> fetchBagRelationships(Optional<ProductCategory> productCategory) {
        return productCategory.map(this::fetchProducts);
    }

    @Override
    public Page<ProductCategory> fetchBagRelationships(Page<ProductCategory> productCategories) {
        return new PageImpl<>(
            fetchBagRelationships(productCategories.getContent()),
            productCategories.getPageable(),
            productCategories.getTotalElements()
        );
    }

    @Override
    public List<ProductCategory> fetchBagRelationships(List<ProductCategory> productCategories) {
        return Optional.of(productCategories).map(this::fetchProducts).orElse(Collections.emptyList());
    }

    ProductCategory fetchProducts(ProductCategory result) {
        return entityManager
            .createQuery(
                "select productCategory from ProductCategory productCategory left join fetch productCategory.products where productCategory.id = :id",
                ProductCategory.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<ProductCategory> fetchProducts(List<ProductCategory> productCategories) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, productCategories.size()).forEach(index -> order.put(productCategories.get(index).getId(), index));
        List<ProductCategory> result = entityManager
            .createQuery(
                "select productCategory from ProductCategory productCategory left join fetch productCategory.products where productCategory in :productCategories",
                ProductCategory.class
            )
            .setParameter(PRODUCTCATEGORIES_PARAMETER, productCategories)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
