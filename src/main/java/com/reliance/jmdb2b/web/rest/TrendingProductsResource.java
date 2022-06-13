package com.reliance.jmdb2b.web.rest;

import com.reliance.jmdb2b.domain.TrendingProducts;
import com.reliance.jmdb2b.repository.TrendingProductsRepository;
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
 * REST controller for managing {@link com.reliance.jmdb2b.domain.TrendingProducts}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TrendingProductsResource {

    private final Logger log = LoggerFactory.getLogger(TrendingProductsResource.class);

    private static final String ENTITY_NAME = "trendingProducts";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TrendingProductsRepository trendingProductsRepository;

    public TrendingProductsResource(TrendingProductsRepository trendingProductsRepository) {
        this.trendingProductsRepository = trendingProductsRepository;
    }

    /**
     * {@code POST  /trending-products} : Create a new trendingProducts.
     *
     * @param trendingProducts the trendingProducts to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new trendingProducts, or with status {@code 400 (Bad Request)} if the trendingProducts has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/trending-products")
    public ResponseEntity<TrendingProducts> createTrendingProducts(@RequestBody TrendingProducts trendingProducts)
        throws URISyntaxException {
        log.debug("REST request to save TrendingProducts : {}", trendingProducts);
        if (trendingProducts.getId() != null) {
            throw new BadRequestAlertException("A new trendingProducts cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TrendingProducts result = trendingProductsRepository.save(trendingProducts);
        return ResponseEntity
            .created(new URI("/api/trending-products/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /trending-products/:id} : Updates an existing trendingProducts.
     *
     * @param id the id of the trendingProducts to save.
     * @param trendingProducts the trendingProducts to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trendingProducts,
     * or with status {@code 400 (Bad Request)} if the trendingProducts is not valid,
     * or with status {@code 500 (Internal Server Error)} if the trendingProducts couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/trending-products/{id}")
    public ResponseEntity<TrendingProducts> updateTrendingProducts(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TrendingProducts trendingProducts
    ) throws URISyntaxException {
        log.debug("REST request to update TrendingProducts : {}, {}", id, trendingProducts);
        if (trendingProducts.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trendingProducts.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trendingProductsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TrendingProducts result = trendingProductsRepository.save(trendingProducts);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, trendingProducts.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /trending-products/:id} : Partial updates given fields of an existing trendingProducts, field will ignore if it is null
     *
     * @param id the id of the trendingProducts to save.
     * @param trendingProducts the trendingProducts to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trendingProducts,
     * or with status {@code 400 (Bad Request)} if the trendingProducts is not valid,
     * or with status {@code 404 (Not Found)} if the trendingProducts is not found,
     * or with status {@code 500 (Internal Server Error)} if the trendingProducts couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/trending-products/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TrendingProducts> partialUpdateTrendingProducts(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TrendingProducts trendingProducts
    ) throws URISyntaxException {
        log.debug("REST request to partial update TrendingProducts partially : {}, {}", id, trendingProducts);
        if (trendingProducts.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trendingProducts.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trendingProductsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TrendingProducts> result = trendingProductsRepository
            .findById(trendingProducts.getId())
            .map(existingTrendingProducts -> {
                if (trendingProducts.getProductId() != null) {
                    existingTrendingProducts.setProductId(trendingProducts.getProductId());
                }
                if (trendingProducts.getCategoryId() != null) {
                    existingTrendingProducts.setCategoryId(trendingProducts.getCategoryId());
                }
                if (trendingProducts.getSoldQuantity() != null) {
                    existingTrendingProducts.setSoldQuantity(trendingProducts.getSoldQuantity());
                }
                if (trendingProducts.getViewCount() != null) {
                    existingTrendingProducts.setViewCount(trendingProducts.getViewCount());
                }
                if (trendingProducts.getSoldDate() != null) {
                    existingTrendingProducts.setSoldDate(trendingProducts.getSoldDate());
                }
                if (trendingProducts.getCreatedTime() != null) {
                    existingTrendingProducts.setCreatedTime(trendingProducts.getCreatedTime());
                }
                if (trendingProducts.getUpdatedTime() != null) {
                    existingTrendingProducts.setUpdatedTime(trendingProducts.getUpdatedTime());
                }
                if (trendingProducts.getCreatedBy() != null) {
                    existingTrendingProducts.setCreatedBy(trendingProducts.getCreatedBy());
                }
                if (trendingProducts.getUpdatedBy() != null) {
                    existingTrendingProducts.setUpdatedBy(trendingProducts.getUpdatedBy());
                }

                return existingTrendingProducts;
            })
            .map(trendingProductsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, trendingProducts.getId().toString())
        );
    }

    /**
     * {@code GET  /trending-products} : get all the trendingProducts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of trendingProducts in body.
     */
    @GetMapping("/trending-products")
    public List<TrendingProducts> getAllTrendingProducts() {
        log.debug("REST request to get all TrendingProducts");
        return trendingProductsRepository.findAll();
    }

    /**
     * {@code GET  /trending-products/:id} : get the "id" trendingProducts.
     *
     * @param id the id of the trendingProducts to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the trendingProducts, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/trending-products/{id}")
    public ResponseEntity<TrendingProducts> getTrendingProducts(@PathVariable Long id) {
        log.debug("REST request to get TrendingProducts : {}", id);
        Optional<TrendingProducts> trendingProducts = trendingProductsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(trendingProducts);
    }

    /**
     * {@code DELETE  /trending-products/:id} : delete the "id" trendingProducts.
     *
     * @param id the id of the trendingProducts to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/trending-products/{id}")
    public ResponseEntity<Void> deleteTrendingProducts(@PathVariable Long id) {
        log.debug("REST request to delete TrendingProducts : {}", id);
        trendingProductsRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
