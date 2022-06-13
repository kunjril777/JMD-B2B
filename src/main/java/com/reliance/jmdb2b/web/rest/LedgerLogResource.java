package com.reliance.jmdb2b.web.rest;

import com.reliance.jmdb2b.domain.LedgerLog;
import com.reliance.jmdb2b.repository.LedgerLogRepository;
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
 * REST controller for managing {@link com.reliance.jmdb2b.domain.LedgerLog}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class LedgerLogResource {

    private final Logger log = LoggerFactory.getLogger(LedgerLogResource.class);

    private static final String ENTITY_NAME = "ledgerLog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LedgerLogRepository ledgerLogRepository;

    public LedgerLogResource(LedgerLogRepository ledgerLogRepository) {
        this.ledgerLogRepository = ledgerLogRepository;
    }

    /**
     * {@code POST  /ledger-logs} : Create a new ledgerLog.
     *
     * @param ledgerLog the ledgerLog to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ledgerLog, or with status {@code 400 (Bad Request)} if the ledgerLog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ledger-logs")
    public ResponseEntity<LedgerLog> createLedgerLog(@RequestBody LedgerLog ledgerLog) throws URISyntaxException {
        log.debug("REST request to save LedgerLog : {}", ledgerLog);
        if (ledgerLog.getId() != null) {
            throw new BadRequestAlertException("A new ledgerLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LedgerLog result = ledgerLogRepository.save(ledgerLog);
        return ResponseEntity
            .created(new URI("/api/ledger-logs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ledger-logs/:id} : Updates an existing ledgerLog.
     *
     * @param id the id of the ledgerLog to save.
     * @param ledgerLog the ledgerLog to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ledgerLog,
     * or with status {@code 400 (Bad Request)} if the ledgerLog is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ledgerLog couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ledger-logs/{id}")
    public ResponseEntity<LedgerLog> updateLedgerLog(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LedgerLog ledgerLog
    ) throws URISyntaxException {
        log.debug("REST request to update LedgerLog : {}, {}", id, ledgerLog);
        if (ledgerLog.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ledgerLog.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ledgerLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LedgerLog result = ledgerLogRepository.save(ledgerLog);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, ledgerLog.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ledger-logs/:id} : Partial updates given fields of an existing ledgerLog, field will ignore if it is null
     *
     * @param id the id of the ledgerLog to save.
     * @param ledgerLog the ledgerLog to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ledgerLog,
     * or with status {@code 400 (Bad Request)} if the ledgerLog is not valid,
     * or with status {@code 404 (Not Found)} if the ledgerLog is not found,
     * or with status {@code 500 (Internal Server Error)} if the ledgerLog couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ledger-logs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LedgerLog> partialUpdateLedgerLog(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LedgerLog ledgerLog
    ) throws URISyntaxException {
        log.debug("REST request to partial update LedgerLog partially : {}, {}", id, ledgerLog);
        if (ledgerLog.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ledgerLog.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ledgerLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LedgerLog> result = ledgerLogRepository
            .findById(ledgerLog.getId())
            .map(existingLedgerLog -> {
                if (ledgerLog.getAmount() != null) {
                    existingLedgerLog.setAmount(ledgerLog.getAmount());
                }
                if (ledgerLog.getTotalBalance() != null) {
                    existingLedgerLog.setTotalBalance(ledgerLog.getTotalBalance());
                }
                if (ledgerLog.getTransactionDate() != null) {
                    existingLedgerLog.setTransactionDate(ledgerLog.getTransactionDate());
                }
                if (ledgerLog.getTransactionType() != null) {
                    existingLedgerLog.setTransactionType(ledgerLog.getTransactionType());
                }
                if (ledgerLog.getClientTransactionId() != null) {
                    existingLedgerLog.setClientTransactionId(ledgerLog.getClientTransactionId());
                }
                if (ledgerLog.getClientName() != null) {
                    existingLedgerLog.setClientName(ledgerLog.getClientName());
                }

                return existingLedgerLog;
            })
            .map(ledgerLogRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, ledgerLog.getId().toString())
        );
    }

    /**
     * {@code GET  /ledger-logs} : get all the ledgerLogs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ledgerLogs in body.
     */
    @GetMapping("/ledger-logs")
    public List<LedgerLog> getAllLedgerLogs() {
        log.debug("REST request to get all LedgerLogs");
        return ledgerLogRepository.findAll();
    }

    /**
     * {@code GET  /ledger-logs/:id} : get the "id" ledgerLog.
     *
     * @param id the id of the ledgerLog to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ledgerLog, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ledger-logs/{id}")
    public ResponseEntity<LedgerLog> getLedgerLog(@PathVariable Long id) {
        log.debug("REST request to get LedgerLog : {}", id);
        Optional<LedgerLog> ledgerLog = ledgerLogRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(ledgerLog);
    }

    /**
     * {@code DELETE  /ledger-logs/:id} : delete the "id" ledgerLog.
     *
     * @param id the id of the ledgerLog to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ledger-logs/{id}")
    public ResponseEntity<Void> deleteLedgerLog(@PathVariable Long id) {
        log.debug("REST request to delete LedgerLog : {}", id);
        ledgerLogRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
