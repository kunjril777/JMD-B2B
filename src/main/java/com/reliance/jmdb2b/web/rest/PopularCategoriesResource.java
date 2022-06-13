package com.reliance.jmdb2b.web.rest;

import com.reliance.jmdb2b.domain.PopularCategories;
import com.reliance.jmdb2b.repository.PopularCategoriesRepository;
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
 * REST controller for managing {@link com.reliance.jmdb2b.domain.PopularCategories}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PopularCategoriesResource {

    private final Logger log = LoggerFactory.getLogger(PopularCategoriesResource.class);

    private static final String ENTITY_NAME = "popularCategories";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PopularCategoriesRepository popularCategoriesRepository;

    public PopularCategoriesResource(PopularCategoriesRepository popularCategoriesRepository) {
        this.popularCategoriesRepository = popularCategoriesRepository;
    }

    /**
     * {@code POST  /popular-categories} : Create a new popularCategories.
     *
     * @param popularCategories the popularCategories to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new popularCategories, or with status {@code 400 (Bad Request)} if the popularCategories has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/popular-categories")
    public ResponseEntity<PopularCategories> createPopularCategories(@RequestBody PopularCategories popularCategories)
        throws URISyntaxException {
        log.debug("REST request to save PopularCategories : {}", popularCategories);
        if (popularCategories.getId() != null) {
            throw new BadRequestAlertException("A new popularCategories cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PopularCategories result = popularCategoriesRepository.save(popularCategories);
        return ResponseEntity
            .created(new URI("/api/popular-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /popular-categories/:id} : Updates an existing popularCategories.
     *
     * @param id the id of the popularCategories to save.
     * @param popularCategories the popularCategories to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated popularCategories,
     * or with status {@code 400 (Bad Request)} if the popularCategories is not valid,
     * or with status {@code 500 (Internal Server Error)} if the popularCategories couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/popular-categories/{id}")
    public ResponseEntity<PopularCategories> updatePopularCategories(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PopularCategories popularCategories
    ) throws URISyntaxException {
        log.debug("REST request to update PopularCategories : {}, {}", id, popularCategories);
        if (popularCategories.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, popularCategories.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!popularCategoriesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PopularCategories result = popularCategoriesRepository.save(popularCategories);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, popularCategories.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /popular-categories/:id} : Partial updates given fields of an existing popularCategories, field will ignore if it is null
     *
     * @param id the id of the popularCategories to save.
     * @param popularCategories the popularCategories to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated popularCategories,
     * or with status {@code 400 (Bad Request)} if the popularCategories is not valid,
     * or with status {@code 404 (Not Found)} if the popularCategories is not found,
     * or with status {@code 500 (Internal Server Error)} if the popularCategories couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/popular-categories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PopularCategories> partialUpdatePopularCategories(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PopularCategories popularCategories
    ) throws URISyntaxException {
        log.debug("REST request to partial update PopularCategories partially : {}, {}", id, popularCategories);
        if (popularCategories.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, popularCategories.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!popularCategoriesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PopularCategories> result = popularCategoriesRepository
            .findById(popularCategories.getId())
            .map(existingPopularCategories -> {
                if (popularCategories.getCategoryId() != null) {
                    existingPopularCategories.setCategoryId(popularCategories.getCategoryId());
                }
                if (popularCategories.getViewCount() != null) {
                    existingPopularCategories.setViewCount(popularCategories.getViewCount());
                }
                if (popularCategories.getSoldQuantity() != null) {
                    existingPopularCategories.setSoldQuantity(popularCategories.getSoldQuantity());
                }
                if (popularCategories.getSoldDate() != null) {
                    existingPopularCategories.setSoldDate(popularCategories.getSoldDate());
                }
                if (popularCategories.getCreatedTime() != null) {
                    existingPopularCategories.setCreatedTime(popularCategories.getCreatedTime());
                }
                if (popularCategories.getUpdatedTime() != null) {
                    existingPopularCategories.setUpdatedTime(popularCategories.getUpdatedTime());
                }
                if (popularCategories.getCreatedBy() != null) {
                    existingPopularCategories.setCreatedBy(popularCategories.getCreatedBy());
                }
                if (popularCategories.getUpdatedBy() != null) {
                    existingPopularCategories.setUpdatedBy(popularCategories.getUpdatedBy());
                }

                return existingPopularCategories;
            })
            .map(popularCategoriesRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, popularCategories.getId().toString())
        );
    }

    /**
     * {@code GET  /popular-categories} : get all the popularCategories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of popularCategories in body.
     */
    @GetMapping("/popular-categories")
    public List<PopularCategories> getAllPopularCategories() {
        log.debug("REST request to get all PopularCategories");
        return popularCategoriesRepository.findAll();
    }

    /**
     * {@code GET  /popular-categories/:id} : get the "id" popularCategories.
     *
     * @param id the id of the popularCategories to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the popularCategories, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/popular-categories/{id}")
    public ResponseEntity<PopularCategories> getPopularCategories(@PathVariable Long id) {
        log.debug("REST request to get PopularCategories : {}", id);
        Optional<PopularCategories> popularCategories = popularCategoriesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(popularCategories);
    }

    /**
     * {@code DELETE  /popular-categories/:id} : delete the "id" popularCategories.
     *
     * @param id the id of the popularCategories to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/popular-categories/{id}")
    public ResponseEntity<Void> deletePopularCategories(@PathVariable Long id) {
        log.debug("REST request to delete PopularCategories : {}", id);
        popularCategoriesRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
