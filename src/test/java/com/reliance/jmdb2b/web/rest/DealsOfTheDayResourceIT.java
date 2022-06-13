package com.reliance.jmdb2b.web.rest;

import static com.reliance.jmdb2b.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.reliance.jmdb2b.IntegrationTest;
import com.reliance.jmdb2b.domain.DealsOfTheDay;
import com.reliance.jmdb2b.repository.DealsOfTheDayRepository;
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
 * Integration tests for the {@link DealsOfTheDayResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DealsOfTheDayResourceIT {

    private static final Long DEFAULT_PRODUCT_ID = 1L;
    private static final Long UPDATED_PRODUCT_ID = 2L;

    private static final Long DEFAULT_CATEGORY_ID = 1L;
    private static final Long UPDATED_CATEGORY_ID = 2L;

    private static final ZonedDateTime DEFAULT_DEAL_START_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DEAL_START_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DEAL_END_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DEAL_END_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

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

    private static final String ENTITY_API_URL = "/api/deals-of-the-days";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DealsOfTheDayRepository dealsOfTheDayRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDealsOfTheDayMockMvc;

    private DealsOfTheDay dealsOfTheDay;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DealsOfTheDay createEntity(EntityManager em) {
        DealsOfTheDay dealsOfTheDay = new DealsOfTheDay()
            .productId(DEFAULT_PRODUCT_ID)
            .categoryId(DEFAULT_CATEGORY_ID)
            .dealStartTime(DEFAULT_DEAL_START_TIME)
            .dealEndTime(DEFAULT_DEAL_END_TIME)
            .priority(DEFAULT_PRIORITY)
            .createdTime(DEFAULT_CREATED_TIME)
            .updatedTime(DEFAULT_UPDATED_TIME)
            .createdBy(DEFAULT_CREATED_BY)
            .updatedBy(DEFAULT_UPDATED_BY);
        return dealsOfTheDay;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DealsOfTheDay createUpdatedEntity(EntityManager em) {
        DealsOfTheDay dealsOfTheDay = new DealsOfTheDay()
            .productId(UPDATED_PRODUCT_ID)
            .categoryId(UPDATED_CATEGORY_ID)
            .dealStartTime(UPDATED_DEAL_START_TIME)
            .dealEndTime(UPDATED_DEAL_END_TIME)
            .priority(UPDATED_PRIORITY)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);
        return dealsOfTheDay;
    }

    @BeforeEach
    public void initTest() {
        dealsOfTheDay = createEntity(em);
    }

    @Test
    @Transactional
    void createDealsOfTheDay() throws Exception {
        int databaseSizeBeforeCreate = dealsOfTheDayRepository.findAll().size();
        // Create the DealsOfTheDay
        restDealsOfTheDayMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dealsOfTheDay)))
            .andExpect(status().isCreated());

        // Validate the DealsOfTheDay in the database
        List<DealsOfTheDay> dealsOfTheDayList = dealsOfTheDayRepository.findAll();
        assertThat(dealsOfTheDayList).hasSize(databaseSizeBeforeCreate + 1);
        DealsOfTheDay testDealsOfTheDay = dealsOfTheDayList.get(dealsOfTheDayList.size() - 1);
        assertThat(testDealsOfTheDay.getProductId()).isEqualTo(DEFAULT_PRODUCT_ID);
        assertThat(testDealsOfTheDay.getCategoryId()).isEqualTo(DEFAULT_CATEGORY_ID);
        assertThat(testDealsOfTheDay.getDealStartTime()).isEqualTo(DEFAULT_DEAL_START_TIME);
        assertThat(testDealsOfTheDay.getDealEndTime()).isEqualTo(DEFAULT_DEAL_END_TIME);
        assertThat(testDealsOfTheDay.getPriority()).isEqualTo(DEFAULT_PRIORITY);
        assertThat(testDealsOfTheDay.getCreatedTime()).isEqualTo(DEFAULT_CREATED_TIME);
        assertThat(testDealsOfTheDay.getUpdatedTime()).isEqualTo(DEFAULT_UPDATED_TIME);
        assertThat(testDealsOfTheDay.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testDealsOfTheDay.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    void createDealsOfTheDayWithExistingId() throws Exception {
        // Create the DealsOfTheDay with an existing ID
        dealsOfTheDay.setId(1L);

        int databaseSizeBeforeCreate = dealsOfTheDayRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDealsOfTheDayMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dealsOfTheDay)))
            .andExpect(status().isBadRequest());

        // Validate the DealsOfTheDay in the database
        List<DealsOfTheDay> dealsOfTheDayList = dealsOfTheDayRepository.findAll();
        assertThat(dealsOfTheDayList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDealsOfTheDays() throws Exception {
        // Initialize the database
        dealsOfTheDayRepository.saveAndFlush(dealsOfTheDay);

        // Get all the dealsOfTheDayList
        restDealsOfTheDayMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dealsOfTheDay.getId().intValue())))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.[*].categoryId").value(hasItem(DEFAULT_CATEGORY_ID.intValue())))
            .andExpect(jsonPath("$.[*].dealStartTime").value(hasItem(sameInstant(DEFAULT_DEAL_START_TIME))))
            .andExpect(jsonPath("$.[*].dealEndTime").value(hasItem(sameInstant(DEFAULT_DEAL_END_TIME))))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(sameInstant(DEFAULT_CREATED_TIME))))
            .andExpect(jsonPath("$.[*].updatedTime").value(hasItem(sameInstant(DEFAULT_UPDATED_TIME))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }

    @Test
    @Transactional
    void getDealsOfTheDay() throws Exception {
        // Initialize the database
        dealsOfTheDayRepository.saveAndFlush(dealsOfTheDay);

        // Get the dealsOfTheDay
        restDealsOfTheDayMockMvc
            .perform(get(ENTITY_API_URL_ID, dealsOfTheDay.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dealsOfTheDay.getId().intValue()))
            .andExpect(jsonPath("$.productId").value(DEFAULT_PRODUCT_ID.intValue()))
            .andExpect(jsonPath("$.categoryId").value(DEFAULT_CATEGORY_ID.intValue()))
            .andExpect(jsonPath("$.dealStartTime").value(sameInstant(DEFAULT_DEAL_START_TIME)))
            .andExpect(jsonPath("$.dealEndTime").value(sameInstant(DEFAULT_DEAL_END_TIME)))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY))
            .andExpect(jsonPath("$.createdTime").value(sameInstant(DEFAULT_CREATED_TIME)))
            .andExpect(jsonPath("$.updatedTime").value(sameInstant(DEFAULT_UPDATED_TIME)))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingDealsOfTheDay() throws Exception {
        // Get the dealsOfTheDay
        restDealsOfTheDayMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDealsOfTheDay() throws Exception {
        // Initialize the database
        dealsOfTheDayRepository.saveAndFlush(dealsOfTheDay);

        int databaseSizeBeforeUpdate = dealsOfTheDayRepository.findAll().size();

        // Update the dealsOfTheDay
        DealsOfTheDay updatedDealsOfTheDay = dealsOfTheDayRepository.findById(dealsOfTheDay.getId()).get();
        // Disconnect from session so that the updates on updatedDealsOfTheDay are not directly saved in db
        em.detach(updatedDealsOfTheDay);
        updatedDealsOfTheDay
            .productId(UPDATED_PRODUCT_ID)
            .categoryId(UPDATED_CATEGORY_ID)
            .dealStartTime(UPDATED_DEAL_START_TIME)
            .dealEndTime(UPDATED_DEAL_END_TIME)
            .priority(UPDATED_PRIORITY)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restDealsOfTheDayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDealsOfTheDay.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDealsOfTheDay))
            )
            .andExpect(status().isOk());

        // Validate the DealsOfTheDay in the database
        List<DealsOfTheDay> dealsOfTheDayList = dealsOfTheDayRepository.findAll();
        assertThat(dealsOfTheDayList).hasSize(databaseSizeBeforeUpdate);
        DealsOfTheDay testDealsOfTheDay = dealsOfTheDayList.get(dealsOfTheDayList.size() - 1);
        assertThat(testDealsOfTheDay.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testDealsOfTheDay.getCategoryId()).isEqualTo(UPDATED_CATEGORY_ID);
        assertThat(testDealsOfTheDay.getDealStartTime()).isEqualTo(UPDATED_DEAL_START_TIME);
        assertThat(testDealsOfTheDay.getDealEndTime()).isEqualTo(UPDATED_DEAL_END_TIME);
        assertThat(testDealsOfTheDay.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testDealsOfTheDay.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testDealsOfTheDay.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
        assertThat(testDealsOfTheDay.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testDealsOfTheDay.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void putNonExistingDealsOfTheDay() throws Exception {
        int databaseSizeBeforeUpdate = dealsOfTheDayRepository.findAll().size();
        dealsOfTheDay.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDealsOfTheDayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dealsOfTheDay.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dealsOfTheDay))
            )
            .andExpect(status().isBadRequest());

        // Validate the DealsOfTheDay in the database
        List<DealsOfTheDay> dealsOfTheDayList = dealsOfTheDayRepository.findAll();
        assertThat(dealsOfTheDayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDealsOfTheDay() throws Exception {
        int databaseSizeBeforeUpdate = dealsOfTheDayRepository.findAll().size();
        dealsOfTheDay.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDealsOfTheDayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dealsOfTheDay))
            )
            .andExpect(status().isBadRequest());

        // Validate the DealsOfTheDay in the database
        List<DealsOfTheDay> dealsOfTheDayList = dealsOfTheDayRepository.findAll();
        assertThat(dealsOfTheDayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDealsOfTheDay() throws Exception {
        int databaseSizeBeforeUpdate = dealsOfTheDayRepository.findAll().size();
        dealsOfTheDay.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDealsOfTheDayMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dealsOfTheDay)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DealsOfTheDay in the database
        List<DealsOfTheDay> dealsOfTheDayList = dealsOfTheDayRepository.findAll();
        assertThat(dealsOfTheDayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDealsOfTheDayWithPatch() throws Exception {
        // Initialize the database
        dealsOfTheDayRepository.saveAndFlush(dealsOfTheDay);

        int databaseSizeBeforeUpdate = dealsOfTheDayRepository.findAll().size();

        // Update the dealsOfTheDay using partial update
        DealsOfTheDay partialUpdatedDealsOfTheDay = new DealsOfTheDay();
        partialUpdatedDealsOfTheDay.setId(dealsOfTheDay.getId());

        partialUpdatedDealsOfTheDay
            .productId(UPDATED_PRODUCT_ID)
            .categoryId(UPDATED_CATEGORY_ID)
            .priority(UPDATED_PRIORITY)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restDealsOfTheDayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDealsOfTheDay.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDealsOfTheDay))
            )
            .andExpect(status().isOk());

        // Validate the DealsOfTheDay in the database
        List<DealsOfTheDay> dealsOfTheDayList = dealsOfTheDayRepository.findAll();
        assertThat(dealsOfTheDayList).hasSize(databaseSizeBeforeUpdate);
        DealsOfTheDay testDealsOfTheDay = dealsOfTheDayList.get(dealsOfTheDayList.size() - 1);
        assertThat(testDealsOfTheDay.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testDealsOfTheDay.getCategoryId()).isEqualTo(UPDATED_CATEGORY_ID);
        assertThat(testDealsOfTheDay.getDealStartTime()).isEqualTo(DEFAULT_DEAL_START_TIME);
        assertThat(testDealsOfTheDay.getDealEndTime()).isEqualTo(DEFAULT_DEAL_END_TIME);
        assertThat(testDealsOfTheDay.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testDealsOfTheDay.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testDealsOfTheDay.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
        assertThat(testDealsOfTheDay.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testDealsOfTheDay.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void fullUpdateDealsOfTheDayWithPatch() throws Exception {
        // Initialize the database
        dealsOfTheDayRepository.saveAndFlush(dealsOfTheDay);

        int databaseSizeBeforeUpdate = dealsOfTheDayRepository.findAll().size();

        // Update the dealsOfTheDay using partial update
        DealsOfTheDay partialUpdatedDealsOfTheDay = new DealsOfTheDay();
        partialUpdatedDealsOfTheDay.setId(dealsOfTheDay.getId());

        partialUpdatedDealsOfTheDay
            .productId(UPDATED_PRODUCT_ID)
            .categoryId(UPDATED_CATEGORY_ID)
            .dealStartTime(UPDATED_DEAL_START_TIME)
            .dealEndTime(UPDATED_DEAL_END_TIME)
            .priority(UPDATED_PRIORITY)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restDealsOfTheDayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDealsOfTheDay.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDealsOfTheDay))
            )
            .andExpect(status().isOk());

        // Validate the DealsOfTheDay in the database
        List<DealsOfTheDay> dealsOfTheDayList = dealsOfTheDayRepository.findAll();
        assertThat(dealsOfTheDayList).hasSize(databaseSizeBeforeUpdate);
        DealsOfTheDay testDealsOfTheDay = dealsOfTheDayList.get(dealsOfTheDayList.size() - 1);
        assertThat(testDealsOfTheDay.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testDealsOfTheDay.getCategoryId()).isEqualTo(UPDATED_CATEGORY_ID);
        assertThat(testDealsOfTheDay.getDealStartTime()).isEqualTo(UPDATED_DEAL_START_TIME);
        assertThat(testDealsOfTheDay.getDealEndTime()).isEqualTo(UPDATED_DEAL_END_TIME);
        assertThat(testDealsOfTheDay.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testDealsOfTheDay.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testDealsOfTheDay.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
        assertThat(testDealsOfTheDay.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testDealsOfTheDay.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingDealsOfTheDay() throws Exception {
        int databaseSizeBeforeUpdate = dealsOfTheDayRepository.findAll().size();
        dealsOfTheDay.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDealsOfTheDayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dealsOfTheDay.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dealsOfTheDay))
            )
            .andExpect(status().isBadRequest());

        // Validate the DealsOfTheDay in the database
        List<DealsOfTheDay> dealsOfTheDayList = dealsOfTheDayRepository.findAll();
        assertThat(dealsOfTheDayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDealsOfTheDay() throws Exception {
        int databaseSizeBeforeUpdate = dealsOfTheDayRepository.findAll().size();
        dealsOfTheDay.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDealsOfTheDayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dealsOfTheDay))
            )
            .andExpect(status().isBadRequest());

        // Validate the DealsOfTheDay in the database
        List<DealsOfTheDay> dealsOfTheDayList = dealsOfTheDayRepository.findAll();
        assertThat(dealsOfTheDayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDealsOfTheDay() throws Exception {
        int databaseSizeBeforeUpdate = dealsOfTheDayRepository.findAll().size();
        dealsOfTheDay.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDealsOfTheDayMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(dealsOfTheDay))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DealsOfTheDay in the database
        List<DealsOfTheDay> dealsOfTheDayList = dealsOfTheDayRepository.findAll();
        assertThat(dealsOfTheDayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDealsOfTheDay() throws Exception {
        // Initialize the database
        dealsOfTheDayRepository.saveAndFlush(dealsOfTheDay);

        int databaseSizeBeforeDelete = dealsOfTheDayRepository.findAll().size();

        // Delete the dealsOfTheDay
        restDealsOfTheDayMockMvc
            .perform(delete(ENTITY_API_URL_ID, dealsOfTheDay.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DealsOfTheDay> dealsOfTheDayList = dealsOfTheDayRepository.findAll();
        assertThat(dealsOfTheDayList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
