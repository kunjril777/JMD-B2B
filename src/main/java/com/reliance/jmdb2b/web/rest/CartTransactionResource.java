package com.reliance.jmdb2b.web.rest;

import com.reliance.jmdb2b.domain.CartTransaction;
import com.reliance.jmdb2b.repository.CartTransactionRepository;
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
 * REST controller for managing {@link com.reliance.jmdb2b.domain.CartTransaction}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CartTransactionResource {

    private final Logger log = LoggerFactory.getLogger(CartTransactionResource.class);

    private static final String ENTITY_NAME = "cartTransaction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CartTransactionRepository cartTransactionRepository;

    public CartTransactionResource(CartTransactionRepository cartTransactionRepository) {
        this.cartTransactionRepository = cartTransactionRepository;
    }

    /**
     * {@code POST  /cart-transactions} : Create a new cartTransaction.
     *
     * @param cartTransaction the cartTransaction to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cartTransaction, or with status {@code 400 (Bad Request)} if the cartTransaction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cart-transactions")
    public ResponseEntity<CartTransaction> createCartTransaction(@RequestBody CartTransaction cartTransaction) throws URISyntaxException {
        log.debug("REST request to save CartTransaction : {}", cartTransaction);
        if (cartTransaction.getId() != null) {
            throw new BadRequestAlertException("A new cartTransaction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CartTransaction result = cartTransactionRepository.save(cartTransaction);
        return ResponseEntity
            .created(new URI("/api/cart-transactions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cart-transactions/:id} : Updates an existing cartTransaction.
     *
     * @param id the id of the cartTransaction to save.
     * @param cartTransaction the cartTransaction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cartTransaction,
     * or with status {@code 400 (Bad Request)} if the cartTransaction is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cartTransaction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cart-transactions/{id}")
    public ResponseEntity<CartTransaction> updateCartTransaction(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CartTransaction cartTransaction
    ) throws URISyntaxException {
        log.debug("REST request to update CartTransaction : {}, {}", id, cartTransaction);
        if (cartTransaction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cartTransaction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cartTransactionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CartTransaction result = cartTransactionRepository.save(cartTransaction);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cartTransaction.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cart-transactions/:id} : Partial updates given fields of an existing cartTransaction, field will ignore if it is null
     *
     * @param id the id of the cartTransaction to save.
     * @param cartTransaction the cartTransaction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cartTransaction,
     * or with status {@code 400 (Bad Request)} if the cartTransaction is not valid,
     * or with status {@code 404 (Not Found)} if the cartTransaction is not found,
     * or with status {@code 500 (Internal Server Error)} if the cartTransaction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cart-transactions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CartTransaction> partialUpdateCartTransaction(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CartTransaction cartTransaction
    ) throws URISyntaxException {
        log.debug("REST request to partial update CartTransaction partially : {}, {}", id, cartTransaction);
        if (cartTransaction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cartTransaction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cartTransactionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CartTransaction> result = cartTransactionRepository
            .findById(cartTransaction.getId())
            .map(existingCartTransaction -> {
                if (cartTransaction.getCartTotalQuantity() != null) {
                    existingCartTransaction.setCartTotalQuantity(cartTransaction.getCartTotalQuantity());
                }
                if (cartTransaction.getCartTotalPrice() != null) {
                    existingCartTransaction.setCartTotalPrice(cartTransaction.getCartTotalPrice());
                }
                if (cartTransaction.getBillingAddressId() != null) {
                    existingCartTransaction.setBillingAddressId(cartTransaction.getBillingAddressId());
                }
                if (cartTransaction.getShippingAddressId() != null) {
                    existingCartTransaction.setShippingAddressId(cartTransaction.getShippingAddressId());
                }
                if (cartTransaction.getDeliveryCharge() != null) {
                    existingCartTransaction.setDeliveryCharge(cartTransaction.getDeliveryCharge());
                }
                if (cartTransaction.getCouponCode() != null) {
                    existingCartTransaction.setCouponCode(cartTransaction.getCouponCode());
                }
                if (cartTransaction.getCartFinalTotal() != null) {
                    existingCartTransaction.setCartFinalTotal(cartTransaction.getCartFinalTotal());
                }
                if (cartTransaction.getCreatedTime() != null) {
                    existingCartTransaction.setCreatedTime(cartTransaction.getCreatedTime());
                }
                if (cartTransaction.getUpdatedTime() != null) {
                    existingCartTransaction.setUpdatedTime(cartTransaction.getUpdatedTime());
                }
                if (cartTransaction.getCreatedBy() != null) {
                    existingCartTransaction.setCreatedBy(cartTransaction.getCreatedBy());
                }
                if (cartTransaction.getUpdatedBy() != null) {
                    existingCartTransaction.setUpdatedBy(cartTransaction.getUpdatedBy());
                }

                return existingCartTransaction;
            })
            .map(cartTransactionRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cartTransaction.getId().toString())
        );
    }

    /**
     * {@code GET  /cart-transactions} : get all the cartTransactions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cartTransactions in body.
     */
    @GetMapping("/cart-transactions")
    public List<CartTransaction> getAllCartTransactions() {
        log.debug("REST request to get all CartTransactions");
        return cartTransactionRepository.findAll();
    }

    /**
     * {@code GET  /cart-transactions/:id} : get the "id" cartTransaction.
     *
     * @param id the id of the cartTransaction to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cartTransaction, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cart-transactions/{id}")
    public ResponseEntity<CartTransaction> getCartTransaction(@PathVariable Long id) {
        log.debug("REST request to get CartTransaction : {}", id);
        Optional<CartTransaction> cartTransaction = cartTransactionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(cartTransaction);
    }

    /**
     * {@code DELETE  /cart-transactions/:id} : delete the "id" cartTransaction.
     *
     * @param id the id of the cartTransaction to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cart-transactions/{id}")
    public ResponseEntity<Void> deleteCartTransaction(@PathVariable Long id) {
        log.debug("REST request to delete CartTransaction : {}", id);
        cartTransactionRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
