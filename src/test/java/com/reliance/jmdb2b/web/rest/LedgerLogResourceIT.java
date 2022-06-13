package com.reliance.jmdb2b.web.rest;

import static com.reliance.jmdb2b.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.reliance.jmdb2b.IntegrationTest;
import com.reliance.jmdb2b.domain.LedgerLog;
import com.reliance.jmdb2b.domain.enumeration.CLIENTNAME;
import com.reliance.jmdb2b.domain.enumeration.TRANSACTIONTYPE;
import com.reliance.jmdb2b.repository.LedgerLogRepository;
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
 * Integration tests for the {@link LedgerLogResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LedgerLogResourceIT {

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    private static final Double DEFAULT_TOTAL_BALANCE = 1D;
    private static final Double UPDATED_TOTAL_BALANCE = 2D;

    private static final ZonedDateTime DEFAULT_TRANSACTION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TRANSACTION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final TRANSACTIONTYPE DEFAULT_TRANSACTION_TYPE = TRANSACTIONTYPE.CREDIT;
    private static final TRANSACTIONTYPE UPDATED_TRANSACTION_TYPE = TRANSACTIONTYPE.DEBIT;

    private static final Long DEFAULT_CLIENT_TRANSACTION_ID = 1L;
    private static final Long UPDATED_CLIENT_TRANSACTION_ID = 2L;

    private static final CLIENTNAME DEFAULT_CLIENT_NAME = CLIENTNAME.ORDERDEBIT;
    private static final CLIENTNAME UPDATED_CLIENT_NAME = CLIENTNAME.LEDGER_CREDIT_PG;

    private static final String ENTITY_API_URL = "/api/ledger-logs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LedgerLogRepository ledgerLogRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLedgerLogMockMvc;

    private LedgerLog ledgerLog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LedgerLog createEntity(EntityManager em) {
        LedgerLog ledgerLog = new LedgerLog()
            .amount(DEFAULT_AMOUNT)
            .totalBalance(DEFAULT_TOTAL_BALANCE)
            .transactionDate(DEFAULT_TRANSACTION_DATE)
            .transactionType(DEFAULT_TRANSACTION_TYPE)
            .clientTransactionId(DEFAULT_CLIENT_TRANSACTION_ID)
            .clientName(DEFAULT_CLIENT_NAME);
        return ledgerLog;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LedgerLog createUpdatedEntity(EntityManager em) {
        LedgerLog ledgerLog = new LedgerLog()
            .amount(UPDATED_AMOUNT)
            .totalBalance(UPDATED_TOTAL_BALANCE)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .clientTransactionId(UPDATED_CLIENT_TRANSACTION_ID)
            .clientName(UPDATED_CLIENT_NAME);
        return ledgerLog;
    }

    @BeforeEach
    public void initTest() {
        ledgerLog = createEntity(em);
    }

    @Test
    @Transactional
    void createLedgerLog() throws Exception {
        int databaseSizeBeforeCreate = ledgerLogRepository.findAll().size();
        // Create the LedgerLog
        restLedgerLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ledgerLog)))
            .andExpect(status().isCreated());

        // Validate the LedgerLog in the database
        List<LedgerLog> ledgerLogList = ledgerLogRepository.findAll();
        assertThat(ledgerLogList).hasSize(databaseSizeBeforeCreate + 1);
        LedgerLog testLedgerLog = ledgerLogList.get(ledgerLogList.size() - 1);
        assertThat(testLedgerLog.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testLedgerLog.getTotalBalance()).isEqualTo(DEFAULT_TOTAL_BALANCE);
        assertThat(testLedgerLog.getTransactionDate()).isEqualTo(DEFAULT_TRANSACTION_DATE);
        assertThat(testLedgerLog.getTransactionType()).isEqualTo(DEFAULT_TRANSACTION_TYPE);
        assertThat(testLedgerLog.getClientTransactionId()).isEqualTo(DEFAULT_CLIENT_TRANSACTION_ID);
        assertThat(testLedgerLog.getClientName()).isEqualTo(DEFAULT_CLIENT_NAME);
    }

    @Test
    @Transactional
    void createLedgerLogWithExistingId() throws Exception {
        // Create the LedgerLog with an existing ID
        ledgerLog.setId(1L);

        int databaseSizeBeforeCreate = ledgerLogRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLedgerLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ledgerLog)))
            .andExpect(status().isBadRequest());

        // Validate the LedgerLog in the database
        List<LedgerLog> ledgerLogList = ledgerLogRepository.findAll();
        assertThat(ledgerLogList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLedgerLogs() throws Exception {
        // Initialize the database
        ledgerLogRepository.saveAndFlush(ledgerLog);

        // Get all the ledgerLogList
        restLedgerLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ledgerLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].totalBalance").value(hasItem(DEFAULT_TOTAL_BALANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(sameInstant(DEFAULT_TRANSACTION_DATE))))
            .andExpect(jsonPath("$.[*].transactionType").value(hasItem(DEFAULT_TRANSACTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].clientTransactionId").value(hasItem(DEFAULT_CLIENT_TRANSACTION_ID.intValue())))
            .andExpect(jsonPath("$.[*].clientName").value(hasItem(DEFAULT_CLIENT_NAME.toString())));
    }

    @Test
    @Transactional
    void getLedgerLog() throws Exception {
        // Initialize the database
        ledgerLogRepository.saveAndFlush(ledgerLog);

        // Get the ledgerLog
        restLedgerLogMockMvc
            .perform(get(ENTITY_API_URL_ID, ledgerLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ledgerLog.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.totalBalance").value(DEFAULT_TOTAL_BALANCE.doubleValue()))
            .andExpect(jsonPath("$.transactionDate").value(sameInstant(DEFAULT_TRANSACTION_DATE)))
            .andExpect(jsonPath("$.transactionType").value(DEFAULT_TRANSACTION_TYPE.toString()))
            .andExpect(jsonPath("$.clientTransactionId").value(DEFAULT_CLIENT_TRANSACTION_ID.intValue()))
            .andExpect(jsonPath("$.clientName").value(DEFAULT_CLIENT_NAME.toString()));
    }

    @Test
    @Transactional
    void getNonExistingLedgerLog() throws Exception {
        // Get the ledgerLog
        restLedgerLogMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLedgerLog() throws Exception {
        // Initialize the database
        ledgerLogRepository.saveAndFlush(ledgerLog);

        int databaseSizeBeforeUpdate = ledgerLogRepository.findAll().size();

        // Update the ledgerLog
        LedgerLog updatedLedgerLog = ledgerLogRepository.findById(ledgerLog.getId()).get();
        // Disconnect from session so that the updates on updatedLedgerLog are not directly saved in db
        em.detach(updatedLedgerLog);
        updatedLedgerLog
            .amount(UPDATED_AMOUNT)
            .totalBalance(UPDATED_TOTAL_BALANCE)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .clientTransactionId(UPDATED_CLIENT_TRANSACTION_ID)
            .clientName(UPDATED_CLIENT_NAME);

        restLedgerLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLedgerLog.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLedgerLog))
            )
            .andExpect(status().isOk());

        // Validate the LedgerLog in the database
        List<LedgerLog> ledgerLogList = ledgerLogRepository.findAll();
        assertThat(ledgerLogList).hasSize(databaseSizeBeforeUpdate);
        LedgerLog testLedgerLog = ledgerLogList.get(ledgerLogList.size() - 1);
        assertThat(testLedgerLog.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testLedgerLog.getTotalBalance()).isEqualTo(UPDATED_TOTAL_BALANCE);
        assertThat(testLedgerLog.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testLedgerLog.getTransactionType()).isEqualTo(UPDATED_TRANSACTION_TYPE);
        assertThat(testLedgerLog.getClientTransactionId()).isEqualTo(UPDATED_CLIENT_TRANSACTION_ID);
        assertThat(testLedgerLog.getClientName()).isEqualTo(UPDATED_CLIENT_NAME);
    }

    @Test
    @Transactional
    void putNonExistingLedgerLog() throws Exception {
        int databaseSizeBeforeUpdate = ledgerLogRepository.findAll().size();
        ledgerLog.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLedgerLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ledgerLog.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ledgerLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the LedgerLog in the database
        List<LedgerLog> ledgerLogList = ledgerLogRepository.findAll();
        assertThat(ledgerLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLedgerLog() throws Exception {
        int databaseSizeBeforeUpdate = ledgerLogRepository.findAll().size();
        ledgerLog.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLedgerLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ledgerLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the LedgerLog in the database
        List<LedgerLog> ledgerLogList = ledgerLogRepository.findAll();
        assertThat(ledgerLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLedgerLog() throws Exception {
        int databaseSizeBeforeUpdate = ledgerLogRepository.findAll().size();
        ledgerLog.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLedgerLogMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ledgerLog)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LedgerLog in the database
        List<LedgerLog> ledgerLogList = ledgerLogRepository.findAll();
        assertThat(ledgerLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLedgerLogWithPatch() throws Exception {
        // Initialize the database
        ledgerLogRepository.saveAndFlush(ledgerLog);

        int databaseSizeBeforeUpdate = ledgerLogRepository.findAll().size();

        // Update the ledgerLog using partial update
        LedgerLog partialUpdatedLedgerLog = new LedgerLog();
        partialUpdatedLedgerLog.setId(ledgerLog.getId());

        partialUpdatedLedgerLog
            .amount(UPDATED_AMOUNT)
            .totalBalance(UPDATED_TOTAL_BALANCE)
            .clientTransactionId(UPDATED_CLIENT_TRANSACTION_ID)
            .clientName(UPDATED_CLIENT_NAME);

        restLedgerLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLedgerLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLedgerLog))
            )
            .andExpect(status().isOk());

        // Validate the LedgerLog in the database
        List<LedgerLog> ledgerLogList = ledgerLogRepository.findAll();
        assertThat(ledgerLogList).hasSize(databaseSizeBeforeUpdate);
        LedgerLog testLedgerLog = ledgerLogList.get(ledgerLogList.size() - 1);
        assertThat(testLedgerLog.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testLedgerLog.getTotalBalance()).isEqualTo(UPDATED_TOTAL_BALANCE);
        assertThat(testLedgerLog.getTransactionDate()).isEqualTo(DEFAULT_TRANSACTION_DATE);
        assertThat(testLedgerLog.getTransactionType()).isEqualTo(DEFAULT_TRANSACTION_TYPE);
        assertThat(testLedgerLog.getClientTransactionId()).isEqualTo(UPDATED_CLIENT_TRANSACTION_ID);
        assertThat(testLedgerLog.getClientName()).isEqualTo(UPDATED_CLIENT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateLedgerLogWithPatch() throws Exception {
        // Initialize the database
        ledgerLogRepository.saveAndFlush(ledgerLog);

        int databaseSizeBeforeUpdate = ledgerLogRepository.findAll().size();

        // Update the ledgerLog using partial update
        LedgerLog partialUpdatedLedgerLog = new LedgerLog();
        partialUpdatedLedgerLog.setId(ledgerLog.getId());

        partialUpdatedLedgerLog
            .amount(UPDATED_AMOUNT)
            .totalBalance(UPDATED_TOTAL_BALANCE)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .clientTransactionId(UPDATED_CLIENT_TRANSACTION_ID)
            .clientName(UPDATED_CLIENT_NAME);

        restLedgerLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLedgerLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLedgerLog))
            )
            .andExpect(status().isOk());

        // Validate the LedgerLog in the database
        List<LedgerLog> ledgerLogList = ledgerLogRepository.findAll();
        assertThat(ledgerLogList).hasSize(databaseSizeBeforeUpdate);
        LedgerLog testLedgerLog = ledgerLogList.get(ledgerLogList.size() - 1);
        assertThat(testLedgerLog.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testLedgerLog.getTotalBalance()).isEqualTo(UPDATED_TOTAL_BALANCE);
        assertThat(testLedgerLog.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testLedgerLog.getTransactionType()).isEqualTo(UPDATED_TRANSACTION_TYPE);
        assertThat(testLedgerLog.getClientTransactionId()).isEqualTo(UPDATED_CLIENT_TRANSACTION_ID);
        assertThat(testLedgerLog.getClientName()).isEqualTo(UPDATED_CLIENT_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingLedgerLog() throws Exception {
        int databaseSizeBeforeUpdate = ledgerLogRepository.findAll().size();
        ledgerLog.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLedgerLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ledgerLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ledgerLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the LedgerLog in the database
        List<LedgerLog> ledgerLogList = ledgerLogRepository.findAll();
        assertThat(ledgerLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLedgerLog() throws Exception {
        int databaseSizeBeforeUpdate = ledgerLogRepository.findAll().size();
        ledgerLog.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLedgerLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ledgerLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the LedgerLog in the database
        List<LedgerLog> ledgerLogList = ledgerLogRepository.findAll();
        assertThat(ledgerLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLedgerLog() throws Exception {
        int databaseSizeBeforeUpdate = ledgerLogRepository.findAll().size();
        ledgerLog.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLedgerLogMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ledgerLog))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LedgerLog in the database
        List<LedgerLog> ledgerLogList = ledgerLogRepository.findAll();
        assertThat(ledgerLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLedgerLog() throws Exception {
        // Initialize the database
        ledgerLogRepository.saveAndFlush(ledgerLog);

        int databaseSizeBeforeDelete = ledgerLogRepository.findAll().size();

        // Delete the ledgerLog
        restLedgerLogMockMvc
            .perform(delete(ENTITY_API_URL_ID, ledgerLog.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LedgerLog> ledgerLogList = ledgerLogRepository.findAll();
        assertThat(ledgerLogList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
