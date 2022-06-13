package com.reliance.jmdb2b.web.rest;

import com.reliance.jmdb2b.domain.RecommmendedItems;
import com.reliance.jmdb2b.repository.RecommmendedItemsRepository;
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
 * REST controller for managing {@link com.reliance.jmdb2b.domain.RecommmendedItems}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RecommmendedItemsResource {

    private final Logger log = LoggerFactory.getLogger(RecommmendedItemsResource.class);

    private static final String ENTITY_NAME = "recommmendedItems";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RecommmendedItemsRepository recommmendedItemsRepository;

    public RecommmendedItemsResource(RecommmendedItemsRepository recommmendedItemsRepository) {
        this.recommmendedItemsRepository = recommmendedItemsRepository;
    }

    /**
     * {@code POST  /recommmended-items} : Create a new recommmendedItems.
     *
     * @param recommmendedItems the recommmendedItems to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new recommmendedItems, or with status {@code 400 (Bad Request)} if the recommmendedItems has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/recommmended-items")
    public ResponseEntity<RecommmendedItems> createRecommmendedItems(@RequestBody RecommmendedItems recommmendedItems)
        throws URISyntaxException {
        log.debug("REST request to save RecommmendedItems : {}", recommmendedItems);
        if (recommmendedItems.getId() != null) {
            throw new BadRequestAlertException("A new recommmendedItems cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RecommmendedItems result = recommmendedItemsRepository.save(recommmendedItems);
        return ResponseEntity
            .created(new URI("/api/recommmended-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /recommmended-items/:id} : Updates an existing recommmendedItems.
     *
     * @param id the id of the recommmendedItems to save.
     * @param recommmendedItems the recommmendedItems to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recommmendedItems,
     * or with status {@code 400 (Bad Request)} if the recommmendedItems is not valid,
     * or with status {@code 500 (Internal Server Error)} if the recommmendedItems couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/recommmended-items/{id}")
    public ResponseEntity<RecommmendedItems> updateRecommmendedItems(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RecommmendedItems recommmendedItems
    ) throws URISyntaxException {
        log.debug("REST request to update RecommmendedItems : {}, {}", id, recommmendedItems);
        if (recommmendedItems.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recommmendedItems.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recommmendedItemsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RecommmendedItems result = recommmendedItemsRepository.save(recommmendedItems);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, recommmendedItems.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /recommmended-items/:id} : Partial updates given fields of an existing recommmendedItems, field will ignore if it is null
     *
     * @param id the id of the recommmendedItems to save.
     * @param recommmendedItems the recommmendedItems to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recommmendedItems,
     * or with status {@code 400 (Bad Request)} if the recommmendedItems is not valid,
     * or with status {@code 404 (Not Found)} if the recommmendedItems is not found,
     * or with status {@code 500 (Internal Server Error)} if the recommmendedItems couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/recommmended-items/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RecommmendedItems> partialUpdateRecommmendedItems(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RecommmendedItems recommmendedItems
    ) throws URISyntaxException {
        log.debug("REST request to partial update RecommmendedItems partially : {}, {}", id, recommmendedItems);
        if (recommmendedItems.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recommmendedItems.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recommmendedItemsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RecommmendedItems> result = recommmendedItemsRepository
            .findById(recommmendedItems.getId())
            .map(existingRecommmendedItems -> {
                if (recommmendedItems.getUserId() != null) {
                    existingRecommmendedItems.setUserId(recommmendedItems.getUserId());
                }
                if (recommmendedItems.getProductId() != null) {
                    existingRecommmendedItems.setProductId(recommmendedItems.getProductId());
                }
                if (recommmendedItems.getCategoryId() != null) {
                    existingRecommmendedItems.setCategoryId(recommmendedItems.getCategoryId());
                }
                if (recommmendedItems.getPriority() != null) {
                    existingRecommmendedItems.setPriority(recommmendedItems.getPriority());
                }
                if (recommmendedItems.getCreatedTime() != null) {
                    existingRecommmendedItems.setCreatedTime(recommmendedItems.getCreatedTime());
                }
                if (recommmendedItems.getUpdatedTime() != null) {
                    existingRecommmendedItems.setUpdatedTime(recommmendedItems.getUpdatedTime());
                }
                if (recommmendedItems.getCreatedBy() != null) {
                    existingRecommmendedItems.setCreatedBy(recommmendedItems.getCreatedBy());
                }
                if (recommmendedItems.getUpdatedBy() != null) {
                    existingRecommmendedItems.setUpdatedBy(recommmendedItems.getUpdatedBy());
                }

                return existingRecommmendedItems;
            })
            .map(recommmendedItemsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, recommmendedItems.getId().toString())
        );
    }

    /**
     * {@code GET  /recommmended-items} : get all the recommmendedItems.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of recommmendedItems in body.
     */
    @GetMapping("/recommmended-items")
    public List<RecommmendedItems> getAllRecommmendedItems() {
        log.debug("REST request to get all RecommmendedItems");
        return recommmendedItemsRepository.findAll();
    }

    /**
     * {@code GET  /recommmended-items/:id} : get the "id" recommmendedItems.
     *
     * @param id the id of the recommmendedItems to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the recommmendedItems, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/recommmended-items/{id}")
    public ResponseEntity<RecommmendedItems> getRecommmendedItems(@PathVariable Long id) {
        log.debug("REST request to get RecommmendedItems : {}", id);
        Optional<RecommmendedItems> recommmendedItems = recommmendedItemsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(recommmendedItems);
    }

    /**
     * {@code DELETE  /recommmended-items/:id} : delete the "id" recommmendedItems.
     *
     * @param id the id of the recommmendedItems to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/recommmended-items/{id}")
    public ResponseEntity<Void> deleteRecommmendedItems(@PathVariable Long id) {
        log.debug("REST request to delete RecommmendedItems : {}", id);
        recommmendedItemsRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
