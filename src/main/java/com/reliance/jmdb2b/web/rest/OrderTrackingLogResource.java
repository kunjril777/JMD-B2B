package com.reliance.jmdb2b.web.rest;

import com.reliance.jmdb2b.domain.OrderTrackingLog;
import com.reliance.jmdb2b.repository.OrderTrackingLogRepository;
import com.reliance.jmdb2b.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.reliance.jmdb2b.domain.OrderTrackingLog}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class OrderTrackingLogResource {

    private final Logger log = LoggerFactory.getLogger(OrderTrackingLogResource.class);

    private static final String ENTITY_NAME = "orderTrackingLog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrderTrackingLogRepository orderTrackingLogRepository;

    public OrderTrackingLogResource(OrderTrackingLogRepository orderTrackingLogRepository) {
        this.orderTrackingLogRepository = orderTrackingLogRepository;
    }

    /**
     * {@code POST  /order-tracking-logs} : Create a new orderTrackingLog.
     *
     * @param orderTrackingLog the orderTrackingLog to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderTrackingLog, or with status {@code 400 (Bad Request)} if the orderTrackingLog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/order-tracking-logs")
    public ResponseEntity<OrderTrackingLog> createOrderTrackingLog(@RequestBody OrderTrackingLog orderTrackingLog)
        throws URISyntaxException {
        log.debug("REST request to save OrderTrackingLog : {}", orderTrackingLog);
        if (orderTrackingLog.getId() != null) {
            throw new BadRequestAlertException("A new orderTrackingLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrderTrackingLog result = orderTrackingLogRepository.save(orderTrackingLog);
        return ResponseEntity
            .created(new URI("/api/order-tracking-logs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /order-tracking-logs/:id} : Updates an existing orderTrackingLog.
     *
     * @param id the id of the orderTrackingLog to save.
     * @param orderTrackingLog the orderTrackingLog to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderTrackingLog,
     * or with status {@code 400 (Bad Request)} if the orderTrackingLog is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderTrackingLog couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/order-tracking-logs/{id}")
    public ResponseEntity<OrderTrackingLog> updateOrderTrackingLog(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrderTrackingLog orderTrackingLog
    ) throws URISyntaxException {
        log.debug("REST request to update OrderTrackingLog : {}, {}", id, orderTrackingLog);
        if (orderTrackingLog.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderTrackingLog.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderTrackingLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrderTrackingLog result = orderTrackingLogRepository.save(orderTrackingLog);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, orderTrackingLog.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /order-tracking-logs/:id} : Partial updates given fields of an existing orderTrackingLog, field will ignore if it is null
     *
     * @param id the id of the orderTrackingLog to save.
     * @param orderTrackingLog the orderTrackingLog to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderTrackingLog,
     * or with status {@code 400 (Bad Request)} if the orderTrackingLog is not valid,
     * or with status {@code 404 (Not Found)} if the orderTrackingLog is not found,
     * or with status {@code 500 (Internal Server Error)} if the orderTrackingLog couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/order-tracking-logs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrderTrackingLog> partialUpdateOrderTrackingLog(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrderTrackingLog orderTrackingLog
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrderTrackingLog partially : {}, {}", id, orderTrackingLog);
        if (orderTrackingLog.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderTrackingLog.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderTrackingLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrderTrackingLog> result = orderTrackingLogRepository
            .findById(orderTrackingLog.getId())
            .map(existingOrderTrackingLog -> {
                if (orderTrackingLog.getDescription() != null) {
                    existingOrderTrackingLog.setDescription(orderTrackingLog.getDescription());
                }
                if (orderTrackingLog.getOrderTrackingStatus() != null) {
                    existingOrderTrackingLog.setOrderTrackingStatus(orderTrackingLog.getOrderTrackingStatus());
                }
                if (orderTrackingLog.getCreatedTime() != null) {
                    existingOrderTrackingLog.setCreatedTime(orderTrackingLog.getCreatedTime());
                }
                if (orderTrackingLog.getUpdatedTime() != null) {
                    existingOrderTrackingLog.setUpdatedTime(orderTrackingLog.getUpdatedTime());
                }
                if (orderTrackingLog.getCreatedBy() != null) {
                    existingOrderTrackingLog.setCreatedBy(orderTrackingLog.getCreatedBy());
                }
                if (orderTrackingLog.getUpdatedBy() != null) {
                    existingOrderTrackingLog.setUpdatedBy(orderTrackingLog.getUpdatedBy());
                }

                return existingOrderTrackingLog;
            })
            .map(orderTrackingLogRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, orderTrackingLog.getId().toString())
        );
    }

    /**
     * {@code GET  /order-tracking-logs} : get all the orderTrackingLogs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orderTrackingLogs in body.
     */
    @GetMapping("/order-tracking-logs")
    public List<OrderTrackingLog> getAllOrderTrackingLogs() {
        log.debug("REST request to get all OrderTrackingLogs");
        return orderTrackingLogRepository.findAll();
    }

    /**
     * {@code GET  /order-tracking-logs/:id} : get the "id" orderTrackingLog.
     *
     * @param id the id of the orderTrackingLog to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderTrackingLog, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/order-tracking-logs/{id}")
    public ResponseEntity<OrderTrackingLog> getOrderTrackingLog(@PathVariable Long id) {
        log.debug("REST request to get OrderTrackingLog : {}", id);
        Optional<OrderTrackingLog> orderTrackingLog = orderTrackingLogRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(orderTrackingLog);
    }

    /**
     * {@code DELETE  /order-tracking-logs/:id} : delete the "id" orderTrackingLog.
     *
     * @param id the id of the orderTrackingLog to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/order-tracking-logs/{id}")
    public ResponseEntity<Void> deleteOrderTrackingLog(@PathVariable Long id) {
        log.debug("REST request to delete OrderTrackingLog : {}", id);
        orderTrackingLogRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
