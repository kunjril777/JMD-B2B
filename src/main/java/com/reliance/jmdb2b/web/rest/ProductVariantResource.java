package com.reliance.jmdb2b.web.rest;

import com.reliance.jmdb2b.domain.ProductVariant;
import com.reliance.jmdb2b.repository.ProductVariantRepository;
import com.reliance.jmdb2b.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.reliance.jmdb2b.domain.ProductVariant}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ProductVariantResource {

    private final Logger log = LoggerFactory.getLogger(ProductVariantResource.class);

    private static final String ENTITY_NAME = "productVariant";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductVariantRepository productVariantRepository;

    public ProductVariantResource(ProductVariantRepository productVariantRepository) {
        this.productVariantRepository = productVariantRepository;
    }

    /**
     * {@code POST  /product-variants} : Create a new productVariant.
     *
     * @param productVariant the productVariant to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productVariant, or with status {@code 400 (Bad Request)} if the productVariant has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/product-variants")
    public ResponseEntity<ProductVariant> createProductVariant(@Valid @RequestBody ProductVariant productVariant)
        throws URISyntaxException {
        log.debug("REST request to save ProductVariant : {}", productVariant);
        if (productVariant.getId() != null) {
            throw new BadRequestAlertException("A new productVariant cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductVariant result = productVariantRepository.save(productVariant);
        return ResponseEntity
            .created(new URI("/api/product-variants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /product-variants/:id} : Updates an existing productVariant.
     *
     * @param id the id of the productVariant to save.
     * @param productVariant the productVariant to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productVariant,
     * or with status {@code 400 (Bad Request)} if the productVariant is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productVariant couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/product-variants/{id}")
    public ResponseEntity<ProductVariant> updateProductVariant(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductVariant productVariant
    ) throws URISyntaxException {
        log.debug("REST request to update ProductVariant : {}, {}", id, productVariant);
        if (productVariant.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productVariant.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productVariantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProductVariant result = productVariantRepository.save(productVariant);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, productVariant.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /product-variants/:id} : Partial updates given fields of an existing productVariant, field will ignore if it is null
     *
     * @param id the id of the productVariant to save.
     * @param productVariant the productVariant to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productVariant,
     * or with status {@code 400 (Bad Request)} if the productVariant is not valid,
     * or with status {@code 404 (Not Found)} if the productVariant is not found,
     * or with status {@code 500 (Internal Server Error)} if the productVariant couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/product-variants/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductVariant> partialUpdateProductVariant(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductVariant productVariant
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProductVariant partially : {}, {}", id, productVariant);
        if (productVariant.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productVariant.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productVariantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductVariant> result = productVariantRepository
            .findById(productVariant.getId())
            .map(existingProductVariant -> {
                if (productVariant.getProductVariantId() != null) {
                    existingProductVariant.setProductVariantId(productVariant.getProductVariantId());
                }
                if (productVariant.getProductPrice() != null) {
                    existingProductVariant.setProductPrice(productVariant.getProductPrice());
                }
                if (productVariant.getDealPrice() != null) {
                    existingProductVariant.setDealPrice(productVariant.getDealPrice());
                }
                if (productVariant.getMrp() != null) {
                    existingProductVariant.setMrp(productVariant.getMrp());
                }
                if (productVariant.getDescription() != null) {
                    existingProductVariant.setDescription(productVariant.getDescription());
                }
                if (productVariant.getTitle() != null) {
                    existingProductVariant.setTitle(productVariant.getTitle());
                }
                if (productVariant.getAttributes() != null) {
                    existingProductVariant.setAttributes(productVariant.getAttributes());
                }
                if (productVariant.getImages() != null) {
                    existingProductVariant.setImages(productVariant.getImages());
                }
                if (productVariant.getCreatedTime() != null) {
                    existingProductVariant.setCreatedTime(productVariant.getCreatedTime());
                }
                if (productVariant.getUpdatedTime() != null) {
                    existingProductVariant.setUpdatedTime(productVariant.getUpdatedTime());
                }
                if (productVariant.getCreatedBy() != null) {
                    existingProductVariant.setCreatedBy(productVariant.getCreatedBy());
                }
                if (productVariant.getUpdatedBy() != null) {
                    existingProductVariant.setUpdatedBy(productVariant.getUpdatedBy());
                }

                return existingProductVariant;
            })
            .map(productVariantRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, productVariant.getId().toString())
        );
    }

    /**
     * {@code GET  /product-variants} : get all the productVariants.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productVariants in body.
     */
    @GetMapping("/product-variants")
    public List<ProductVariant> getAllProductVariants() {
        log.debug("REST request to get all ProductVariants");
        return productVariantRepository.findAll();
    }

    /**
     * {@code GET  /product-variants/:id} : get the "id" productVariant.
     *
     * @param id the id of the productVariant to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productVariant, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/product-variants/{id}")
    public ResponseEntity<ProductVariant> getProductVariant(@PathVariable Long id) {
        log.debug("REST request to get ProductVariant : {}", id);
        Optional<ProductVariant> productVariant = productVariantRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(productVariant);
    }

    /**
     * {@code DELETE  /product-variants/:id} : delete the "id" productVariant.
     *
     * @param id the id of the productVariant to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/product-variants/{id}")
    public ResponseEntity<Void> deleteProductVariant(@PathVariable Long id) {
        log.debug("REST request to delete ProductVariant : {}", id);
        productVariantRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
