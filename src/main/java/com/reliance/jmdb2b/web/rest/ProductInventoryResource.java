package com.reliance.jmdb2b.web.rest;

import com.reliance.jmdb2b.domain.ProductInventory;
import com.reliance.jmdb2b.repository.ProductInventoryRepository;
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
 * REST controller for managing {@link com.reliance.jmdb2b.domain.ProductInventory}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ProductInventoryResource {

    private final Logger log = LoggerFactory.getLogger(ProductInventoryResource.class);

    private static final String ENTITY_NAME = "productInventory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductInventoryRepository productInventoryRepository;

    public ProductInventoryResource(ProductInventoryRepository productInventoryRepository) {
        this.productInventoryRepository = productInventoryRepository;
    }

    /**
     * {@code POST  /product-inventories} : Create a new productInventory.
     *
     * @param productInventory the productInventory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productInventory, or with status {@code 400 (Bad Request)} if the productInventory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/product-inventories")
    public ResponseEntity<ProductInventory> createProductInventory(@RequestBody ProductInventory productInventory)
        throws URISyntaxException {
        log.debug("REST request to save ProductInventory : {}", productInventory);
        if (productInventory.getId() != null) {
            throw new BadRequestAlertException("A new productInventory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductInventory result = productInventoryRepository.save(productInventory);
        return ResponseEntity
            .created(new URI("/api/product-inventories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /product-inventories/:id} : Updates an existing productInventory.
     *
     * @param id the id of the productInventory to save.
     * @param productInventory the productInventory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productInventory,
     * or with status {@code 400 (Bad Request)} if the productInventory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productInventory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/product-inventories/{id}")
    public ResponseEntity<ProductInventory> updateProductInventory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductInventory productInventory
    ) throws URISyntaxException {
        log.debug("REST request to update ProductInventory : {}, {}", id, productInventory);
        if (productInventory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productInventory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productInventoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProductInventory result = productInventoryRepository.save(productInventory);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, productInventory.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /product-inventories/:id} : Partial updates given fields of an existing productInventory, field will ignore if it is null
     *
     * @param id the id of the productInventory to save.
     * @param productInventory the productInventory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productInventory,
     * or with status {@code 400 (Bad Request)} if the productInventory is not valid,
     * or with status {@code 404 (Not Found)} if the productInventory is not found,
     * or with status {@code 500 (Internal Server Error)} if the productInventory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/product-inventories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductInventory> partialUpdateProductInventory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductInventory productInventory
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProductInventory partially : {}, {}", id, productInventory);
        if (productInventory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productInventory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productInventoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductInventory> result = productInventoryRepository
            .findById(productInventory.getId())
            .map(existingProductInventory -> {
                if (productInventory.getLocation() != null) {
                    existingProductInventory.setLocation(productInventory.getLocation());
                }
                if (productInventory.getStockQuantity() != null) {
                    existingProductInventory.setStockQuantity(productInventory.getStockQuantity());
                }
                if (productInventory.getStockStatus() != null) {
                    existingProductInventory.setStockStatus(productInventory.getStockStatus());
                }
                if (productInventory.getCreatedTime() != null) {
                    existingProductInventory.setCreatedTime(productInventory.getCreatedTime());
                }
                if (productInventory.getUpdatedTime() != null) {
                    existingProductInventory.setUpdatedTime(productInventory.getUpdatedTime());
                }
                if (productInventory.getCreatedBy() != null) {
                    existingProductInventory.setCreatedBy(productInventory.getCreatedBy());
                }
                if (productInventory.getUpdatedBy() != null) {
                    existingProductInventory.setUpdatedBy(productInventory.getUpdatedBy());
                }

                return existingProductInventory;
            })
            .map(productInventoryRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, productInventory.getId().toString())
        );
    }

    /**
     * {@code GET  /product-inventories} : get all the productInventories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productInventories in body.
     */
    @GetMapping("/product-inventories")
    public List<ProductInventory> getAllProductInventories() {
        log.debug("REST request to get all ProductInventories");
        return productInventoryRepository.findAll();
    }

    /**
     * {@code GET  /product-inventories/:id} : get the "id" productInventory.
     *
     * @param id the id of the productInventory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productInventory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/product-inventories/{id}")
    public ResponseEntity<ProductInventory> getProductInventory(@PathVariable Long id) {
        log.debug("REST request to get ProductInventory : {}", id);
        Optional<ProductInventory> productInventory = productInventoryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(productInventory);
    }

    /**
     * {@code DELETE  /product-inventories/:id} : delete the "id" productInventory.
     *
     * @param id the id of the productInventory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/product-inventories/{id}")
    public ResponseEntity<Void> deleteProductInventory(@PathVariable Long id) {
        log.debug("REST request to delete ProductInventory : {}", id);
        productInventoryRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
