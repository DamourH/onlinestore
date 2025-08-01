package com.mycompany.store.service;

import com.mycompany.store.domain.Shipment;
import com.mycompany.store.repository.ShipmentRepository;
import com.mycompany.store.repository.search.ShipmentSearchRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.store.domain.Shipment}.
 */
@Service
@Transactional
public class ShipmentService {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentService.class);

    private final ShipmentRepository shipmentRepository;

    private final ShipmentSearchRepository shipmentSearchRepository;

    public ShipmentService(ShipmentRepository shipmentRepository, ShipmentSearchRepository shipmentSearchRepository) {
        this.shipmentRepository = shipmentRepository;
        this.shipmentSearchRepository = shipmentSearchRepository;
    }

    /**
     * Save a shipment.
     *
     * @param shipment the entity to save.
     * @return the persisted entity.
     */
    public Shipment save(Shipment shipment) {
        LOG.debug("Request to save Shipment : {}", shipment);
        shipment = shipmentRepository.save(shipment);
        shipmentSearchRepository.index(shipment);
        return shipment;
    }

    /**
     * Update a shipment.
     *
     * @param shipment the entity to save.
     * @return the persisted entity.
     */
    public Shipment update(Shipment shipment) {
        LOG.debug("Request to update Shipment : {}", shipment);
        shipment = shipmentRepository.save(shipment);
        shipmentSearchRepository.index(shipment);
        return shipment;
    }

    /**
     * Partially update a shipment.
     *
     * @param shipment the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Shipment> partialUpdate(Shipment shipment) {
        LOG.debug("Request to partially update Shipment : {}", shipment);

        return shipmentRepository
            .findById(shipment.getId())
            .map(existingShipment -> {
                if (shipment.getTrackingCode() != null) {
                    existingShipment.setTrackingCode(shipment.getTrackingCode());
                }
                if (shipment.getDate() != null) {
                    existingShipment.setDate(shipment.getDate());
                }
                if (shipment.getDetails() != null) {
                    existingShipment.setDetails(shipment.getDetails());
                }

                return existingShipment;
            })
            .map(shipmentRepository::save)
            .map(savedShipment -> {
                shipmentSearchRepository.index(savedShipment);
                return savedShipment;
            });
    }

    /**
     * Get all the shipments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Shipment> findAll(Pageable pageable) {
        LOG.debug("Request to get all Shipments");
        return shipmentRepository.findAll(pageable);
    }

    /**
     * Get one shipment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Shipment> findOne(Long id) {
        LOG.debug("Request to get Shipment : {}", id);
        return shipmentRepository.findById(id);
    }

    /**
     * Delete the shipment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Shipment : {}", id);
        shipmentRepository.deleteById(id);
        shipmentSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the shipment corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Shipment> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of Shipments for query {}", query);
        return shipmentSearchRepository.search(query, pageable);
    }
}
