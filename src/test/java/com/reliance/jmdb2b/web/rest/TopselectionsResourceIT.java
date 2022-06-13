package com.reliance.jmdb2b.web.rest;

import static com.reliance.jmdb2b.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.reliance.jmdb2b.IntegrationTest;
import com.reliance.jmdb2b.domain.Topselections;
import com.reliance.jmdb2b.repository.TopselectionsRepository;
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
 * Integration tests for the {@link TopselectionsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TopselectionsResourceIT {

    private static final Long DEFAULT_PRODUCT_ID = 1L;
    private static final Long UPDATED_PRODUCT_ID = 2L;

    private static final Long DEFAULT_CATEGORY_ID = 1L;
    private static final Long UPDATED_CATEGORY_ID = 2L;

    private static final Integer DEFAULT_PRIORITY = 1;
    private static final Integer UPDATED_PRIORITY = 2;

    private static final ZonedDateTime DEFAULT_CREATED_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/topselections";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TopselectionsRepository topselectionsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTopselectionsMockMvc;

    private Topselections topselections;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Topselections createEntity(EntityManager em) {
        Topselections topselections = new Topselections()
            .productId(DEFAULT_PRODUCT_ID)
            .categoryId(DEFAULT_CATEGORY_ID)
            .priority(DEFAULT_PRIORITY)
            .createdTime(DEFAULT_CREATED_TIME)
            .updatedTime(DEFAULT_UPDATED_TIME)
            .createdBy(DEFAULT_CREATED_BY)
            .updatedBy(DEFAULT_UPDATED_BY);
        return topselections;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Topselections createUpdatedEntity(EntityManager em) {
        Topselections topselections = new Topselections()
            .productId(UPDATED_PRODUCT_ID)
            .categoryId(UPDATED_CATEGORY_ID)
            .priority(UPDATED_PRIORITY)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);
        return topselections;
    }

    @BeforeEach
    public void initTest() {
        topselections = createEntity(em);
    }

    @Test
    @Transactional
    void createTopselections() throws Exception {
        int databaseSizeBeforeCreate = topselectionsRepository.findAll().size();
        // Create the Topselections
        restTopselectionsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(topselections)))
            .andExpect(status().isCreated());

        // Validate the Topselections in the database
        List<Topselections> topselectionsList = topselectionsRepository.findAll();
        assertThat(topselectionsList).hasSize(databaseSizeBeforeCreate + 1);
        Topselections testTopselections = topselectionsList.get(topselectionsList.size() - 1);
        assertThat(testTopselections.getProductId()).isEqualTo(DEFAULT_PRODUCT_ID);
        assertThat(testTopselections.getCategoryId()).isEqualTo(DEFAULT_CATEGORY_ID);
        assertThat(testTopselections.getPriority()).isEqualTo(DEFAULT_PRIORITY);
        assertThat(testTopselections.getCreatedTime()).isEqualTo(DEFAULT_CREATED_TIME);
        assertThat(testTopselections.getUpdatedTime()).isEqualTo(DEFAULT_UPDATED_TIME);
        assertThat(testTopselections.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testTopselections.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    void createTopselectionsWithExistingId() throws Exception {
        // Create the Topselections with an existing ID
        topselections.setId(1L);

        int databaseSizeBeforeCreate = topselectionsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTopselectionsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(topselections)))
            .andExpect(status().isBadRequest());

        // Validate the Topselections in the database
        List<Topselections> topselectionsList = topselectionsRepository.findAll();
        assertThat(topselectionsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTopselections() throws Exception {
        // Initialize the database
        topselectionsRepository.saveAndFlush(topselections);

        // Get all the topselectionsList
        restTopselectionsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(topselections.getId().intValue())))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.[*].categoryId").value(hasItem(DEFAULT_CATEGORY_ID.intValue())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(sameInstant(DEFAULT_CREATED_TIME))))
            .andExpect(jsonPath("$.[*].updatedTime").value(hasItem(sameInstant(DEFAULT_UPDATED_TIME))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }

    @Test
    @Transactional
    void getTopselections() throws Exception {
        // Initialize the database
        topselectionsRepository.saveAndFlush(topselections);

        // Get the topselections
        restTopselectionsMockMvc
            .perform(get(ENTITY_API_URL_ID, topselections.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(topselections.getId().intValue()))
            .andExpect(jsonPath("$.productId").value(DEFAULT_PRODUCT_ID.intValue()))
            .andExpect(jsonPath("$.categoryId").value(DEFAULT_CATEGORY_ID.intValue()))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY))
            .andExpect(jsonPath("$.createdTime").value(sameInstant(DEFAULT_CREATED_TIME)))
            .andExpect(jsonPath("$.updatedTime").value(sameInstant(DEFAULT_UPDATED_TIME)))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingTopselections() throws Exception {
        // Get the topselections
        restTopselectionsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTopselections() throws Exception {
        // Initialize the database
        topselectionsRepository.saveAndFlush(topselections);

        int databaseSizeBeforeUpdate = topselectionsRepository.findAll().size();

        // Update the topselections
        Topselections updatedTopselections = topselectionsRepository.findById(topselections.getId()).get();
        // Disconnect from session so that the updates on updatedTopselections are not directly saved in db
        em.detach(updatedTopselections);
        updatedTopselections
            .productId(UPDATED_PRODUCT_ID)
            .categoryId(UPDATED_CATEGORY_ID)
            .priority(UPDATED_PRIORITY)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restTopselectionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTopselections.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTopselections))
            )
            .andExpect(status().isOk());

        // Validate the Topselections in the database
        List<Topselections> topselectionsList = topselectionsRepository.findAll();
        assertThat(topselectionsList).hasSize(databaseSizeBeforeUpdate);
        Topselections testTopselections = topselectionsList.get(topselectionsList.size() - 1);
        assertThat(testTopselections.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testTopselections.getCategoryId()).isEqualTo(UPDATED_CATEGORY_ID);
        assertThat(testTopselections.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testTopselections.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testTopselections.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
        assertThat(testTopselections.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testTopselections.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void putNonExistingTopselections() throws Exception {
        int databaseSizeBeforeUpdate = topselectionsRepository.findAll().size();
        topselections.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTopselectionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, topselections.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(topselections))
            )
            .andExpect(status().isBadRequest());

        // Validate the Topselections in the database
        List<Topselections> topselectionsList = topselectionsRepository.findAll();
        assertThat(topselectionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTopselections() throws Exception {
        int databaseSizeBeforeUpdate = topselectionsRepository.findAll().size();
        topselections.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTopselectionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(topselections))
            )
            .andExpect(status().isBadRequest());

        // Validate the Topselections in the database
        List<Topselections> topselectionsList = topselectionsRepository.findAll();
        assertThat(topselectionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTopselections() throws Exception {
        int databaseSizeBeforeUpdate = topselectionsRepository.findAll().size();
        topselections.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTopselectionsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(topselections)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Topselections in the database
        List<Topselections> topselectionsList = topselectionsRepository.findAll();
        assertThat(topselectionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTopselectionsWithPatch() throws Exception {
        // Initialize the database
        topselectionsRepository.saveAndFlush(topselections);

        int databaseSizeBeforeUpdate = topselectionsRepository.findAll().size();

        // Update the topselections using partial update
        Topselections partialUpdatedTopselections = new Topselections();
        partialUpdatedTopselections.setId(topselections.getId());

        partialUpdatedTopselections
            .priority(UPDATED_PRIORITY)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restTopselectionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTopselections.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTopselections))
            )
            .andExpect(status().isOk());

        // Validate the Topselections in the database
        List<Topselections> topselectionsList = topselectionsRepository.findAll();
        assertThat(topselectionsList).hasSize(databaseSizeBeforeUpdate);
        Topselections testTopselections = topselectionsList.get(topselectionsList.size() - 1);
        assertThat(testTopselections.getProductId()).isEqualTo(DEFAULT_PRODUCT_ID);
        assertThat(testTopselections.getCategoryId()).isEqualTo(DEFAULT_CATEGORY_ID);
        assertThat(testTopselections.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testTopselections.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testTopselections.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
        assertThat(testTopselections.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testTopselections.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void fullUpdateTopselectionsWithPatch() throws Exception {
        // Initialize the database
        topselectionsRepository.saveAndFlush(topselections);

        int databaseSizeBeforeUpdate = topselectionsRepository.findAll().size();

        // Update the topselections using partial update
        Topselections partialUpdatedTopselections = new Topselections();
        partialUpdatedTopselections.setId(topselections.getId());

        partialUpdatedTopselections
            .productId(UPDATED_PRODUCT_ID)
            .categoryId(UPDATED_CATEGORY_ID)
            .priority(UPDATED_PRIORITY)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restTopselectionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTopselections.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTopselections))
            )
            .andExpect(status().isOk());

        // Validate the Topselections in the database
        List<Topselections> topselectionsList = topselectionsRepository.findAll();
        assertThat(topselectionsList).hasSize(databaseSizeBeforeUpdate);
        Topselections testTopselections = topselectionsList.get(topselectionsList.size() - 1);
        assertThat(testTopselections.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testTopselections.getCategoryId()).isEqualTo(UPDATED_CATEGORY_ID);
        assertThat(testTopselections.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testTopselections.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testTopselections.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
        assertThat(testTopselections.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testTopselections.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingTopselections() throws Exception {
        int databaseSizeBeforeUpdate = topselectionsRepository.findAll().size();
        topselections.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTopselectionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, topselections.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(topselections))
            )
            .andExpect(status().isBadRequest());

        // Validate the Topselections in the database
        List<Topselections> topselectionsList = topselectionsRepository.findAll();
        assertThat(topselectionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTopselections() throws Exception {
        int databaseSizeBeforeUpdate = topselectionsRepository.findAll().size();
        topselections.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTopselectionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(topselections))
            )
            .andExpect(status().isBadRequest());

        // Validate the Topselections in the database
        List<Topselections> topselectionsList = topselectionsRepository.findAll();
        assertThat(topselectionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTopselections() throws Exception {
        int databaseSizeBeforeUpdate = topselectionsRepository.findAll().size();
        topselections.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTopselectionsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(topselections))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Topselections in the database
        List<Topselections> topselectionsList = topselectionsRepository.findAll();
        assertThat(topselectionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTopselections() throws Exception {
        // Initialize the database
        topselectionsRepository.saveAndFlush(topselections);

        int databaseSizeBeforeDelete = topselectionsRepository.findAll().size();

        // Delete the topselections
        restTopselectionsMockMvc
            .perform(delete(ENTITY_API_URL_ID, topselections.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Topselections> topselectionsList = topselectionsRepository.findAll();
        assertThat(topselectionsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
