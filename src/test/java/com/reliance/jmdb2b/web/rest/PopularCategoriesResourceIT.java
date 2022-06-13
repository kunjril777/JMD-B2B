package com.reliance.jmdb2b.web.rest;

import static com.reliance.jmdb2b.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.reliance.jmdb2b.IntegrationTest;
import com.reliance.jmdb2b.domain.PopularCategories;
import com.reliance.jmdb2b.repository.PopularCategoriesRepository;
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
 * Integration tests for the {@link PopularCategoriesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PopularCategoriesResourceIT {

    private static final Long DEFAULT_CATEGORY_ID = 1L;
    private static final Long UPDATED_CATEGORY_ID = 2L;

    private static final Long DEFAULT_VIEW_COUNT = 1L;
    private static final Long UPDATED_VIEW_COUNT = 2L;

    private static final Long DEFAULT_SOLD_QUANTITY = 1L;
    private static final Long UPDATED_SOLD_QUANTITY = 2L;

    private static final ZonedDateTime DEFAULT_SOLD_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_SOLD_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_CREATED_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/popular-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PopularCategoriesRepository popularCategoriesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPopularCategoriesMockMvc;

    private PopularCategories popularCategories;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PopularCategories createEntity(EntityManager em) {
        PopularCategories popularCategories = new PopularCategories()
            .categoryId(DEFAULT_CATEGORY_ID)
            .viewCount(DEFAULT_VIEW_COUNT)
            .soldQuantity(DEFAULT_SOLD_QUANTITY)
            .soldDate(DEFAULT_SOLD_DATE)
            .createdTime(DEFAULT_CREATED_TIME)
            .updatedTime(DEFAULT_UPDATED_TIME)
            .createdBy(DEFAULT_CREATED_BY)
            .updatedBy(DEFAULT_UPDATED_BY);
        return popularCategories;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PopularCategories createUpdatedEntity(EntityManager em) {
        PopularCategories popularCategories = new PopularCategories()
            .categoryId(UPDATED_CATEGORY_ID)
            .viewCount(UPDATED_VIEW_COUNT)
            .soldQuantity(UPDATED_SOLD_QUANTITY)
            .soldDate(UPDATED_SOLD_DATE)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);
        return popularCategories;
    }

    @BeforeEach
    public void initTest() {
        popularCategories = createEntity(em);
    }

    @Test
    @Transactional
    void createPopularCategories() throws Exception {
        int databaseSizeBeforeCreate = popularCategoriesRepository.findAll().size();
        // Create the PopularCategories
        restPopularCategoriesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(popularCategories))
            )
            .andExpect(status().isCreated());

        // Validate the PopularCategories in the database
        List<PopularCategories> popularCategoriesList = popularCategoriesRepository.findAll();
        assertThat(popularCategoriesList).hasSize(databaseSizeBeforeCreate + 1);
        PopularCategories testPopularCategories = popularCategoriesList.get(popularCategoriesList.size() - 1);
        assertThat(testPopularCategories.getCategoryId()).isEqualTo(DEFAULT_CATEGORY_ID);
        assertThat(testPopularCategories.getViewCount()).isEqualTo(DEFAULT_VIEW_COUNT);
        assertThat(testPopularCategories.getSoldQuantity()).isEqualTo(DEFAULT_SOLD_QUANTITY);
        assertThat(testPopularCategories.getSoldDate()).isEqualTo(DEFAULT_SOLD_DATE);
        assertThat(testPopularCategories.getCreatedTime()).isEqualTo(DEFAULT_CREATED_TIME);
        assertThat(testPopularCategories.getUpdatedTime()).isEqualTo(DEFAULT_UPDATED_TIME);
        assertThat(testPopularCategories.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testPopularCategories.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    void createPopularCategoriesWithExistingId() throws Exception {
        // Create the PopularCategories with an existing ID
        popularCategories.setId(1L);

        int databaseSizeBeforeCreate = popularCategoriesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPopularCategoriesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(popularCategories))
            )
            .andExpect(status().isBadRequest());

        // Validate the PopularCategories in the database
        List<PopularCategories> popularCategoriesList = popularCategoriesRepository.findAll();
        assertThat(popularCategoriesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPopularCategories() throws Exception {
        // Initialize the database
        popularCategoriesRepository.saveAndFlush(popularCategories);

        // Get all the popularCategoriesList
        restPopularCategoriesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(popularCategories.getId().intValue())))
            .andExpect(jsonPath("$.[*].categoryId").value(hasItem(DEFAULT_CATEGORY_ID.intValue())))
            .andExpect(jsonPath("$.[*].viewCount").value(hasItem(DEFAULT_VIEW_COUNT.intValue())))
            .andExpect(jsonPath("$.[*].soldQuantity").value(hasItem(DEFAULT_SOLD_QUANTITY.intValue())))
            .andExpect(jsonPath("$.[*].soldDate").value(hasItem(sameInstant(DEFAULT_SOLD_DATE))))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(sameInstant(DEFAULT_CREATED_TIME))))
            .andExpect(jsonPath("$.[*].updatedTime").value(hasItem(sameInstant(DEFAULT_UPDATED_TIME))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }

    @Test
    @Transactional
    void getPopularCategories() throws Exception {
        // Initialize the database
        popularCategoriesRepository.saveAndFlush(popularCategories);

        // Get the popularCategories
        restPopularCategoriesMockMvc
            .perform(get(ENTITY_API_URL_ID, popularCategories.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(popularCategories.getId().intValue()))
            .andExpect(jsonPath("$.categoryId").value(DEFAULT_CATEGORY_ID.intValue()))
            .andExpect(jsonPath("$.viewCount").value(DEFAULT_VIEW_COUNT.intValue()))
            .andExpect(jsonPath("$.soldQuantity").value(DEFAULT_SOLD_QUANTITY.intValue()))
            .andExpect(jsonPath("$.soldDate").value(sameInstant(DEFAULT_SOLD_DATE)))
            .andExpect(jsonPath("$.createdTime").value(sameInstant(DEFAULT_CREATED_TIME)))
            .andExpect(jsonPath("$.updatedTime").value(sameInstant(DEFAULT_UPDATED_TIME)))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingPopularCategories() throws Exception {
        // Get the popularCategories
        restPopularCategoriesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPopularCategories() throws Exception {
        // Initialize the database
        popularCategoriesRepository.saveAndFlush(popularCategories);

        int databaseSizeBeforeUpdate = popularCategoriesRepository.findAll().size();

        // Update the popularCategories
        PopularCategories updatedPopularCategories = popularCategoriesRepository.findById(popularCategories.getId()).get();
        // Disconnect from session so that the updates on updatedPopularCategories are not directly saved in db
        em.detach(updatedPopularCategories);
        updatedPopularCategories
            .categoryId(UPDATED_CATEGORY_ID)
            .viewCount(UPDATED_VIEW_COUNT)
            .soldQuantity(UPDATED_SOLD_QUANTITY)
            .soldDate(UPDATED_SOLD_DATE)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restPopularCategoriesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPopularCategories.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPopularCategories))
            )
            .andExpect(status().isOk());

        // Validate the PopularCategories in the database
        List<PopularCategories> popularCategoriesList = popularCategoriesRepository.findAll();
        assertThat(popularCategoriesList).hasSize(databaseSizeBeforeUpdate);
        PopularCategories testPopularCategories = popularCategoriesList.get(popularCategoriesList.size() - 1);
        assertThat(testPopularCategories.getCategoryId()).isEqualTo(UPDATED_CATEGORY_ID);
        assertThat(testPopularCategories.getViewCount()).isEqualTo(UPDATED_VIEW_COUNT);
        assertThat(testPopularCategories.getSoldQuantity()).isEqualTo(UPDATED_SOLD_QUANTITY);
        assertThat(testPopularCategories.getSoldDate()).isEqualTo(UPDATED_SOLD_DATE);
        assertThat(testPopularCategories.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testPopularCategories.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
        assertThat(testPopularCategories.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPopularCategories.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void putNonExistingPopularCategories() throws Exception {
        int databaseSizeBeforeUpdate = popularCategoriesRepository.findAll().size();
        popularCategories.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPopularCategoriesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, popularCategories.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(popularCategories))
            )
            .andExpect(status().isBadRequest());

        // Validate the PopularCategories in the database
        List<PopularCategories> popularCategoriesList = popularCategoriesRepository.findAll();
        assertThat(popularCategoriesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPopularCategories() throws Exception {
        int databaseSizeBeforeUpdate = popularCategoriesRepository.findAll().size();
        popularCategories.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPopularCategoriesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(popularCategories))
            )
            .andExpect(status().isBadRequest());

        // Validate the PopularCategories in the database
        List<PopularCategories> popularCategoriesList = popularCategoriesRepository.findAll();
        assertThat(popularCategoriesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPopularCategories() throws Exception {
        int databaseSizeBeforeUpdate = popularCategoriesRepository.findAll().size();
        popularCategories.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPopularCategoriesMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(popularCategories))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PopularCategories in the database
        List<PopularCategories> popularCategoriesList = popularCategoriesRepository.findAll();
        assertThat(popularCategoriesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePopularCategoriesWithPatch() throws Exception {
        // Initialize the database
        popularCategoriesRepository.saveAndFlush(popularCategories);

        int databaseSizeBeforeUpdate = popularCategoriesRepository.findAll().size();

        // Update the popularCategories using partial update
        PopularCategories partialUpdatedPopularCategories = new PopularCategories();
        partialUpdatedPopularCategories.setId(popularCategories.getId());

        partialUpdatedPopularCategories
            .viewCount(UPDATED_VIEW_COUNT)
            .soldDate(UPDATED_SOLD_DATE)
            .createdTime(UPDATED_CREATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restPopularCategoriesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPopularCategories.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPopularCategories))
            )
            .andExpect(status().isOk());

        // Validate the PopularCategories in the database
        List<PopularCategories> popularCategoriesList = popularCategoriesRepository.findAll();
        assertThat(popularCategoriesList).hasSize(databaseSizeBeforeUpdate);
        PopularCategories testPopularCategories = popularCategoriesList.get(popularCategoriesList.size() - 1);
        assertThat(testPopularCategories.getCategoryId()).isEqualTo(DEFAULT_CATEGORY_ID);
        assertThat(testPopularCategories.getViewCount()).isEqualTo(UPDATED_VIEW_COUNT);
        assertThat(testPopularCategories.getSoldQuantity()).isEqualTo(DEFAULT_SOLD_QUANTITY);
        assertThat(testPopularCategories.getSoldDate()).isEqualTo(UPDATED_SOLD_DATE);
        assertThat(testPopularCategories.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testPopularCategories.getUpdatedTime()).isEqualTo(DEFAULT_UPDATED_TIME);
        assertThat(testPopularCategories.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPopularCategories.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void fullUpdatePopularCategoriesWithPatch() throws Exception {
        // Initialize the database
        popularCategoriesRepository.saveAndFlush(popularCategories);

        int databaseSizeBeforeUpdate = popularCategoriesRepository.findAll().size();

        // Update the popularCategories using partial update
        PopularCategories partialUpdatedPopularCategories = new PopularCategories();
        partialUpdatedPopularCategories.setId(popularCategories.getId());

        partialUpdatedPopularCategories
            .categoryId(UPDATED_CATEGORY_ID)
            .viewCount(UPDATED_VIEW_COUNT)
            .soldQuantity(UPDATED_SOLD_QUANTITY)
            .soldDate(UPDATED_SOLD_DATE)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restPopularCategoriesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPopularCategories.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPopularCategories))
            )
            .andExpect(status().isOk());

        // Validate the PopularCategories in the database
        List<PopularCategories> popularCategoriesList = popularCategoriesRepository.findAll();
        assertThat(popularCategoriesList).hasSize(databaseSizeBeforeUpdate);
        PopularCategories testPopularCategories = popularCategoriesList.get(popularCategoriesList.size() - 1);
        assertThat(testPopularCategories.getCategoryId()).isEqualTo(UPDATED_CATEGORY_ID);
        assertThat(testPopularCategories.getViewCount()).isEqualTo(UPDATED_VIEW_COUNT);
        assertThat(testPopularCategories.getSoldQuantity()).isEqualTo(UPDATED_SOLD_QUANTITY);
        assertThat(testPopularCategories.getSoldDate()).isEqualTo(UPDATED_SOLD_DATE);
        assertThat(testPopularCategories.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testPopularCategories.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
        assertThat(testPopularCategories.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPopularCategories.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingPopularCategories() throws Exception {
        int databaseSizeBeforeUpdate = popularCategoriesRepository.findAll().size();
        popularCategories.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPopularCategoriesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, popularCategories.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(popularCategories))
            )
            .andExpect(status().isBadRequest());

        // Validate the PopularCategories in the database
        List<PopularCategories> popularCategoriesList = popularCategoriesRepository.findAll();
        assertThat(popularCategoriesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPopularCategories() throws Exception {
        int databaseSizeBeforeUpdate = popularCategoriesRepository.findAll().size();
        popularCategories.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPopularCategoriesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(popularCategories))
            )
            .andExpect(status().isBadRequest());

        // Validate the PopularCategories in the database
        List<PopularCategories> popularCategoriesList = popularCategoriesRepository.findAll();
        assertThat(popularCategoriesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPopularCategories() throws Exception {
        int databaseSizeBeforeUpdate = popularCategoriesRepository.findAll().size();
        popularCategories.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPopularCategoriesMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(popularCategories))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PopularCategories in the database
        List<PopularCategories> popularCategoriesList = popularCategoriesRepository.findAll();
        assertThat(popularCategoriesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePopularCategories() throws Exception {
        // Initialize the database
        popularCategoriesRepository.saveAndFlush(popularCategories);

        int databaseSizeBeforeDelete = popularCategoriesRepository.findAll().size();

        // Delete the popularCategories
        restPopularCategoriesMockMvc
            .perform(delete(ENTITY_API_URL_ID, popularCategories.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PopularCategories> popularCategoriesList = popularCategoriesRepository.findAll();
        assertThat(popularCategoriesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
