package com.reliance.jmdb2b.web.rest;

import com.reliance.jmdb2b.domain.ProductReview;
import com.reliance.jmdb2b.repository.ProductReviewRepository;
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
 * REST controller for managing {@link com.reliance.jmdb2b.domain.ProductReview}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ProductReviewResource {

    private final Logger log = LoggerFactory.getLogger(ProductReviewResource.class);

    private static final String ENTITY_NAME = "productReview";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductReviewRepository productReviewRepository;

    public ProductReviewResource(ProductReviewRepository productReviewRepository) {
        this.productReviewRepository = productReviewRepository;
    }

    /**
     * {@code POST  /product-reviews} : Create a new productReview.
     *
     * @param productReview the productReview to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productReview, or with status {@code 400 (Bad Request)} if the productReview has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/product-reviews")
    public ResponseEntity<ProductReview> createProductReview(@RequestBody ProductReview productReview) throws URISyntaxException {
        log.debug("REST request to save ProductReview : {}", productReview);
        if (productReview.getId() != null) {
            throw new BadRequestAlertException("A new productReview cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductReview result = productReviewRepository.save(productReview);
        return ResponseEntity
            .created(new URI("/api/product-reviews/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /product-reviews/:id} : Updates an existing productReview.
     *
     * @param id the id of the productReview to save.
     * @param productReview the productReview to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productReview,
     * or with status {@code 400 (Bad Request)} if the productReview is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productReview couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/product-reviews/{id}")
    public ResponseEntity<ProductReview> updateProductReview(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductReview productReview
    ) throws URISyntaxException {
        log.debug("REST request to update ProductReview : {}, {}", id, productReview);
        if (productReview.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productReview.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productReviewRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProductReview result = productReviewRepository.save(productReview);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, productReview.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /product-reviews/:id} : Partial updates given fields of an existing productReview, field will ignore if it is null
     *
     * @param id the id of the productReview to save.
     * @param productReview the productReview to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productReview,
     * or with status {@code 400 (Bad Request)} if the productReview is not valid,
     * or with status {@code 404 (Not Found)} if the productReview is not found,
     * or with status {@code 500 (Internal Server Error)} if the productReview couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/product-reviews/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductReview> partialUpdateProductReview(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductReview productReview
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProductReview partially : {}, {}", id, productReview);
        if (productReview.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productReview.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productReviewRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductReview> result = productReviewRepository
            .findById(productReview.getId())
            .map(existingProductReview -> {
                if (productReview.getUserId() != null) {
                    existingProductReview.setUserId(productReview.getUserId());
                }
                if (productReview.getReviewer() != null) {
                    existingProductReview.setReviewer(productReview.getReviewer());
                }
                if (productReview.getRating() != null) {
                    existingProductReview.setRating(productReview.getRating());
                }
                if (productReview.getComment() != null) {
                    existingProductReview.setComment(productReview.getComment());
                }
                if (productReview.getCreatedTime() != null) {
                    existingProductReview.setCreatedTime(productReview.getCreatedTime());
                }
                if (productReview.getUpdatedTime() != null) {
                    existingProductReview.setUpdatedTime(productReview.getUpdatedTime());
                }
                if (productReview.getCreatedBy() != null) {
                    existingProductReview.setCreatedBy(productReview.getCreatedBy());
                }
                if (productReview.getUpdatedBy() != null) {
                    existingProductReview.setUpdatedBy(productReview.getUpdatedBy());
                }

                return existingProductReview;
            })
            .map(productReviewRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, productReview.getId().toString())
        );
    }

    /**
     * {@code GET  /product-reviews} : get all the productReviews.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productReviews in body.
     */
    @GetMapping("/product-reviews")
    public List<ProductReview> getAllProductReviews() {
        log.debug("REST request to get all ProductReviews");
        return productReviewRepository.findAll();
    }

    /**
     * {@code GET  /product-reviews/:id} : get the "id" productReview.
     *
     * @param id the id of the productReview to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productReview, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/product-reviews/{id}")
    public ResponseEntity<ProductReview> getProductReview(@PathVariable Long id) {
        log.debug("REST request to get ProductReview : {}", id);
        Optional<ProductReview> productReview = productReviewRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(productReview);
    }

    /**
     * {@code DELETE  /product-reviews/:id} : delete the "id" productReview.
     *
     * @param id the id of the productReview to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/product-reviews/{id}")
    public ResponseEntity<Void> deleteProductReview(@PathVariable Long id) {
        log.debug("REST request to delete ProductReview : {}", id);
        productReviewRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
