package com.reliance.jmdb2b.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.reliance.jmdb2b.IntegrationTest;
import com.reliance.jmdb2b.domain.Ledger;
import com.reliance.jmdb2b.repository.LedgerRepository;
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
 * Integration tests for the {@link LedgerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LedgerResourceIT {

    private static final Double DEFAULT_JIO_CREDITS = 1D;
    private static final Double UPDATED_JIO_CREDITS = 2D;

    private static final String ENTITY_API_URL = "/api/ledgers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LedgerRepository ledgerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLedgerMockMvc;

    private Ledger ledger;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ledger createEntity(EntityManager em) {
        Ledger ledger = new Ledger().jioCredits(DEFAULT_JIO_CREDITS);
        return ledger;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ledger createUpdatedEntity(EntityManager em) {
        Ledger ledger = new Ledger().jioCredits(UPDATED_JIO_CREDITS);
        return ledger;
    }

    @BeforeEach
    public void initTest() {
        ledger = createEntity(em);
    }

    @Test
    @Transactional
    void createLedger() throws Exception {
        int databaseSizeBeforeCreate = ledgerRepository.findAll().size();
        // Create the Ledger
        restLedgerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ledger)))
            .andExpect(status().isCreated());

        // Validate the Ledger in the database
        List<Ledger> ledgerList = ledgerRepository.findAll();
        assertThat(ledgerList).hasSize(databaseSizeBeforeCreate + 1);
        Ledger testLedger = ledgerList.get(ledgerList.size() - 1);
        assertThat(testLedger.getJioCredits()).isEqualTo(DEFAULT_JIO_CREDITS);
    }

    @Test
    @Transactional
    void createLedgerWithExistingId() throws Exception {
        // Create the Ledger with an existing ID
        ledger.setId(1L);

        int databaseSizeBeforeCreate = ledgerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLedgerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ledger)))
            .andExpect(status().isBadRequest());

        // Validate the Ledger in the database
        List<Ledger> ledgerList = ledgerRepository.findAll();
        assertThat(ledgerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLedgers() throws Exception {
        // Initialize the database
        ledgerRepository.saveAndFlush(ledger);

        // Get all the ledgerList
        restLedgerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ledger.getId().intValue())))
            .andExpect(jsonPath("$.[*].jioCredits").value(hasItem(DEFAULT_JIO_CREDITS.doubleValue())));
    }

    @Test
    @Transactional
    void getLedger() throws Exception {
        // Initialize the database
        ledgerRepository.saveAndFlush(ledger);

        // Get the ledger
        restLedgerMockMvc
            .perform(get(ENTITY_API_URL_ID, ledger.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ledger.getId().intValue()))
            .andExpect(jsonPath("$.jioCredits").value(DEFAULT_JIO_CREDITS.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingLedger() throws Exception {
        // Get the ledger
        restLedgerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLedger() throws Exception {
        // Initialize the database
        ledgerRepository.saveAndFlush(ledger);

        int databaseSizeBeforeUpdate = ledgerRepository.findAll().size();

        // Update the ledger
        Ledger updatedLedger = ledgerRepository.findById(ledger.getId()).get();
        // Disconnect from session so that the updates on updatedLedger are not directly saved in db
        em.detach(updatedLedger);
        updatedLedger.jioCredits(UPDATED_JIO_CREDITS);

        restLedgerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLedger.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLedger))
            )
            .andExpect(status().isOk());

        // Validate the Ledger in the database
        List<Ledger> ledgerList = ledgerRepository.findAll();
        assertThat(ledgerList).hasSize(databaseSizeBeforeUpdate);
        Ledger testLedger = ledgerList.get(ledgerList.size() - 1);
        assertThat(testLedger.getJioCredits()).isEqualTo(UPDATED_JIO_CREDITS);
    }

    @Test
    @Transactional
    void putNonExistingLedger() throws Exception {
        int databaseSizeBeforeUpdate = ledgerRepository.findAll().size();
        ledger.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLedgerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ledger.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ledger))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ledger in the database
        List<Ledger> ledgerList = ledgerRepository.findAll();
        assertThat(ledgerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLedger() throws Exception {
        int databaseSizeBeforeUpdate = ledgerRepository.findAll().size();
        ledger.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLedgerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ledger))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ledger in the database
        List<Ledger> ledgerList = ledgerRepository.findAll();
        assertThat(ledgerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLedger() throws Exception {
        int databaseSizeBeforeUpdate = ledgerRepository.findAll().size();
        ledger.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLedgerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ledger)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ledger in the database
        List<Ledger> ledgerList = ledgerRepository.findAll();
        assertThat(ledgerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLedgerWithPatch() throws Exception {
        // Initialize the database
        ledgerRepository.saveAndFlush(ledger);

        int databaseSizeBeforeUpdate = ledgerRepository.findAll().size();

        // Update the ledger using partial update
        Ledger partialUpdatedLedger = new Ledger();
        partialUpdatedLedger.setId(ledger.getId());

        restLedgerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLedger.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLedger))
            )
            .andExpect(status().isOk());

        // Validate the Ledger in the database
        List<Ledger> ledgerList = ledgerRepository.findAll();
        assertThat(ledgerList).hasSize(databaseSizeBeforeUpdate);
        Ledger testLedger = ledgerList.get(ledgerList.size() - 1);
        assertThat(testLedger.getJioCredits()).isEqualTo(DEFAULT_JIO_CREDITS);
    }

    @Test
    @Transactional
    void fullUpdateLedgerWithPatch() throws Exception {
        // Initialize the database
        ledgerRepository.saveAndFlush(ledger);

        int databaseSizeBeforeUpdate = ledgerRepository.findAll().size();

        // Update the ledger using partial update
        Ledger partialUpdatedLedger = new Ledger();
        partialUpdatedLedger.setId(ledger.getId());

        partialUpdatedLedger.jioCredits(UPDATED_JIO_CREDITS);

        restLedgerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLedger.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLedger))
            )
            .andExpect(status().isOk());

        // Validate the Ledger in the database
        List<Ledger> ledgerList = ledgerRepository.findAll();
        assertThat(ledgerList).hasSize(databaseSizeBeforeUpdate);
        Ledger testLedger = ledgerList.get(ledgerList.size() - 1);
        assertThat(testLedger.getJioCredits()).isEqualTo(UPDATED_JIO_CREDITS);
    }

    @Test
    @Transactional
    void patchNonExistingLedger() throws Exception {
        int databaseSizeBeforeUpdate = ledgerRepository.findAll().size();
        ledger.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLedgerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ledger.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ledger))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ledger in the database
        List<Ledger> ledgerList = ledgerRepository.findAll();
        assertThat(ledgerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLedger() throws Exception {
        int databaseSizeBeforeUpdate = ledgerRepository.findAll().size();
        ledger.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLedgerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ledger))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ledger in the database
        List<Ledger> ledgerList = ledgerRepository.findAll();
        assertThat(ledgerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLedger() throws Exception {
        int databaseSizeBeforeUpdate = ledgerRepository.findAll().size();
        ledger.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLedgerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ledger)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ledger in the database
        List<Ledger> ledgerList = ledgerRepository.findAll();
        assertThat(ledgerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLedger() throws Exception {
        // Initialize the database
        ledgerRepository.saveAndFlush(ledger);

        int databaseSizeBeforeDelete = ledgerRepository.findAll().size();

        // Delete the ledger
        restLedgerMockMvc
            .perform(delete(ENTITY_API_URL_ID, ledger.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ledger> ledgerList = ledgerRepository.findAll();
        assertThat(ledgerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
