package com.reliance.jmdb2b.web.rest;

import com.reliance.jmdb2b.domain.CartProduct;
import com.reliance.jmdb2b.repository.CartProductRepository;
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
 * REST controller for managing {@link com.reliance.jmdb2b.domain.CartProduct}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CartProductResource {

    private final Logger log = LoggerFactory.getLogger(CartProductResource.class);

    private static final String ENTITY_NAME = "cartProduct";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CartProductRepository cartProductRepository;

    public CartProductResource(CartProductRepository cartProductRepository) {
        this.cartProductRepository = cartProductRepository;
    }

    /**
     * {@code POST  /cart-products} : Create a new cartProduct.
     *
     * @param cartProduct the cartProduct to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cartProduct, or with status {@code 400 (Bad Request)} if the cartProduct has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cart-products")
    public ResponseEntity<CartProduct> createCartProduct(@RequestBody CartProduct cartProduct) throws URISyntaxException {
        log.debug("REST request to save CartProduct : {}", cartProduct);
        if (cartProduct.getId() != null) {
            throw new BadRequestAlertException("A new cartProduct cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CartProduct result = cartProductRepository.save(cartProduct);
        return ResponseEntity
            .created(new URI("/api/cart-products/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cart-products/:id} : Updates an existing cartProduct.
     *
     * @param id the id of the cartProduct to save.
     * @param cartProduct the cartProduct to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cartProduct,
     * or with status {@code 400 (Bad Request)} if the cartProduct is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cartProduct couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cart-products/{id}")
    public ResponseEntity<CartProduct> updateCartProduct(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CartProduct cartProduct
    ) throws URISyntaxException {
        log.debug("REST request to update CartProduct : {}, {}", id, cartProduct);
        if (cartProduct.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cartProduct.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cartProductRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CartProduct result = cartProductRepository.save(cartProduct);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cartProduct.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cart-products/:id} : Partial updates given fields of an existing cartProduct, field will ignore if it is null
     *
     * @param id the id of the cartProduct to save.
     * @param cartProduct the cartProduct to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cartProduct,
     * or with status {@code 400 (Bad Request)} if the cartProduct is not valid,
     * or with status {@code 404 (Not Found)} if the cartProduct is not found,
     * or with status {@code 500 (Internal Server Error)} if the cartProduct couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cart-products/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CartProduct> partialUpdateCartProduct(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CartProduct cartProduct
    ) throws URISyntaxException {
        log.debug("REST request to partial update CartProduct partially : {}, {}", id, cartProduct);
        if (cartProduct.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cartProduct.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cartProductRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CartProduct> result = cartProductRepository
            .findById(cartProduct.getId())
            .map(existingCartProduct -> {
                if (cartProduct.getQuantity() != null) {
                    existingCartProduct.setQuantity(cartProduct.getQuantity());
                }
                if (cartProduct.getProductVariantId() != null) {
                    existingCartProduct.setProductVariantId(cartProduct.getProductVariantId());
                }
                if (cartProduct.getPrice() != null) {
                    existingCartProduct.setPrice(cartProduct.getPrice());
                }
                if (cartProduct.getCreatedTime() != null) {
                    existingCartProduct.setCreatedTime(cartProduct.getCreatedTime());
                }
                if (cartProduct.getUpdatedTime() != null) {
                    existingCartProduct.setUpdatedTime(cartProduct.getUpdatedTime());
                }
                if (cartProduct.getCreatedBy() != null) {
                    existingCartProduct.setCreatedBy(cartProduct.getCreatedBy());
                }
                if (cartProduct.getUpdatedBy() != null) {
                    existingCartProduct.setUpdatedBy(cartProduct.getUpdatedBy());
                }

                return existingCartProduct;
            })
            .map(cartProductRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cartProduct.getId().toString())
        );
    }

    /**
     * {@code GET  /cart-products} : get all the cartProducts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cartProducts in body.
     */
    @GetMapping("/cart-products")
    public List<CartProduct> getAllCartProducts() {
        log.debug("REST request to get all CartProducts");
        return cartProductRepository.findAll();
    }

    /**
     * {@code GET  /cart-products/:id} : get the "id" cartProduct.
     *
     * @param id the id of the cartProduct to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cartProduct, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cart-products/{id}")
    public ResponseEntity<CartProduct> getCartProduct(@PathVariable Long id) {
        log.debug("REST request to get CartProduct : {}", id);
        Optional<CartProduct> cartProduct = cartProductRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(cartProduct);
    }

    /**
     * {@code DELETE  /cart-products/:id} : delete the "id" cartProduct.
     *
     * @param id the id of the cartProduct to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cart-products/{id}")
    public ResponseEntity<Void> deleteCartProduct(@PathVariable Long id) {
        log.debug("REST request to delete CartProduct : {}", id);
        cartProductRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
