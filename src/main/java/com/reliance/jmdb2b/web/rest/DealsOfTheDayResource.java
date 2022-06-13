package com.reliance.jmdb2b.web.rest;

import com.reliance.jmdb2b.domain.DealsOfTheDay;
import com.reliance.jmdb2b.repository.DealsOfTheDayRepository;
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
 * REST controller for managing {@link com.reliance.jmdb2b.domain.DealsOfTheDay}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DealsOfTheDayResource {

    private final Logger log = LoggerFactory.getLogger(DealsOfTheDayResource.class);

    private static final String ENTITY_NAME = "dealsOfTheDay";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DealsOfTheDayRepository dealsOfTheDayRepository;

    public DealsOfTheDayResource(DealsOfTheDayRepository dealsOfTheDayRepository) {
        this.dealsOfTheDayRepository = dealsOfTheDayRepository;
    }

    /**
     * {@code POST  /deals-of-the-days} : Create a new dealsOfTheDay.
     *
     * @param dealsOfTheDay the dealsOfTheDay to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dealsOfTheDay, or with status {@code 400 (Bad Request)} if the dealsOfTheDay has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/deals-of-the-days")
    public ResponseEntity<DealsOfTheDay> createDealsOfTheDay(@RequestBody DealsOfTheDay dealsOfTheDay) throws URISyntaxException {
        log.debug("REST request to save DealsOfTheDay : {}", dealsOfTheDay);
        if (dealsOfTheDay.getId() != null) {
            throw new BadRequestAlertException("A new dealsOfTheDay cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DealsOfTheDay result = dealsOfTheDayRepository.save(dealsOfTheDay);
        return ResponseEntity
            .created(new URI("/api/deals-of-the-days/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /deals-of-the-days/:id} : Updates an existing dealsOfTheDay.
     *
     * @param id the id of the dealsOfTheDay to save.
     * @param dealsOfTheDay the dealsOfTheDay to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dealsOfTheDay,
     * or with status {@code 400 (Bad Request)} if the dealsOfTheDay is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dealsOfTheDay couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/deals-of-the-days/{id}")
    public ResponseEntity<DealsOfTheDay> updateDealsOfTheDay(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DealsOfTheDay dealsOfTheDay
    ) throws URISyntaxException {
        log.debug("REST request to update DealsOfTheDay : {}, {}", id, dealsOfTheDay);
        if (dealsOfTheDay.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dealsOfTheDay.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dealsOfTheDayRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DealsOfTheDay result = dealsOfTheDayRepository.save(dealsOfTheDay);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dealsOfTheDay.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /deals-of-the-days/:id} : Partial updates given fields of an existing dealsOfTheDay, field will ignore if it is null
     *
     * @param id the id of the dealsOfTheDay to save.
     * @param dealsOfTheDay the dealsOfTheDay to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dealsOfTheDay,
     * or with status {@code 400 (Bad Request)} if the dealsOfTheDay is not valid,
     * or with status {@code 404 (Not Found)} if the dealsOfTheDay is not found,
     * or with status {@code 500 (Internal Server Error)} if the dealsOfTheDay couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/deals-of-the-days/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DealsOfTheDay> partialUpdateDealsOfTheDay(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DealsOfTheDay dealsOfTheDay
    ) throws URISyntaxException {
        log.debug("REST request to partial update DealsOfTheDay partially : {}, {}", id, dealsOfTheDay);
        if (dealsOfTheDay.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dealsOfTheDay.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dealsOfTheDayRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DealsOfTheDay> result = dealsOfTheDayRepository
            .findById(dealsOfTheDay.getId())
            .map(existingDealsOfTheDay -> {
                if (dealsOfTheDay.getProductId() != null) {
                    existingDealsOfTheDay.setProductId(dealsOfTheDay.getProductId());
                }
                if (dealsOfTheDay.getCategoryId() != null) {
                    existingDealsOfTheDay.setCategoryId(dealsOfTheDay.getCategoryId());
                }
                if (dealsOfTheDay.getDealStartTime() != null) {
                    existingDealsOfTheDay.setDealStartTime(dealsOfTheDay.getDealStartTime());
                }
                if (dealsOfTheDay.getDealEndTime() != null) {
                    existingDealsOfTheDay.setDealEndTime(dealsOfTheDay.getDealEndTime());
                }
                if (dealsOfTheDay.getPriority() != null) {
                    existingDealsOfTheDay.setPriority(dealsOfTheDay.getPriority());
                }
                if (dealsOfTheDay.getCreatedTime() != null) {
                    existingDealsOfTheDay.setCreatedTime(dealsOfTheDay.getCreatedTime());
                }
                if (dealsOfTheDay.getUpdatedTime() != null) {
                    existingDealsOfTheDay.setUpdatedTime(dealsOfTheDay.getUpdatedTime());
                }
                if (dealsOfTheDay.getCreatedBy() != null) {
                    existingDealsOfTheDay.setCreatedBy(dealsOfTheDay.getCreatedBy());
                }
                if (dealsOfTheDay.getUpdatedBy() != null) {
                    existingDealsOfTheDay.setUpdatedBy(dealsOfTheDay.getUpdatedBy());
                }

                return existingDealsOfTheDay;
            })
            .map(dealsOfTheDayRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dealsOfTheDay.getId().toString())
        );
    }

    /**
     * {@code GET  /deals-of-the-days} : get all the dealsOfTheDays.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dealsOfTheDays in body.
     */
    @GetMapping("/deals-of-the-days")
    public List<DealsOfTheDay> getAllDealsOfTheDays() {
        log.debug("REST request to get all DealsOfTheDays");
        return dealsOfTheDayRepository.findAll();
    }

    /**
     * {@code GET  /deals-of-the-days/:id} : get the "id" dealsOfTheDay.
     *
     * @param id the id of the dealsOfTheDay to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dealsOfTheDay, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/deals-of-the-days/{id}")
    public ResponseEntity<DealsOfTheDay> getDealsOfTheDay(@PathVariable Long id) {
        log.debug("REST request to get DealsOfTheDay : {}", id);
        Optional<DealsOfTheDay> dealsOfTheDay = dealsOfTheDayRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(dealsOfTheDay);
    }

    /**
     * {@code DELETE  /deals-of-the-days/:id} : delete the "id" dealsOfTheDay.
     *
     * @param id the id of the dealsOfTheDay to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/deals-of-the-days/{id}")
    public ResponseEntity<Void> deleteDealsOfTheDay(@PathVariable Long id) {
        log.debug("REST request to delete DealsOfTheDay : {}", id);
        dealsOfTheDayRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
