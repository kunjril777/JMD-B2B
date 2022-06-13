package com.reliance.jmdb2b.web.rest;

import static com.reliance.jmdb2b.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.reliance.jmdb2b.IntegrationTest;
import com.reliance.jmdb2b.domain.LedgerTransaction;
import com.reliance.jmdb2b.domain.enumeration.CLIENTNAME;
import com.reliance.jmdb2b.domain.enumeration.LTSTATUS;
import com.reliance.jmdb2b.domain.enumeration.TRANSACTIONTYPE;
import com.reliance.jmdb2b.repository.LedgerTransactionRepository;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link LedgerTransactionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LedgerTransactionResourceIT {

    private static final Long DEFAULT_AMOUNT = 1L;
    private static final Long UPDATED_AMOUNT = 2L;

    private static final TRANSACTIONTYPE DEFAULT_TRANSACTION_TYPE = TRANSACTIONTYPE.CREDIT;
    private static final TRANSACTIONTYPE UPDATED_TRANSACTION_TYPE = TRANSACTIONTYPE.DEBIT;

    private static final LTSTATUS DEFAULT_LEDGER_TRANSACTION_STATUS = LTSTATUS.SUCCESSFUL;
    private static final LTSTATUS UPDATED_LEDGER_TRANSACTION_STATUS = LTSTATUS.PENDING;

    private static final CLIENTNAME DEFAULT_CLIENT_NAME = CLIENTNAME.ORDERDEBIT;
    private static final CLIENTNAME UPDATED_CLIENT_NAME = CLIENTNAME.LEDGER_CREDIT_PG;

    private static final ZonedDateTime DEFAULT_CREATED_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ledger-transactions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LedgerTransactionRepository ledgerTransactionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLedgerTransactionMockMvc;

    private LedgerTransaction ledgerTransaction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LedgerTransaction createEntity(EntityManager em) {
        LedgerTransaction ledgerTransaction = new LedgerTransaction()
            .amount(DEFAULT_AMOUNT)
            .transactionType(DEFAULT_TRANSACTION_TYPE)
            .ledgerTransactionStatus(DEFAULT_LEDGER_TRANSACTION_STATUS)
            .clientName(DEFAULT_CLIENT_NAME)
            .createdTime(DEFAULT_CREATED_TIME)
            .updatedTime(DEFAULT_UPDATED_TIME)
            .createdBy(DEFAULT_CREATED_BY)
            .updatedBy(DEFAULT_UPDATED_BY);
        return ledgerTransaction;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LedgerTransaction createUpdatedEntity(EntityManager em) {
        LedgerTransaction ledgerTransaction = new LedgerTransaction()
            .amount(UPDATED_AMOUNT)
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .ledgerTransactionStatus(UPDATED_LEDGER_TRANSACTION_STATUS)
            .clientName(UPDATED_CLIENT_NAME)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);
        return ledgerTransaction;
    }

    @BeforeEach
    public void initTest() {
        ledgerTransaction = createEntity(em);
    }

    @Test
    @Transactional
    void createLedgerTransaction() throws Exception {
        int databaseSizeBeforeCreate = ledgerTransactionRepository.findAll().size();
        // Create the LedgerTransaction
        restLedgerTransactionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ledgerTransaction))
            )
            .andExpect(status().isCreated());

        // Validate the LedgerTransaction in the database
        List<LedgerTransaction> ledgerTransactionList = ledgerTransactionRepository.findAll();
        assertThat(ledgerTransactionList).hasSize(databaseSizeBeforeCreate + 1);
        LedgerTransaction testLedgerTransaction = ledgerTransactionList.get(ledgerTransactionList.size() - 1);
        assertThat(testLedgerTransaction.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testLedgerTransaction.getTransactionType()).isEqualTo(DEFAULT_TRANSACTION_TYPE);
        assertThat(testLedgerTransaction.getLedgerTransactionStatus()).isEqualTo(DEFAULT_LEDGER_TRANSACTION_STATUS);
        assertThat(testLedgerTransaction.getClientName()).isEqualTo(DEFAULT_CLIENT_NAME);
        assertThat(testLedgerTransaction.getCreatedTime()).isEqualTo(DEFAULT_CREATED_TIME);
        assertThat(testLedgerTransaction.getUpdatedTime()).isEqualTo(DEFAULT_UPDATED_TIME);
        assertThat(testLedgerTransaction.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testLedgerTransaction.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    void createLedgerTransactionWithExistingId() throws Exception {
        // Create the LedgerTransaction with an existing ID
        ledgerTransaction.setId(1L);

        int databaseSizeBeforeCreate = ledgerTransactionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLedgerTransactionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ledgerTransaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the LedgerTransaction in the database
        List<LedgerTransaction> ledgerTransactionList = ledgerTransactionRepository.findAll();
        assertThat(ledgerTransactionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLedgerTransactions() throws Exception {
        // Initialize the database
        ledgerTransactionRepository.saveAndFlush(ledgerTransaction);

        // Get all the ledgerTransactionList
        restLedgerTransactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ledgerTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].transactionType").value(hasItem(DEFAULT_TRANSACTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].ledgerTransactionStatus").value(hasItem(DEFAULT_LEDGER_TRANSACTION_STATUS.toString())))
            .andExpect(jsonPath("$.[*].clientName").value(hasItem(DEFAULT_CLIENT_NAME.toString())))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(sameInstant(DEFAULT_CREATED_TIME))))
            .andExpect(jsonPath("$.[*].updatedTime").value(hasItem(sameInstant(DEFAULT_UPDATED_TIME))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }

    @Test
    @Transactional
    void getLedgerTransaction() throws Exception {
        // Initialize the database
        ledgerTransactionRepository.saveAndFlush(ledgerTransaction);

        // Get the ledgerTransaction
        restLedgerTransactionMockMvc
            .perform(get(ENTITY_API_URL_ID, ledgerTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ledgerTransaction.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.transactionType").value(DEFAULT_TRANSACTION_TYPE.toString()))
            .andExpect(jsonPath("$.ledgerTransactionStatus").value(DEFAULT_LEDGER_TRANSACTION_STATUS.toString()))
            .andExpect(jsonPath("$.clientName").value(DEFAULT_CLIENT_NAME.toString()))
            .andExpect(jsonPath("$.createdTime").value(sameInstant(DEFAULT_CREATED_TIME)))
            .andExpect(jsonPath("$.updatedTime").value(sameInstant(DEFAULT_UPDATED_TIME)))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingLedgerTransaction() throws Exception {
        // Get the ledgerTransaction
        restLedgerTransactionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLedgerTransaction() throws Exception {
        // Initialize the database
        ledgerTransactionRepository.saveAndFlush(ledgerTransaction);

        int databaseSizeBeforeUpdate = ledgerTransactionRepository.findAll().size();

        // Update the ledgerTransaction
        LedgerTransaction updatedLedgerTransaction = ledgerTransactionRepository.findById(ledgerTransaction.getId()).get();
        // Disconnect from session so that the updates on updatedLedgerTransaction are not directly saved in db
        em.detach(updatedLedgerTransaction);
        updatedLedgerTransaction
            .amount(UPDATED_AMOUNT)
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .ledgerTransactionStatus(UPDATED_LEDGER_TRANSACTION_STATUS)
            .clientName(UPDATED_CLIENT_NAME)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restLedgerTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLedgerTransaction.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLedgerTransaction))
            )
            .andExpect(status().isOk());

        // Validate the LedgerTransaction in the database
        List<LedgerTransaction> ledgerTransactionList = ledgerTransactionRepository.findAll();
        assertThat(ledgerTransactionList).hasSize(databaseSizeBeforeUpdate);
        LedgerTransaction testLedgerTransaction = ledgerTransactionList.get(ledgerTransactionList.size() - 1);
        assertThat(testLedgerTransaction.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testLedgerTransaction.getTransactionType()).isEqualTo(UPDATED_TRANSACTION_TYPE);
        assertThat(testLedgerTransaction.getLedgerTransactionStatus()).isEqualTo(UPDATED_LEDGER_TRANSACTION_STATUS);
        assertThat(testLedgerTransaction.getClientName()).isEqualTo(UPDATED_CLIENT_NAME);
        assertThat(testLedgerTransaction.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testLedgerTransaction.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
        assertThat(testLedgerTransaction.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testLedgerTransaction.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void putNonExistingLedgerTransaction() throws Exception {
        int databaseSizeBeforeUpdate = ledgerTransactionRepository.findAll().size();
        ledgerTransaction.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLedgerTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ledgerTransaction.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ledgerTransaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the LedgerTransaction in the database
        List<LedgerTransaction> ledgerTransactionList = ledgerTransactionRepository.findAll();
        assertThat(ledgerTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLedgerTransaction() throws Exception {
        int databaseSizeBeforeUpdate = ledgerTransactionRepository.findAll().size();
        ledgerTransaction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLedgerTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ledgerTransaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the LedgerTransaction in the database
        List<LedgerTransaction> ledgerTransactionList = ledgerTransactionRepository.findAll();
        assertThat(ledgerTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLedgerTransaction() throws Exception {
        int databaseSizeBeforeUpdate = ledgerTransactionRepository.findAll().size();
        ledgerTransaction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLedgerTransactionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ledgerTransaction))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LedgerTransaction in the database
        List<LedgerTransaction> ledgerTransactionList = ledgerTransactionRepository.findAll();
        assertThat(ledgerTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLedgerTransactionWithPatch() throws Exception {
        // Initialize the database
        ledgerTransactionRepository.saveAndFlush(ledgerTransaction);

        int databaseSizeBeforeUpdate = ledgerTransactionRepository.findAll().size();

        // Update the ledgerTransaction using partial update
        LedgerTransaction partialUpdatedLedgerTransaction = new LedgerTransaction();
        partialUpdatedLedgerTransaction.setId(ledgerTransaction.getId());

        partialUpdatedLedgerTransaction
            .amount(UPDATED_AMOUNT)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .updatedBy(UPDATED_UPDATED_BY);

        restLedgerTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLedgerTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLedgerTransaction))
            )
            .andExpect(status().isOk());

        // Validate the LedgerTransaction in the database
        List<LedgerTransaction> ledgerTransactionList = ledgerTransactionRepository.findAll();
        assertThat(ledgerTransactionList).hasSize(databaseSizeBeforeUpdate);
        LedgerTransaction testLedgerTransaction = ledgerTransactionList.get(ledgerTransactionList.size() - 1);
        assertThat(testLedgerTransaction.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testLedgerTransaction.getTransactionType()).isEqualTo(DEFAULT_TRANSACTION_TYPE);
        assertThat(testLedgerTransaction.getLedgerTransactionStatus()).isEqualTo(DEFAULT_LEDGER_TRANSACTION_STATUS);
        assertThat(testLedgerTransaction.getClientName()).isEqualTo(DEFAULT_CLIENT_NAME);
        assertThat(testLedgerTransaction.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testLedgerTransaction.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
        assertThat(testLedgerTransaction.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testLedgerTransaction.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void fullUpdateLedgerTransactionWithPatch() throws Exception {
        // Initialize the database
        ledgerTransactionRepository.saveAndFlush(ledgerTransaction);

        int databaseSizeBeforeUpdate = ledgerTransactionRepository.findAll().size();

        // Update the ledgerTransaction using partial update
        LedgerTransaction partialUpdatedLedgerTransaction = new LedgerTransaction();
        partialUpdatedLedgerTransaction.setId(ledgerTransaction.getId());

        partialUpdatedLedgerTransaction
            .amount(UPDATED_AMOUNT)
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .ledgerTransactionStatus(UPDATED_LEDGER_TRANSACTION_STATUS)
            .clientName(UPDATED_CLIENT_NAME)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restLedgerTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLedgerTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLedgerTransaction))
            )
            .andExpect(status().isOk());

        // Validate the LedgerTransaction in the database
        List<LedgerTransaction> ledgerTransactionList = ledgerTransactionRepository.findAll();
        assertThat(ledgerTransactionList).hasSize(databaseSizeBeforeUpdate);
        LedgerTransaction testLedgerTransaction = ledgerTransactionList.get(ledgerTransactionList.size() - 1);
        assertThat(testLedgerTransaction.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testLedgerTransaction.getTransactionType()).isEqualTo(UPDATED_TRANSACTION_TYPE);
        assertThat(testLedgerTransaction.getLedgerTransactionStatus()).isEqualTo(UPDATED_LEDGER_TRANSACTION_STATUS);
        assertThat(testLedgerTransaction.getClientName()).isEqualTo(UPDATED_CLIENT_NAME);
        assertThat(testLedgerTransaction.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testLedgerTransaction.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
        assertThat(testLedgerTransaction.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testLedgerTransaction.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingLedgerTransaction() throws Exception {
        int databaseSizeBeforeUpdate = ledgerTransactionRepository.findAll().size();
        ledgerTransaction.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLedgerTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ledgerTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ledgerTransaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the LedgerTransaction in the database
        List<LedgerTransaction> ledgerTransactionList = ledgerTransactionRepository.findAll();
        assertThat(ledgerTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLedgerTransaction() throws Exception {
        int databaseSizeBeforeUpdate = ledgerTransactionRepository.findAll().size();
        ledgerTransaction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLedgerTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ledgerTransaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the LedgerTransaction in the database
        List<LedgerTransaction> ledgerTransactionList = ledgerTransactionRepository.findAll();
        assertThat(ledgerTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLedgerTransaction() throws Exception {
        int databaseSizeBeforeUpdate = ledgerTransactionRepository.findAll().size();
        ledgerTransaction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLedgerTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ledgerTransaction))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LedgerTransaction in the database
        List<LedgerTransaction> ledgerTransactionList = ledgerTransactionRepository.findAll();
        assertThat(ledgerTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLedgerTransaction() throws Exception {
        // Initialize the database
        ledgerTransactionRepository.saveAndFlush(ledgerTransaction);

        int databaseSizeBeforeDelete = ledgerTransactionRepository.findAll().size();

        // Delete the ledgerTransaction
        restLedgerTransactionMockMvc
            .perform(delete(ENTITY_API_URL_ID, ledgerTransaction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LedgerTransaction> ledgerTransactionList = ledgerTransactionRepository.findAll();
        assertThat(ledgerTransactionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
