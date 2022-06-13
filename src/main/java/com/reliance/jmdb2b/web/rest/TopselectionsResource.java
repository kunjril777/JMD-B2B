package com.reliance.jmdb2b.web.rest;

import com.reliance.jmdb2b.domain.Topselections;
import com.reliance.jmdb2b.repository.TopselectionsRepository;
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
 * REST controller for managing {@link com.reliance.jmdb2b.domain.Topselections}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TopselectionsResource {

    private final Logger log = LoggerFactory.getLogger(TopselectionsResource.class);

    private static final String ENTITY_NAME = "topselections";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TopselectionsRepository topselectionsRepository;

    public TopselectionsResource(TopselectionsRepository topselectionsRepository) {
        this.topselectionsRepository = topselectionsRepository;
    }

    /**
     * {@code POST  /topselections} : Create a new topselections.
     *
     * @param topselections the topselections to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new topselections, or with status {@code 400 (Bad Request)} if the topselections has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/topselections")
    public ResponseEntity<Topselections> createTopselections(@RequestBody Topselections topselections) throws URISyntaxException {
        log.debug("REST request to save Topselections : {}", topselections);
        if (topselections.getId() != null) {
            throw new BadRequestAlertException("A new topselections cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Topselections result = topselectionsRepository.save(topselections);
        return ResponseEntity
            .created(new URI("/api/topselections/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /topselections/:id} : Updates an existing topselections.
     *
     * @param id the id of the topselections to save.
     * @param topselections the topselections to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated topselections,
     * or with status {@code 400 (Bad Request)} if the topselections is not valid,
     * or with status {@code 500 (Internal Server Error)} if the topselections couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/topselections/{id}")
    public ResponseEntity<Topselections> updateTopselections(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Topselections topselections
    ) throws URISyntaxException {
        log.debug("REST request to update Topselections : {}, {}", id, topselections);
        if (topselections.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, topselections.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!topselectionsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Topselections result = topselectionsRepository.save(topselections);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, topselections.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /topselections/:id} : Partial updates given fields of an existing topselections, field will ignore if it is null
     *
     * @param id the id of the topselections to save.
     * @param topselections the topselections to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated topselections,
     * or with status {@code 400 (Bad Request)} if the topselections is not valid,
     * or with status {@code 404 (Not Found)} if the topselections is not found,
     * or with status {@code 500 (Internal Server Error)} if the topselections couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/topselections/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Topselections> partialUpdateTopselections(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Topselections topselections
    ) throws URISyntaxException {
        log.debug("REST request to partial update Topselections partially : {}, {}", id, topselections);
        if (topselections.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, topselections.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!topselectionsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Topselections> result = topselectionsRepository
            .findById(topselections.getId())
            .map(existingTopselections -> {
                if (topselections.getProductId() != null) {
                    existingTopselections.setProductId(topselections.getProductId());
                }
                if (topselections.getCategoryId() != null) {
                    existingTopselections.setCategoryId(topselections.getCategoryId());
                }
                if (topselections.getPriority() != null) {
                    existingTopselections.setPriority(topselections.getPriority());
                }
                if (topselections.getCreatedTime() != null) {
                    existingTopselections.setCreatedTime(topselections.getCreatedTime());
                }
                if (topselections.getUpdatedTime() != null) {
                    existingTopselections.setUpdatedTime(topselections.getUpdatedTime());
                }
                if (topselections.getCreatedBy() != null) {
                    existingTopselections.setCreatedBy(topselections.getCreatedBy());
                }
                if (topselections.getUpdatedBy() != null) {
                    existingTopselections.setUpdatedBy(topselections.getUpdatedBy());
                }

                return existingTopselections;
            })
            .map(topselectionsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, topselections.getId().toString())
        );
    }

    /**
     * {@code GET  /topselections} : get all the topselections.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of topselections in body.
     */
    @GetMapping("/topselections")
    public List<Topselections> getAllTopselections() {
        log.debug("REST request to get all Topselections");
        return topselectionsRepository.findAll();
    }

    /**
     * {@code GET  /topselections/:id} : get the "id" topselections.
     *
     * @param id the id of the topselections to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the topselections, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/topselections/{id}")
    public ResponseEntity<Topselections> getTopselections(@PathVariable Long id) {
        log.debug("REST request to get Topselections : {}", id);
        Optional<Topselections> topselections = topselectionsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(topselections);
    }

    /**
     * {@code DELETE  /topselections/:id} : delete the "id" topselections.
     *
     * @param id the id of the topselections to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/topselections/{id}")
    public ResponseEntity<Void> deleteTopselections(@PathVariable Long id) {
        log.debug("REST request to delete Topselections : {}", id);
        topselectionsRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
