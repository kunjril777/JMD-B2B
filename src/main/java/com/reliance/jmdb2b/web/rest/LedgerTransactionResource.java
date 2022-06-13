package com.reliance.jmdb2b.web.rest;

import com.reliance.jmdb2b.domain.LedgerTransaction;
import com.reliance.jmdb2b.repository.LedgerTransactionRepository;
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
 * REST controller for managing {@link com.reliance.jmdb2b.domain.LedgerTransaction}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class LedgerTransactionResource {

    private final Logger log = LoggerFactory.getLogger(LedgerTransactionResource.class);

    private static final String ENTITY_NAME = "ledgerTransaction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LedgerTransactionRepository ledgerTransactionRepository;

    public LedgerTransactionResource(LedgerTransactionRepository ledgerTransactionRepository) {
        this.ledgerTransactionRepository = ledgerTransactionRepository;
    }

    /**
     * {@code POST  /ledger-transactions} : Create a new ledgerTransaction.
     *
     * @param ledgerTransaction the ledgerTransaction to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ledgerTransaction, or with status {@code 400 (Bad Request)} if the ledgerTransaction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ledger-transactions")
    public ResponseEntity<LedgerTransaction> createLedgerTransaction(@RequestBody LedgerTransaction ledgerTransaction)
        throws URISyntaxException {
        log.debug("REST request to save LedgerTransaction : {}", ledgerTransaction);
        if (ledgerTransaction.getId() != null) {
            throw new BadRequestAlertException("A new ledgerTransaction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LedgerTransaction result = ledgerTransactionRepository.save(ledgerTransaction);
        return ResponseEntity
            .created(new URI("/api/ledger-transactions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ledger-transactions/:id} : Updates an existing ledgerTransaction.
     *
     * @param id the id of the ledgerTransaction to save.
     * @param ledgerTransaction the ledgerTransaction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ledgerTransaction,
     * or with status {@code 400 (Bad Request)} if the ledgerTransaction is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ledgerTransaction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ledger-transactions/{id}")
    public ResponseEntity<LedgerTransaction> updateLedgerTransaction(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LedgerTransaction ledgerTransaction
    ) throws URISyntaxException {
        log.debug("REST request to update LedgerTransaction : {}, {}", id, ledgerTransaction);
        if (ledgerTransaction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ledgerTransaction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ledgerTransactionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LedgerTransaction result = ledgerTransactionRepository.save(ledgerTransaction);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, ledgerTransaction.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ledger-transactions/:id} : Partial updates given fields of an existing ledgerTransaction, field will ignore if it is null
     *
     * @param id the id of the ledgerTransaction to save.
     * @param ledgerTransaction the ledgerTransaction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ledgerTransaction,
     * or with status {@code 400 (Bad Request)} if the ledgerTransaction is not valid,
     * or with status {@code 404 (Not Found)} if the ledgerTransaction is not found,
     * or with status {@code 500 (Internal Server Error)} if the ledgerTransaction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ledger-transactions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LedgerTransaction> partialUpdateLedgerTransaction(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LedgerTransaction ledgerTransaction
    ) throws URISyntaxException {
        log.debug("REST request to partial update LedgerTransaction partially : {}, {}", id, ledgerTransaction);
        if (ledgerTransaction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ledgerTransaction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ledgerTransactionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LedgerTransaction> result = ledgerTransactionRepository
            .findById(ledgerTransaction.getId())
            .map(existingLedgerTransaction -> {
                if (ledgerTransaction.getAmount() != null) {
                    existingLedgerTransaction.setAmount(ledgerTransaction.getAmount());
                }
                if (ledgerTransaction.getTransactionType() != null) {
                    existingLedgerTransaction.setTransactionType(ledgerTransaction.getTransactionType());
                }
                if (ledgerTransaction.getLedgerTransactionStatus() != null) {
                    existingLedgerTransaction.setLedgerTransactionStatus(ledgerTransaction.getLedgerTransactionStatus());
                }
                if (ledgerTransaction.getClientName() != null) {
                    existingLedgerTransaction.setClientName(ledgerTransaction.getClientName());
                }
                if (ledgerTransaction.getCreatedTime() != null) {
                    existingLedgerTransaction.setCreatedTime(ledgerTransaction.getCreatedTime());
                }
                if (ledgerTransaction.getUpdatedTime() != null) {
                    existingLedgerTransaction.setUpdatedTime(ledgerTransaction.getUpdatedTime());
                }
                if (ledgerTransaction.getCreatedBy() != null) {
                    existingLedgerTransaction.setCreatedBy(ledgerTransaction.getCreatedBy());
                }
                if (ledgerTransaction.getUpdatedBy() != null) {
                    existingLedgerTransaction.setUpdatedBy(ledgerTransaction.getUpdatedBy());
                }

                return existingLedgerTransaction;
            })
            .map(ledgerTransactionRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, ledgerTransaction.getId().toString())
        );
    }

    /**
     * {@code GET  /ledger-transactions} : get all the ledgerTransactions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ledgerTransactions in body.
     */
    @GetMapping("/ledger-transactions")
    public List<LedgerTransaction> getAllLedgerTransactions() {
        log.debug("REST request to get all LedgerTransactions");
        return ledgerTransactionRepository.findAll();
    }

    /**
     * {@code GET  /ledger-transactions/:id} : get the "id" ledgerTransaction.
     *
     * @param id the id of the ledgerTransaction to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ledgerTransaction, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ledger-transactions/{id}")
    public ResponseEntity<LedgerTransaction> getLedgerTransaction(@PathVariable Long id) {
        log.debug("REST request to get LedgerTransaction : {}", id);
        Optional<LedgerTransaction> ledgerTransaction = ledgerTransactionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(ledgerTransaction);
    }

    /**
     * {@code DELETE  /ledger-transactions/:id} : delete the "id" ledgerTransaction.
     *
     * @param id the id of the ledgerTransaction to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ledger-transactions/{id}")
    public ResponseEntity<Void> deleteLedgerTransaction(@PathVariable Long id) {
        log.debug("REST request to delete LedgerTransaction : {}", id);
        ledgerTransactionRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
