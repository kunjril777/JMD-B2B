package com.reliance.jmdb2b.web.rest;

import com.reliance.jmdb2b.domain.AttributeName;
import com.reliance.jmdb2b.repository.AttributeNameRepository;
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
 * REST controller for managing {@link com.reliance.jmdb2b.domain.AttributeName}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AttributeNameResource {

    private final Logger log = LoggerFactory.getLogger(AttributeNameResource.class);

    private static final String ENTITY_NAME = "attributeName";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AttributeNameRepository attributeNameRepository;

    public AttributeNameResource(AttributeNameRepository attributeNameRepository) {
        this.attributeNameRepository = attributeNameRepository;
    }

    /**
     * {@code POST  /attribute-names} : Create a new attributeName.
     *
     * @param attributeName the attributeName to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new attributeName, or with status {@code 400 (Bad Request)} if the attributeName has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/attribute-names")
    public ResponseEntity<AttributeName> createAttributeName(@RequestBody AttributeName attributeName) throws URISyntaxException {
        log.debug("REST request to save AttributeName : {}", attributeName);
        if (attributeName.getId() != null) {
            throw new BadRequestAlertException("A new attributeName cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AttributeName result = attributeNameRepository.save(attributeName);
        return ResponseEntity
            .created(new URI("/api/attribute-names/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /attribute-names/:id} : Updates an existing attributeName.
     *
     * @param id the id of the attributeName to save.
     * @param attributeName the attributeName to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attributeName,
     * or with status {@code 400 (Bad Request)} if the attributeName is not valid,
     * or with status {@code 500 (Internal Server Error)} if the attributeName couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/attribute-names/{id}")
    public ResponseEntity<AttributeName> updateAttributeName(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AttributeName attributeName
    ) throws URISyntaxException {
        log.debug("REST request to update AttributeName : {}, {}", id, attributeName);
        if (attributeName.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, attributeName.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!attributeNameRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AttributeName result = attributeNameRepository.save(attributeName);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, attributeName.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /attribute-names/:id} : Partial updates given fields of an existing attributeName, field will ignore if it is null
     *
     * @param id the id of the attributeName to save.
     * @param attributeName the attributeName to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attributeName,
     * or with status {@code 400 (Bad Request)} if the attributeName is not valid,
     * or with status {@code 404 (Not Found)} if the attributeName is not found,
     * or with status {@code 500 (Internal Server Error)} if the attributeName couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/attribute-names/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AttributeName> partialUpdateAttributeName(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AttributeName attributeName
    ) throws URISyntaxException {
        log.debug("REST request to partial update AttributeName partially : {}, {}", id, attributeName);
        if (attributeName.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, attributeName.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!attributeNameRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AttributeName> result = attributeNameRepository
            .findById(attributeName.getId())
            .map(existingAttributeName -> {
                if (attributeName.getPropertyName() != null) {
                    existingAttributeName.setPropertyName(attributeName.getPropertyName());
                }
                if (attributeName.getPropertyDescription() != null) {
                    existingAttributeName.setPropertyDescription(attributeName.getPropertyDescription());
                }
                if (attributeName.getMandetory() != null) {
                    existingAttributeName.setMandetory(attributeName.getMandetory());
                }
                if (attributeName.getPropertyType() != null) {
                    existingAttributeName.setPropertyType(attributeName.getPropertyType());
                }
                if (attributeName.getPlpDisplayName() != null) {
                    existingAttributeName.setPlpDisplayName(attributeName.getPlpDisplayName());
                }
                if (attributeName.getPdpDisplayName() != null) {
                    existingAttributeName.setPdpDisplayName(attributeName.getPdpDisplayName());
                }
                if (attributeName.getParentAttributeId() != null) {
                    existingAttributeName.setParentAttributeId(attributeName.getParentAttributeId());
                }
                if (attributeName.getDisplayOrder() != null) {
                    existingAttributeName.setDisplayOrder(attributeName.getDisplayOrder());
                }

                return existingAttributeName;
            })
            .map(attributeNameRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, attributeName.getId().toString())
        );
    }

    /**
     * {@code GET  /attribute-names} : get all the attributeNames.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of attributeNames in body.
     */
    @GetMapping("/attribute-names")
    public List<AttributeName> getAllAttributeNames() {
        log.debug("REST request to get all AttributeNames");
        return attributeNameRepository.findAll();
    }

    /**
     * {@code GET  /attribute-names/:id} : get the "id" attributeName.
     *
     * @param id the id of the attributeName to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the attributeName, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/attribute-names/{id}")
    public ResponseEntity<AttributeName> getAttributeName(@PathVariable Long id) {
        log.debug("REST request to get AttributeName : {}", id);
        Optional<AttributeName> attributeName = attributeNameRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(attributeName);
    }

    /**
     * {@code DELETE  /attribute-names/:id} : delete the "id" attributeName.
     *
     * @param id the id of the attributeName to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/attribute-names/{id}")
    public ResponseEntity<Void> deleteAttributeName(@PathVariable Long id) {
        log.debug("REST request to delete AttributeName : {}", id);
        attributeNameRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
