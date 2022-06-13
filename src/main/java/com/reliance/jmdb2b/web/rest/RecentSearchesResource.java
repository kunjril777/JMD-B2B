package com.reliance.jmdb2b.web.rest;

import com.reliance.jmdb2b.domain.RecentSearches;
import com.reliance.jmdb2b.repository.RecentSearchesRepository;
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
 * REST controller for managing {@link com.reliance.jmdb2b.domain.RecentSearches}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RecentSearchesResource {

    private final Logger log = LoggerFactory.getLogger(RecentSearchesResource.class);

    private static final String ENTITY_NAME = "recentSearches";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RecentSearchesRepository recentSearchesRepository;

    public RecentSearchesResource(RecentSearchesRepository recentSearchesRepository) {
        this.recentSearchesRepository = recentSearchesRepository;
    }

    /**
     * {@code POST  /recent-searches} : Create a new recentSearches.
     *
     * @param recentSearches the recentSearches to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new recentSearches, or with status {@code 400 (Bad Request)} if the recentSearches has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/recent-searches")
    public ResponseEntity<RecentSearches> createRecentSearches(@RequestBody RecentSearches recentSearches) throws URISyntaxException {
        log.debug("REST request to save RecentSearches : {}", recentSearches);
        if (recentSearches.getId() != null) {
            throw new BadRequestAlertException("A new recentSearches cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RecentSearches result = recentSearchesRepository.save(recentSearches);
        return ResponseEntity
            .created(new URI("/api/recent-searches/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /recent-searches/:id} : Updates an existing recentSearches.
     *
     * @param id the id of the recentSearches to save.
     * @param recentSearches the recentSearches to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recentSearches,
     * or with status {@code 400 (Bad Request)} if the recentSearches is not valid,
     * or with status {@code 500 (Internal Server Error)} if the recentSearches couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/recent-searches/{id}")
    public ResponseEntity<RecentSearches> updateRecentSearches(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RecentSearches recentSearches
    ) throws URISyntaxException {
        log.debug("REST request to update RecentSearches : {}, {}", id, recentSearches);
        if (recentSearches.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recentSearches.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recentSearchesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RecentSearches result = recentSearchesRepository.save(recentSearches);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, recentSearches.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /recent-searches/:id} : Partial updates given fields of an existing recentSearches, field will ignore if it is null
     *
     * @param id the id of the recentSearches to save.
     * @param recentSearches the recentSearches to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recentSearches,
     * or with status {@code 400 (Bad Request)} if the recentSearches is not valid,
     * or with status {@code 404 (Not Found)} if the recentSearches is not found,
     * or with status {@code 500 (Internal Server Error)} if the recentSearches couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/recent-searches/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RecentSearches> partialUpdateRecentSearches(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RecentSearches recentSearches
    ) throws URISyntaxException {
        log.debug("REST request to partial update RecentSearches partially : {}, {}", id, recentSearches);
        if (recentSearches.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recentSearches.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recentSearchesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RecentSearches> result = recentSearchesRepository
            .findById(recentSearches.getId())
            .map(existingRecentSearches -> {
                if (recentSearches.getUserId() != null) {
                    existingRecentSearches.setUserId(recentSearches.getUserId());
                }
                if (recentSearches.getSearchText() != null) {
                    existingRecentSearches.setSearchText(recentSearches.getSearchText());
                }
                if (recentSearches.getSearchCount() != null) {
                    existingRecentSearches.setSearchCount(recentSearches.getSearchCount());
                }
                if (recentSearches.getCreatedTime() != null) {
                    existingRecentSearches.setCreatedTime(recentSearches.getCreatedTime());
                }
                if (recentSearches.getUpdatedTime() != null) {
                    existingRecentSearches.setUpdatedTime(recentSearches.getUpdatedTime());
                }
                if (recentSearches.getCreatedBy() != null) {
                    existingRecentSearches.setCreatedBy(recentSearches.getCreatedBy());
                }
                if (recentSearches.getUpdatedBy() != null) {
                    existingRecentSearches.setUpdatedBy(recentSearches.getUpdatedBy());
                }

                return existingRecentSearches;
            })
            .map(recentSearchesRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, recentSearches.getId().toString())
        );
    }

    /**
     * {@code GET  /recent-searches} : get all the recentSearches.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of recentSearches in body.
     */
    @GetMapping("/recent-searches")
    public List<RecentSearches> getAllRecentSearches() {
        log.debug("REST request to get all RecentSearches");
        return recentSearchesRepository.findAll();
    }

    /**
     * {@code GET  /recent-searches/:id} : get the "id" recentSearches.
     *
     * @param id the id of the recentSearches to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the recentSearches, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/recent-searches/{id}")
    public ResponseEntity<RecentSearches> getRecentSearches(@PathVariable Long id) {
        log.debug("REST request to get RecentSearches : {}", id);
        Optional<RecentSearches> recentSearches = recentSearchesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(recentSearches);
    }

    /**
     * {@code DELETE  /recent-searches/:id} : delete the "id" recentSearches.
     *
     * @param id the id of the recentSearches to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/recent-searches/{id}")
    public ResponseEntity<Void> deleteRecentSearches(@PathVariable Long id) {
        log.debug("REST request to delete RecentSearches : {}", id);
        recentSearchesRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
