package com.reliance.jmdb2b.web.rest;

import static com.reliance.jmdb2b.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.reliance.jmdb2b.IntegrationTest;
import com.reliance.jmdb2b.domain.RecommmendedItems;
import com.reliance.jmdb2b.repository.RecommmendedItemsRepository;
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
 * Integration tests for the {@link RecommmendedItemsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RecommmendedItemsResourceIT {

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final Long DEFAULT_PRODUCT_ID = 1L;
    private static final Long UPDATED_PRODUCT_ID = 2L;

    private static final Long DEFAULT_CATEGORY_ID = 1L;
    private static final Long UPDATED_CATEGORY_ID = 2L;

    private static final Long DEFAULT_PRIORITY = 1L;
    private static final Long UPDATED_PRIORITY = 2L;

    private static final ZonedDateTime DEFAULT_CREATED_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/recommmended-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RecommmendedItemsRepository recommmendedItemsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRecommmendedItemsMockMvc;

    private RecommmendedItems recommmendedItems;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RecommmendedItems createEntity(EntityManager em) {
        RecommmendedItems recommmendedItems = new RecommmendedItems()
            .userId(DEFAULT_USER_ID)
            .productId(DEFAULT_PRODUCT_ID)
            .categoryId(DEFAULT_CATEGORY_ID)
            .priority(DEFAULT_PRIORITY)
            .createdTime(DEFAULT_CREATED_TIME)
            .updatedTime(DEFAULT_UPDATED_TIME)
            .createdBy(DEFAULT_CREATED_BY)
            .updatedBy(DEFAULT_UPDATED_BY);
        return recommmendedItems;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RecommmendedItems createUpdatedEntity(EntityManager em) {
        RecommmendedItems recommmendedItems = new RecommmendedItems()
            .userId(UPDATED_USER_ID)
            .productId(UPDATED_PRODUCT_ID)
            .categoryId(UPDATED_CATEGORY_ID)
            .priority(UPDATED_PRIORITY)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);
        return recommmendedItems;
    }

    @BeforeEach
    public void initTest() {
        recommmendedItems = createEntity(em);
    }

    @Test
    @Transactional
    void createRecommmendedItems() throws Exception {
        int databaseSizeBeforeCreate = recommmendedItemsRepository.findAll().size();
        // Create the RecommmendedItems
        restRecommmendedItemsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recommmendedItems))
            )
            .andExpect(status().isCreated());

        // Validate the RecommmendedItems in the database
        List<RecommmendedItems> recommmendedItemsList = recommmendedItemsRepository.findAll();
        assertThat(recommmendedItemsList).hasSize(databaseSizeBeforeCreate + 1);
        RecommmendedItems testRecommmendedItems = recommmendedItemsList.get(recommmendedItemsList.size() - 1);
        assertThat(testRecommmendedItems.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testRecommmendedItems.getProductId()).isEqualTo(DEFAULT_PRODUCT_ID);
        assertThat(testRecommmendedItems.getCategoryId()).isEqualTo(DEFAULT_CATEGORY_ID);
        assertThat(testRecommmendedItems.getPriority()).isEqualTo(DEFAULT_PRIORITY);
        assertThat(testRecommmendedItems.getCreatedTime()).isEqualTo(DEFAULT_CREATED_TIME);
        assertThat(testRecommmendedItems.getUpdatedTime()).isEqualTo(DEFAULT_UPDATED_TIME);
        assertThat(testRecommmendedItems.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testRecommmendedItems.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    void createRecommmendedItemsWithExistingId() throws Exception {
        // Create the RecommmendedItems with an existing ID
        recommmendedItems.setId(1L);

        int databaseSizeBeforeCreate = recommmendedItemsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRecommmendedItemsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recommmendedItems))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecommmendedItems in the database
        List<RecommmendedItems> recommmendedItemsList = recommmendedItemsRepository.findAll();
        assertThat(recommmendedItemsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRecommmendedItems() throws Exception {
        // Initialize the database
        recommmendedItemsRepository.saveAndFlush(recommmendedItems);

        // Get all the recommmendedItemsList
        restRecommmendedItemsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recommmendedItems.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.[*].categoryId").value(hasItem(DEFAULT_CATEGORY_ID.intValue())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY.intValue())))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(sameInstant(DEFAULT_CREATED_TIME))))
            .andExpect(jsonPath("$.[*].updatedTime").value(hasItem(sameInstant(DEFAULT_UPDATED_TIME))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }

    @Test
    @Transactional
    void getRecommmendedItems() throws Exception {
        // Initialize the database
        recommmendedItemsRepository.saveAndFlush(recommmendedItems);

        // Get the recommmendedItems
        restRecommmendedItemsMockMvc
            .perform(get(ENTITY_API_URL_ID, recommmendedItems.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(recommmendedItems.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.productId").value(DEFAULT_PRODUCT_ID.intValue()))
            .andExpect(jsonPath("$.categoryId").value(DEFAULT_CATEGORY_ID.intValue()))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY.intValue()))
            .andExpect(jsonPath("$.createdTime").value(sameInstant(DEFAULT_CREATED_TIME)))
            .andExpect(jsonPath("$.updatedTime").value(sameInstant(DEFAULT_UPDATED_TIME)))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingRecommmendedItems() throws Exception {
        // Get the recommmendedItems
        restRecommmendedItemsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRecommmendedItems() throws Exception {
        // Initialize the database
        recommmendedItemsRepository.saveAndFlush(recommmendedItems);

        int databaseSizeBeforeUpdate = recommmendedItemsRepository.findAll().size();

        // Update the recommmendedItems
        RecommmendedItems updatedRecommmendedItems = recommmendedItemsRepository.findById(recommmendedItems.getId()).get();
        // Disconnect from session so that the updates on updatedRecommmendedItems are not directly saved in db
        em.detach(updatedRecommmendedItems);
        updatedRecommmendedItems
            .userId(UPDATED_USER_ID)
            .productId(UPDATED_PRODUCT_ID)
            .categoryId(UPDATED_CATEGORY_ID)
            .priority(UPDATED_PRIORITY)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restRecommmendedItemsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRecommmendedItems.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRecommmendedItems))
            )
            .andExpect(status().isOk());

        // Validate the RecommmendedItems in the database
        List<RecommmendedItems> recommmendedItemsList = recommmendedItemsRepository.findAll();
        assertThat(recommmendedItemsList).hasSize(databaseSizeBeforeUpdate);
        RecommmendedItems testRecommmendedItems = recommmendedItemsList.get(recommmendedItemsList.size() - 1);
        assertThat(testRecommmendedItems.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testRecommmendedItems.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testRecommmendedItems.getCategoryId()).isEqualTo(UPDATED_CATEGORY_ID);
        assertThat(testRecommmendedItems.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testRecommmendedItems.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testRecommmendedItems.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
        assertThat(testRecommmendedItems.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testRecommmendedItems.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void putNonExistingRecommmendedItems() throws Exception {
        int databaseSizeBeforeUpdate = recommmendedItemsRepository.findAll().size();
        recommmendedItems.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecommmendedItemsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recommmendedItems.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recommmendedItems))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecommmendedItems in the database
        List<RecommmendedItems> recommmendedItemsList = recommmendedItemsRepository.findAll();
        assertThat(recommmendedItemsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRecommmendedItems() throws Exception {
        int databaseSizeBeforeUpdate = recommmendedItemsRepository.findAll().size();
        recommmendedItems.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecommmendedItemsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recommmendedItems))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecommmendedItems in the database
        List<RecommmendedItems> recommmendedItemsList = recommmendedItemsRepository.findAll();
        assertThat(recommmendedItemsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRecommmendedItems() throws Exception {
        int databaseSizeBeforeUpdate = recommmendedItemsRepository.findAll().size();
        recommmendedItems.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecommmendedItemsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recommmendedItems))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RecommmendedItems in the database
        List<RecommmendedItems> recommmendedItemsList = recommmendedItemsRepository.findAll();
        assertThat(recommmendedItemsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRecommmendedItemsWithPatch() throws Exception {
        // Initialize the database
        recommmendedItemsRepository.saveAndFlush(recommmendedItems);

        int databaseSizeBeforeUpdate = recommmendedItemsRepository.findAll().size();

        // Update the recommmendedItems using partial update
        RecommmendedItems partialUpdatedRecommmendedItems = new RecommmendedItems();
        partialUpdatedRecommmendedItems.setId(recommmendedItems.getId());

        partialUpdatedRecommmendedItems.userId(UPDATED_USER_ID).productId(UPDATED_PRODUCT_ID).createdBy(UPDATED_CREATED_BY);

        restRecommmendedItemsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecommmendedItems.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRecommmendedItems))
            )
            .andExpect(status().isOk());

        // Validate the RecommmendedItems in the database
        List<RecommmendedItems> recommmendedItemsList = recommmendedItemsRepository.findAll();
        assertThat(recommmendedItemsList).hasSize(databaseSizeBeforeUpdate);
        RecommmendedItems testRecommmendedItems = recommmendedItemsList.get(recommmendedItemsList.size() - 1);
        assertThat(testRecommmendedItems.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testRecommmendedItems.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testRecommmendedItems.getCategoryId()).isEqualTo(DEFAULT_CATEGORY_ID);
        assertThat(testRecommmendedItems.getPriority()).isEqualTo(DEFAULT_PRIORITY);
        assertThat(testRecommmendedItems.getCreatedTime()).isEqualTo(DEFAULT_CREATED_TIME);
        assertThat(testRecommmendedItems.getUpdatedTime()).isEqualTo(DEFAULT_UPDATED_TIME);
        assertThat(testRecommmendedItems.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testRecommmendedItems.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    void fullUpdateRecommmendedItemsWithPatch() throws Exception {
        // Initialize the database
        recommmendedItemsRepository.saveAndFlush(recommmendedItems);

        int databaseSizeBeforeUpdate = recommmendedItemsRepository.findAll().size();

        // Update the recommmendedItems using partial update
        RecommmendedItems partialUpdatedRecommmendedItems = new RecommmendedItems();
        partialUpdatedRecommmendedItems.setId(recommmendedItems.getId());

        partialUpdatedRecommmendedItems
            .userId(UPDATED_USER_ID)
            .productId(UPDATED_PRODUCT_ID)
            .categoryId(UPDATED_CATEGORY_ID)
            .priority(UPDATED_PRIORITY)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restRecommmendedItemsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecommmendedItems.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRecommmendedItems))
            )
            .andExpect(status().isOk());

        // Validate the RecommmendedItems in the database
        List<RecommmendedItems> recommmendedItemsList = recommmendedItemsRepository.findAll();
        assertThat(recommmendedItemsList).hasSize(databaseSizeBeforeUpdate);
        RecommmendedItems testRecommmendedItems = recommmendedItemsList.get(recommmendedItemsList.size() - 1);
        assertThat(testRecommmendedItems.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testRecommmendedItems.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testRecommmendedItems.getCategoryId()).isEqualTo(UPDATED_CATEGORY_ID);
        assertThat(testRecommmendedItems.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testRecommmendedItems.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testRecommmendedItems.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
        assertThat(testRecommmendedItems.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testRecommmendedItems.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingRecommmendedItems() throws Exception {
        int databaseSizeBeforeUpdate = recommmendedItemsRepository.findAll().size();
        recommmendedItems.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecommmendedItemsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, recommmendedItems.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(recommmendedItems))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecommmendedItems in the database
        List<RecommmendedItems> recommmendedItemsList = recommmendedItemsRepository.findAll();
        assertThat(recommmendedItemsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRecommmendedItems() throws Exception {
        int databaseSizeBeforeUpdate = recommmendedItemsRepository.findAll().size();
        recommmendedItems.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecommmendedItemsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(recommmendedItems))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecommmendedItems in the database
        List<RecommmendedItems> recommmendedItemsList = recommmendedItemsRepository.findAll();
        assertThat(recommmendedItemsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRecommmendedItems() throws Exception {
        int databaseSizeBeforeUpdate = recommmendedItemsRepository.findAll().size();
        recommmendedItems.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecommmendedItemsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(recommmendedItems))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RecommmendedItems in the database
        List<RecommmendedItems> recommmendedItemsList = recommmendedItemsRepository.findAll();
        assertThat(recommmendedItemsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRecommmendedItems() throws Exception {
        // Initialize the database
        recommmendedItemsRepository.saveAndFlush(recommmendedItems);

        int databaseSizeBeforeDelete = recommmendedItemsRepository.findAll().size();

        // Delete the recommmendedItems
        restRecommmendedItemsMockMvc
            .perform(delete(ENTITY_API_URL_ID, recommmendedItems.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RecommmendedItems> recommmendedItemsList = recommmendedItemsRepository.findAll();
        assertThat(recommmendedItemsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
