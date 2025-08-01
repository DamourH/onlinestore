package com.mycompany.store.service;

import com.mycompany.store.domain.ProductCategory;
import com.mycompany.store.repository.ProductCategoryRepository;
import com.mycompany.store.repository.search.ProductCategorySearchRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.store.domain.ProductCategory}.
 */
@Service
@Transactional
public class ProductCategoryService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductCategoryService.class);

    private final ProductCategoryRepository productCategoryRepository;

    private final ProductCategorySearchRepository productCategorySearchRepository;

    public ProductCategoryService(
        ProductCategoryRepository productCategoryRepository,
        ProductCategorySearchRepository productCategorySearchRepository
    ) {
        this.productCategoryRepository = productCategoryRepository;
        this.productCategorySearchRepository = productCategorySearchRepository;
    }

    /**
     * Save a productCategory.
     *
     * @param productCategory the entity to save.
     * @return the persisted entity.
     */
    public ProductCategory save(ProductCategory productCategory) {
        LOG.debug("Request to save ProductCategory : {}", productCategory);
        productCategory = productCategoryRepository.save(productCategory);
        productCategorySearchRepository.index(productCategory);
        return productCategory;
    }

    /**
     * Update a productCategory.
     *
     * @param productCategory the entity to save.
     * @return the persisted entity.
     */
    public ProductCategory update(ProductCategory productCategory) {
        LOG.debug("Request to update ProductCategory : {}", productCategory);
        productCategory = productCategoryRepository.save(productCategory);
        productCategorySearchRepository.index(productCategory);
        return productCategory;
    }

    /**
     * Partially update a productCategory.
     *
     * @param productCategory the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductCategory> partialUpdate(ProductCategory productCategory) {
        LOG.debug("Request to partially update ProductCategory : {}", productCategory);

        return productCategoryRepository
            .findById(productCategory.getId())
            .map(existingProductCategory -> {
                if (productCategory.getName() != null) {
                    existingProductCategory.setName(productCategory.getName());
                }
                if (productCategory.getDescription() != null) {
                    existingProductCategory.setDescription(productCategory.getDescription());
                }

                return existingProductCategory;
            })
            .map(productCategoryRepository::save)
            .map(savedProductCategory -> {
                productCategorySearchRepository.index(savedProductCategory);
                return savedProductCategory;
            });
    }

    /**
     * Get all the productCategories.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ProductCategory> findAll() {
        LOG.debug("Request to get all ProductCategories");
        return productCategoryRepository.findAll();
    }

    /**
     * Get all the productCategories with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ProductCategory> findAllWithEagerRelationships(Pageable pageable) {
        return productCategoryRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one productCategory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductCategory> findOne(Long id) {
        LOG.debug("Request to get ProductCategory : {}", id);
        return productCategoryRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the productCategory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProductCategory : {}", id);
        productCategoryRepository.deleteById(id);
        productCategorySearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the productCategory corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ProductCategory> search(String query) {
        LOG.debug("Request to search ProductCategories for query {}", query);
        try {
            return StreamSupport.stream(productCategorySearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw e;
        }
    }
}
