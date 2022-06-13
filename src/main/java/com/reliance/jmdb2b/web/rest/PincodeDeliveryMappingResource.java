package com.reliance.jmdb2b.web.rest;

import com.reliance.jmdb2b.domain.PincodeDeliveryMapping;
import com.reliance.jmdb2b.repository.PincodeDeliveryMappingRepository;
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
 * REST controller for managing {@link com.reliance.jmdb2b.domain.PincodeDeliveryMapping}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PincodeDeliveryMappingResource {

    private final Logger log = LoggerFactory.getLogger(PincodeDeliveryMappingResource.class);

    private static final String ENTITY_NAME = "pincodeDeliveryMapping";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PincodeDeliveryMappingRepository pincodeDeliveryMappingRepository;

    public PincodeDeliveryMappingResource(PincodeDeliveryMappingRepository pincodeDeliveryMappingRepository) {
        this.pincodeDeliveryMappingRepository = pincodeDeliveryMappingRepository;
    }

    /**
     * {@code POST  /pincode-delivery-mappings} : Create a new pincodeDeliveryMapping.
     *
     * @param pincodeDeliveryMapping the pincodeDeliveryMapping to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pincodeDeliveryMapping, or with status {@code 400 (Bad Request)} if the pincodeDeliveryMapping has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pincode-delivery-mappings")
    public ResponseEntity<PincodeDeliveryMapping> createPincodeDeliveryMapping(@RequestBody PincodeDeliveryMapping pincodeDeliveryMapping)
        throws URISyntaxException {
        log.debug("REST request to save PincodeDeliveryMapping : {}", pincodeDeliveryMapping);
        if (pincodeDeliveryMapping.getId() != null) {
            throw new BadRequestAlertException("A new pincodeDeliveryMapping cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PincodeDeliveryMapping result = pincodeDeliveryMappingRepository.save(pincodeDeliveryMapping);
        return ResponseEntity
            .created(new URI("/api/pincode-delivery-mappings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pincode-delivery-mappings/:id} : Updates an existing pincodeDeliveryMapping.
     *
     * @param id the id of the pincodeDeliveryMapping to save.
     * @param pincodeDeliveryMapping the pincodeDeliveryMapping to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pincodeDeliveryMapping,
     * or with status {@code 400 (Bad Request)} if the pincodeDeliveryMapping is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pincodeDeliveryMapping couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pincode-delivery-mappings/{id}")
    public ResponseEntity<PincodeDeliveryMapping> updatePincodeDeliveryMapping(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PincodeDeliveryMapping pincodeDeliveryMapping
    ) throws URISyntaxException {
        log.debug("REST request to update PincodeDeliveryMapping : {}, {}", id, pincodeDeliveryMapping);
        if (pincodeDeliveryMapping.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pincodeDeliveryMapping.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pincodeDeliveryMappingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PincodeDeliveryMapping result = pincodeDeliveryMappingRepository.save(pincodeDeliveryMapping);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pincodeDeliveryMapping.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /pincode-delivery-mappings/:id} : Partial updates given fields of an existing pincodeDeliveryMapping, field will ignore if it is null
     *
     * @param id the id of the pincodeDeliveryMapping to save.
     * @param pincodeDeliveryMapping the pincodeDeliveryMapping to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pincodeDeliveryMapping,
     * or with status {@code 400 (Bad Request)} if the pincodeDeliveryMapping is not valid,
     * or with status {@code 404 (Not Found)} if the pincodeDeliveryMapping is not found,
     * or with status {@code 500 (Internal Server Error)} if the pincodeDeliveryMapping couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/pincode-delivery-mappings/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PincodeDeliveryMapping> partialUpdatePincodeDeliveryMapping(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PincodeDeliveryMapping pincodeDeliveryMapping
    ) throws URISyntaxException {
        log.debug("REST request to partial update PincodeDeliveryMapping partially : {}, {}", id, pincodeDeliveryMapping);
        if (pincodeDeliveryMapping.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pincodeDeliveryMapping.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pincodeDeliveryMappingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PincodeDeliveryMapping> result = pincodeDeliveryMappingRepository
            .findById(pincodeDeliveryMapping.getId())
            .map(existingPincodeDeliveryMapping -> {
                if (pincodeDeliveryMapping.getOriginLocation() != null) {
                    existingPincodeDeliveryMapping.setOriginLocation(pincodeDeliveryMapping.getOriginLocation());
                }
                if (pincodeDeliveryMapping.getDeliveryPincode() != null) {
                    existingPincodeDeliveryMapping.setDeliveryPincode(pincodeDeliveryMapping.getDeliveryPincode());
                }
                if (pincodeDeliveryMapping.getWeight() != null) {
                    existingPincodeDeliveryMapping.setWeight(pincodeDeliveryMapping.getWeight());
                }
                if (pincodeDeliveryMapping.getDeliverable() != null) {
                    existingPincodeDeliveryMapping.setDeliverable(pincodeDeliveryMapping.getDeliverable());
                }
                if (pincodeDeliveryMapping.getNumOfDaysToDelivery() != null) {
                    existingPincodeDeliveryMapping.setNumOfDaysToDelivery(pincodeDeliveryMapping.getNumOfDaysToDelivery());
                }
                if (pincodeDeliveryMapping.getCreatedTime() != null) {
                    existingPincodeDeliveryMapping.setCreatedTime(pincodeDeliveryMapping.getCreatedTime());
                }
                if (pincodeDeliveryMapping.getUpdatedTime() != null) {
                    existingPincodeDeliveryMapping.setUpdatedTime(pincodeDeliveryMapping.getUpdatedTime());
                }
                if (pincodeDeliveryMapping.getCreatedBy() != null) {
                    existingPincodeDeliveryMapping.setCreatedBy(pincodeDeliveryMapping.getCreatedBy());
                }
                if (pincodeDeliveryMapping.getUpdatedBy() != null) {
                    existingPincodeDeliveryMapping.setUpdatedBy(pincodeDeliveryMapping.getUpdatedBy());
                }

                return existingPincodeDeliveryMapping;
            })
            .map(pincodeDeliveryMappingRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pincodeDeliveryMapping.getId().toString())
        );
    }

    /**
     * {@code GET  /pincode-delivery-mappings} : get all the pincodeDeliveryMappings.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pincodeDeliveryMappings in body.
     */
    @GetMapping("/pincode-delivery-mappings")
    public List<PincodeDeliveryMapping> getAllPincodeDeliveryMappings() {
        log.debug("REST request to get all PincodeDeliveryMappings");
        return pincodeDeliveryMappingRepository.findAll();
    }

    /**
     * {@code GET  /pincode-delivery-mappings/:id} : get the "id" pincodeDeliveryMapping.
     *
     * @param id the id of the pincodeDeliveryMapping to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pincodeDeliveryMapping, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pincode-delivery-mappings/{id}")
    public ResponseEntity<PincodeDeliveryMapping> getPincodeDeliveryMapping(@PathVariable Long id) {
        log.debug("REST request to get PincodeDeliveryMapping : {}", id);
        Optional<PincodeDeliveryMapping> pincodeDeliveryMapping = pincodeDeliveryMappingRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(pincodeDeliveryMapping);
    }

    /**
     * {@code DELETE  /pincode-delivery-mappings/:id} : delete the "id" pincodeDeliveryMapping.
     *
     * @param id the id of the pincodeDeliveryMapping to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pincode-delivery-mappings/{id}")
    public ResponseEntity<Void> deletePincodeDeliveryMapping(@PathVariable Long id) {
        log.debug("REST request to delete PincodeDeliveryMapping : {}", id);
        pincodeDeliveryMappingRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
