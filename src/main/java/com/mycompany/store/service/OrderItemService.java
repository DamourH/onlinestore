package com.mycompany.store.service;

import com.mycompany.store.domain.OrderItem;
import com.mycompany.store.repository.OrderItemRepository;
import com.mycompany.store.repository.search.OrderItemSearchRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.store.domain.OrderItem}.
 */
@Service
@Transactional
public class OrderItemService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderItemService.class);

    private final OrderItemRepository orderItemRepository;

    private final OrderItemSearchRepository orderItemSearchRepository;

    public OrderItemService(OrderItemRepository orderItemRepository, OrderItemSearchRepository orderItemSearchRepository) {
        this.orderItemRepository = orderItemRepository;
        this.orderItemSearchRepository = orderItemSearchRepository;
    }

    /**
     * Save a orderItem.
     *
     * @param orderItem the entity to save.
     * @return the persisted entity.
     */
    public OrderItem save(OrderItem orderItem) {
        LOG.debug("Request to save OrderItem : {}", orderItem);
        orderItem = orderItemRepository.save(orderItem);
        orderItemSearchRepository.index(orderItem);
        return orderItem;
    }

    /**
     * Update a orderItem.
     *
     * @param orderItem the entity to save.
     * @return the persisted entity.
     */
    public OrderItem update(OrderItem orderItem) {
        LOG.debug("Request to update OrderItem : {}", orderItem);
        orderItem = orderItemRepository.save(orderItem);
        orderItemSearchRepository.index(orderItem);
        return orderItem;
    }

    /**
     * Partially update a orderItem.
     *
     * @param orderItem the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrderItem> partialUpdate(OrderItem orderItem) {
        LOG.debug("Request to partially update OrderItem : {}", orderItem);

        return orderItemRepository
            .findById(orderItem.getId())
            .map(existingOrderItem -> {
                if (orderItem.getQuantity() != null) {
                    existingOrderItem.setQuantity(orderItem.getQuantity());
                }
                if (orderItem.getTotalPrice() != null) {
                    existingOrderItem.setTotalPrice(orderItem.getTotalPrice());
                }
                if (orderItem.getStatus() != null) {
                    existingOrderItem.setStatus(orderItem.getStatus());
                }

                return existingOrderItem;
            })
            .map(orderItemRepository::save)
            .map(savedOrderItem -> {
                orderItemSearchRepository.index(savedOrderItem);
                return savedOrderItem;
            });
    }

    /**
     * Get all the orderItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<OrderItem> findAll(Pageable pageable) {
        LOG.debug("Request to get all OrderItems");
        return orderItemRepository.findAll(pageable);
    }

    /**
     * Get one orderItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderItem> findOne(Long id) {
        LOG.debug("Request to get OrderItem : {}", id);
        return orderItemRepository.findById(id);
    }

    /**
     * Delete the orderItem by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete OrderItem : {}", id);
        orderItemRepository.deleteById(id);
        orderItemSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the orderItem corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<OrderItem> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of OrderItems for query {}", query);
        return orderItemSearchRepository.search(query, pageable);
    }
}
