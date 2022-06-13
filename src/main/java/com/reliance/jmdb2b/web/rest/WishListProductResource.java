package com.reliance.jmdb2b.web.rest;

import com.reliance.jmdb2b.domain.WishListProduct;
import com.reliance.jmdb2b.repository.WishListProductRepository;
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
 * REST controller for managing {@link com.reliance.jmdb2b.domain.WishListProduct}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class WishListProductResource {

    private final Logger log = LoggerFactory.getLogger(WishListProductResource.class);

    private static final String ENTITY_NAME = "wishListProduct";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WishListProductRepository wishListProductRepository;

    public WishListProductResource(WishListProductRepository wishListProductRepository) {
        this.wishListProductRepository = wishListProductRepository;
    }

    /**
     * {@code POST  /wish-list-products} : Create a new wishListProduct.
     *
     * @param wishListProduct the wishListProduct to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new wishListProduct, or with status {@code 400 (Bad Request)} if the wishListProduct has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/wish-list-products")
    public ResponseEntity<WishListProduct> createWishListProduct(@RequestBody WishListProduct wishListProduct) throws URISyntaxException {
        log.debug("REST request to save WishListProduct : {}", wishListProduct);
        if (wishListProduct.getId() != null) {
            throw new BadRequestAlertException("A new wishListProduct cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WishListProduct result = wishListProductRepository.save(wishListProduct);
        return ResponseEntity
            .created(new URI("/api/wish-list-products/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /wish-list-products/:id} : Updates an existing wishListProduct.
     *
     * @param id the id of the wishListProduct to save.
     * @param wishListProduct the wishListProduct to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated wishListProduct,
     * or with status {@code 400 (Bad Request)} if the wishListProduct is not valid,
     * or with status {@code 500 (Internal Server Error)} if the wishListProduct couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/wish-list-products/{id}")
    public ResponseEntity<WishListProduct> updateWishListProduct(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WishListProduct wishListProduct
    ) throws URISyntaxException {
        log.debug("REST request to update WishListProduct : {}, {}", id, wishListProduct);
        if (wishListProduct.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, wishListProduct.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!wishListProductRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WishListProduct result = wishListProductRepository.save(wishListProduct);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, wishListProduct.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /wish-list-products/:id} : Partial updates given fields of an existing wishListProduct, field will ignore if it is null
     *
     * @param id the id of the wishListProduct to save.
     * @param wishListProduct the wishListProduct to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated wishListProduct,
     * or with status {@code 400 (Bad Request)} if the wishListProduct is not valid,
     * or with status {@code 404 (Not Found)} if the wishListProduct is not found,
     * or with status {@code 500 (Internal Server Error)} if the wishListProduct couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/wish-list-products/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WishListProduct> partialUpdateWishListProduct(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WishListProduct wishListProduct
    ) throws URISyntaxException {
        log.debug("REST request to partial update WishListProduct partially : {}, {}", id, wishListProduct);
        if (wishListProduct.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, wishListProduct.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!wishListProductRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WishListProduct> result = wishListProductRepository
            .findById(wishListProduct.getId())
            .map(existingWishListProduct -> {
                if (wishListProduct.getQuantity() != null) {
                    existingWishListProduct.setQuantity(wishListProduct.getQuantity());
                }
                if (wishListProduct.getProductId() != null) {
                    existingWishListProduct.setProductId(wishListProduct.getProductId());
                }
                if (wishListProduct.getPrice() != null) {
                    existingWishListProduct.setPrice(wishListProduct.getPrice());
                }
                if (wishListProduct.getCreatedTime() != null) {
                    existingWishListProduct.setCreatedTime(wishListProduct.getCreatedTime());
                }
                if (wishListProduct.getUpdatedTime() != null) {
                    existingWishListProduct.setUpdatedTime(wishListProduct.getUpdatedTime());
                }
                if (wishListProduct.getCreatedBy() != null) {
                    existingWishListProduct.setCreatedBy(wishListProduct.getCreatedBy());
                }
                if (wishListProduct.getUpdatedBy() != null) {
                    existingWishListProduct.setUpdatedBy(wishListProduct.getUpdatedBy());
                }

                return existingWishListProduct;
            })
            .map(wishListProductRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, wishListProduct.getId().toString())
        );
    }

    /**
     * {@code GET  /wish-list-products} : get all the wishListProducts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of wishListProducts in body.
     */
    @GetMapping("/wish-list-products")
    public List<WishListProduct> getAllWishListProducts() {
        log.debug("REST request to get all WishListProducts");
        return wishListProductRepository.findAll();
    }

    /**
     * {@code GET  /wish-list-products/:id} : get the "id" wishListProduct.
     *
     * @param id the id of the wishListProduct to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the wishListProduct, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/wish-list-products/{id}")
    public ResponseEntity<WishListProduct> getWishListProduct(@PathVariable Long id) {
        log.debug("REST request to get WishListProduct : {}", id);
        Optional<WishListProduct> wishListProduct = wishListProductRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(wishListProduct);
    }

    /**
     * {@code DELETE  /wish-list-products/:id} : delete the "id" wishListProduct.
     *
     * @param id the id of the wishListProduct to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/wish-list-products/{id}")
    public ResponseEntity<Void> deleteWishListProduct(@PathVariable Long id) {
        log.debug("REST request to delete WishListProduct : {}", id);
        wishListProductRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
