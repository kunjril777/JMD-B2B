package com.reliance.jmdb2b.web.rest;

import com.reliance.jmdb2b.domain.ItemsBackInStock;
import com.reliance.jmdb2b.repository.ItemsBackInStockRepository;
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
 * REST controller for managing {@link com.reliance.jmdb2b.domain.ItemsBackInStock}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ItemsBackInStockResource {

    private final Logger log = LoggerFactory.getLogger(ItemsBackInStockResource.class);

    private static final String ENTITY_NAME = "itemsBackInStock";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ItemsBackInStockRepository itemsBackInStockRepository;

    public ItemsBackInStockResource(ItemsBackInStockRepository itemsBackInStockRepository) {
        this.itemsBackInStockRepository = itemsBackInStockRepository;
    }

    /**
     * {@code POST  /items-back-in-stocks} : Create a new itemsBackInStock.
     *
     * @param itemsBackInStock the itemsBackInStock to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new itemsBackInStock, or with status {@code 400 (Bad Request)} if the itemsBackInStock has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/items-back-in-stocks")
    public ResponseEntity<ItemsBackInStock> createItemsBackInStock(@RequestBody ItemsBackInStock itemsBackInStock)
        throws URISyntaxException {
        log.debug("REST request to save ItemsBackInStock : {}", itemsBackInStock);
        if (itemsBackInStock.getId() != null) {
            throw new BadRequestAlertException("A new itemsBackInStock cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ItemsBackInStock result = itemsBackInStockRepository.save(itemsBackInStock);
        return ResponseEntity
            .created(new URI("/api/items-back-in-stocks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /items-back-in-stocks/:id} : Updates an existing itemsBackInStock.
     *
     * @param id the id of the itemsBackInStock to save.
     * @param itemsBackInStock the itemsBackInStock to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemsBackInStock,
     * or with status {@code 400 (Bad Request)} if the itemsBackInStock is not valid,
     * or with status {@code 500 (Internal Server Error)} if the itemsBackInStock couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/items-back-in-stocks/{id}")
    public ResponseEntity<ItemsBackInStock> updateItemsBackInStock(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ItemsBackInStock itemsBackInStock
    ) throws URISyntaxException {
        log.debug("REST request to update ItemsBackInStock : {}, {}", id, itemsBackInStock);
        if (itemsBackInStock.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itemsBackInStock.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!itemsBackInStockRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ItemsBackInStock result = itemsBackInStockRepository.save(itemsBackInStock);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, itemsBackInStock.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /items-back-in-stocks/:id} : Partial updates given fields of an existing itemsBackInStock, field will ignore if it is null
     *
     * @param id the id of the itemsBackInStock to save.
     * @param itemsBackInStock the itemsBackInStock to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemsBackInStock,
     * or with status {@code 400 (Bad Request)} if the itemsBackInStock is not valid,
     * or with status {@code 404 (Not Found)} if the itemsBackInStock is not found,
     * or with status {@code 500 (Internal Server Error)} if the itemsBackInStock couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/items-back-in-stocks/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ItemsBackInStock> partialUpdateItemsBackInStock(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ItemsBackInStock itemsBackInStock
    ) throws URISyntaxException {
        log.debug("REST request to partial update ItemsBackInStock partially : {}, {}", id, itemsBackInStock);
        if (itemsBackInStock.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itemsBackInStock.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!itemsBackInStockRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ItemsBackInStock> result = itemsBackInStockRepository
            .findById(itemsBackInStock.getId())
            .map(existingItemsBackInStock -> {
                if (itemsBackInStock.getUserId() != null) {
                    existingItemsBackInStock.setUserId(itemsBackInStock.getUserId());
                }
                if (itemsBackInStock.getProductId() != null) {
                    existingItemsBackInStock.setProductId(itemsBackInStock.getProductId());
                }
                if (itemsBackInStock.getStockStatus() != null) {
                    existingItemsBackInStock.setStockStatus(itemsBackInStock.getStockStatus());
                }
                if (itemsBackInStock.getStatusUpdateTime() != null) {
                    existingItemsBackInStock.setStatusUpdateTime(itemsBackInStock.getStatusUpdateTime());
                }
                if (itemsBackInStock.getCreatedTime() != null) {
                    existingItemsBackInStock.setCreatedTime(itemsBackInStock.getCreatedTime());
                }
                if (itemsBackInStock.getUpdatedTime() != null) {
                    existingItemsBackInStock.setUpdatedTime(itemsBackInStock.getUpdatedTime());
                }
                if (itemsBackInStock.getCreatedBy() != null) {
                    existingItemsBackInStock.setCreatedBy(itemsBackInStock.getCreatedBy());
                }
                if (itemsBackInStock.getUpdatedBy() != null) {
                    existingItemsBackInStock.setUpdatedBy(itemsBackInStock.getUpdatedBy());
                }

                return existingItemsBackInStock;
            })
            .map(itemsBackInStockRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, itemsBackInStock.getId().toString())
        );
    }

    /**
     * {@code GET  /items-back-in-stocks} : get all the itemsBackInStocks.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of itemsBackInStocks in body.
     */
    @GetMapping("/items-back-in-stocks")
    public List<ItemsBackInStock> getAllItemsBackInStocks() {
        log.debug("REST request to get all ItemsBackInStocks");
        return itemsBackInStockRepository.findAll();
    }

    /**
     * {@code GET  /items-back-in-stocks/:id} : get the "id" itemsBackInStock.
     *
     * @param id the id of the itemsBackInStock to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the itemsBackInStock, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/items-back-in-stocks/{id}")
    public ResponseEntity<ItemsBackInStock> getItemsBackInStock(@PathVariable Long id) {
        log.debug("REST request to get ItemsBackInStock : {}", id);
        Optional<ItemsBackInStock> itemsBackInStock = itemsBackInStockRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(itemsBackInStock);
    }

    /**
     * {@code DELETE  /items-back-in-stocks/:id} : delete the "id" itemsBackInStock.
     *
     * @param id the id of the itemsBackInStock to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/items-back-in-stocks/{id}")
    public ResponseEntity<Void> deleteItemsBackInStock(@PathVariable Long id) {
        log.debug("REST request to delete ItemsBackInStock : {}", id);
        itemsBackInStockRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
