package com.mycompany.store.service;

import com.mycompany.store.domain.ProductOrder;
import com.mycompany.store.repository.ProductOrderRepository;
import com.mycompany.store.repository.search.ProductOrderSearchRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.store.domain.ProductOrder}.
 */
@Service
@Transactional
public class ProductOrderService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductOrderService.class);

    private final ProductOrderRepository productOrderRepository;

    private final ProductOrderSearchRepository productOrderSearchRepository;

    public ProductOrderService(ProductOrderRepository productOrderRepository, ProductOrderSearchRepository productOrderSearchRepository) {
        this.productOrderRepository = productOrderRepository;
        this.productOrderSearchRepository = productOrderSearchRepository;
    }

    /**
     * Save a productOrder.
     *
     * @param productOrder the entity to save.
     * @return the persisted entity.
     */
    public ProductOrder save(ProductOrder productOrder) {
        LOG.debug("Request to save ProductOrder : {}", productOrder);
        productOrder = productOrderRepository.save(productOrder);
        productOrderSearchRepository.index(productOrder);
        return productOrder;
    }

    /**
     * Update a productOrder.
     *
     * @param productOrder the entity to save.
     * @return the persisted entity.
     */
    public ProductOrder update(ProductOrder productOrder) {
        LOG.debug("Request to update ProductOrder : {}", productOrder);
        productOrder = productOrderRepository.save(productOrder);
        productOrderSearchRepository.index(productOrder);
        return productOrder;
    }

    /**
     * Partially update a productOrder.
     *
     * @param productOrder the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductOrder> partialUpdate(ProductOrder productOrder) {
        LOG.debug("Request to partially update ProductOrder : {}", productOrder);

        return productOrderRepository
            .findById(productOrder.getId())
            .map(existingProductOrder -> {
                if (productOrder.getPlacedDate() != null) {
                    existingProductOrder.setPlacedDate(productOrder.getPlacedDate());
                }
                if (productOrder.getStatus() != null) {
                    existingProductOrder.setStatus(productOrder.getStatus());
                }
                if (productOrder.getCode() != null) {
                    existingProductOrder.setCode(productOrder.getCode());
                }

                return existingProductOrder;
            })
            .map(productOrderRepository::save)
            .map(savedProductOrder -> {
                productOrderSearchRepository.index(savedProductOrder);
                return savedProductOrder;
            });
    }

    /**
     * Get all the productOrders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductOrder> findAll(Pageable pageable) {
        LOG.debug("Request to get all ProductOrders");
        return productOrderRepository.findAll(pageable);
    }

    /**
     * Get one productOrder by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductOrder> findOne(Long id) {
        LOG.debug("Request to get ProductOrder : {}", id);
        return productOrderRepository.findById(id);
    }

    /**
     * Delete the productOrder by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProductOrder : {}", id);
        productOrderRepository.deleteById(id);
        productOrderSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the productOrder corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductOrder> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of ProductOrders for query {}", query);
        return productOrderSearchRepository.search(query, pageable);
    }
}
