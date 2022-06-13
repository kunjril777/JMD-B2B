package com.reliance.jmdb2b.web.rest;

import static com.reliance.jmdb2b.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.reliance.jmdb2b.IntegrationTest;
import com.reliance.jmdb2b.domain.RecentSearches;
import com.reliance.jmdb2b.repository.RecentSearchesRepository;
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
 * Integration tests for the {@link RecentSearchesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RecentSearchesResourceIT {

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final String DEFAULT_SEARCH_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_SEARCH_TEXT = "BBBBBBBBBB";

    private static final Long DEFAULT_SEARCH_COUNT = 1L;
    private static final Long UPDATED_SEARCH_COUNT = 2L;

    private static final ZonedDateTime DEFAULT_CREATED_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/recent-searches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RecentSearchesRepository recentSearchesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRecentSearchesMockMvc;

    private RecentSearches recentSearches;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RecentSearches createEntity(EntityManager em) {
        RecentSearches recentSearches = new RecentSearches()
            .userId(DEFAULT_USER_ID)
            .searchText(DEFAULT_SEARCH_TEXT)
            .searchCount(DEFAULT_SEARCH_COUNT)
            .createdTime(DEFAULT_CREATED_TIME)
            .updatedTime(DEFAULT_UPDATED_TIME)
            .createdBy(DEFAULT_CREATED_BY)
            .updatedBy(DEFAULT_UPDATED_BY);
        return recentSearches;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RecentSearches createUpdatedEntity(EntityManager em) {
        RecentSearches recentSearches = new RecentSearches()
            .userId(UPDATED_USER_ID)
            .searchText(UPDATED_SEARCH_TEXT)
            .searchCount(UPDATED_SEARCH_COUNT)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);
        return recentSearches;
    }

    @BeforeEach
    public void initTest() {
        recentSearches = createEntity(em);
    }

    @Test
    @Transactional
    void createRecentSearches() throws Exception {
        int databaseSizeBeforeCreate = recentSearchesRepository.findAll().size();
        // Create the RecentSearches
        restRecentSearchesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recentSearches))
            )
            .andExpect(status().isCreated());

        // Validate the RecentSearches in the database
        List<RecentSearches> recentSearchesList = recentSearchesRepository.findAll();
        assertThat(recentSearchesList).hasSize(databaseSizeBeforeCreate + 1);
        RecentSearches testRecentSearches = recentSearchesList.get(recentSearchesList.size() - 1);
        assertThat(testRecentSearches.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testRecentSearches.getSearchText()).isEqualTo(DEFAULT_SEARCH_TEXT);
        assertThat(testRecentSearches.getSearchCount()).isEqualTo(DEFAULT_SEARCH_COUNT);
        assertThat(testRecentSearches.getCreatedTime()).isEqualTo(DEFAULT_CREATED_TIME);
        assertThat(testRecentSearches.getUpdatedTime()).isEqualTo(DEFAULT_UPDATED_TIME);
        assertThat(testRecentSearches.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testRecentSearches.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    void createRecentSearchesWithExistingId() throws Exception {
        // Create the RecentSearches with an existing ID
        recentSearches.setId(1L);

        int databaseSizeBeforeCreate = recentSearchesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRecentSearchesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recentSearches))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecentSearches in the database
        List<RecentSearches> recentSearchesList = recentSearchesRepository.findAll();
        assertThat(recentSearchesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRecentSearches() throws Exception {
        // Initialize the database
        recentSearchesRepository.saveAndFlush(recentSearches);

        // Get all the recentSearchesList
        restRecentSearchesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recentSearches.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].searchText").value(hasItem(DEFAULT_SEARCH_TEXT)))
            .andExpect(jsonPath("$.[*].searchCount").value(hasItem(DEFAULT_SEARCH_COUNT.intValue())))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(sameInstant(DEFAULT_CREATED_TIME))))
            .andExpect(jsonPath("$.[*].updatedTime").value(hasItem(sameInstant(DEFAULT_UPDATED_TIME))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }

    @Test
    @Transactional
    void getRecentSearches() throws Exception {
        // Initialize the database
        recentSearchesRepository.saveAndFlush(recentSearches);

        // Get the recentSearches
        restRecentSearchesMockMvc
            .perform(get(ENTITY_API_URL_ID, recentSearches.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(recentSearches.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.searchText").value(DEFAULT_SEARCH_TEXT))
            .andExpect(jsonPath("$.searchCount").value(DEFAULT_SEARCH_COUNT.intValue()))
            .andExpect(jsonPath("$.createdTime").value(sameInstant(DEFAULT_CREATED_TIME)))
            .andExpect(jsonPath("$.updatedTime").value(sameInstant(DEFAULT_UPDATED_TIME)))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingRecentSearches() throws Exception {
        // Get the recentSearches
        restRecentSearchesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRecentSearches() throws Exception {
        // Initialize the database
        recentSearchesRepository.saveAndFlush(recentSearches);

        int databaseSizeBeforeUpdate = recentSearchesRepository.findAll().size();

        // Update the recentSearches
        RecentSearches updatedRecentSearches = recentSearchesRepository.findById(recentSearches.getId()).get();
        // Disconnect from session so that the updates on updatedRecentSearches are not directly saved in db
        em.detach(updatedRecentSearches);
        updatedRecentSearches
            .userId(UPDATED_USER_ID)
            .searchText(UPDATED_SEARCH_TEXT)
            .searchCount(UPDATED_SEARCH_COUNT)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restRecentSearchesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRecentSearches.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRecentSearches))
            )
            .andExpect(status().isOk());

        // Validate the RecentSearches in the database
        List<RecentSearches> recentSearchesList = recentSearchesRepository.findAll();
        assertThat(recentSearchesList).hasSize(databaseSizeBeforeUpdate);
        RecentSearches testRecentSearches = recentSearchesList.get(recentSearchesList.size() - 1);
        assertThat(testRecentSearches.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testRecentSearches.getSearchText()).isEqualTo(UPDATED_SEARCH_TEXT);
        assertThat(testRecentSearches.getSearchCount()).isEqualTo(UPDATED_SEARCH_COUNT);
        assertThat(testRecentSearches.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testRecentSearches.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
        assertThat(testRecentSearches.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testRecentSearches.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void putNonExistingRecentSearches() throws Exception {
        int databaseSizeBeforeUpdate = recentSearchesRepository.findAll().size();
        recentSearches.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecentSearchesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recentSearches.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recentSearches))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecentSearches in the database
        List<RecentSearches> recentSearchesList = recentSearchesRepository.findAll();
        assertThat(recentSearchesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRecentSearches() throws Exception {
        int databaseSizeBeforeUpdate = recentSearchesRepository.findAll().size();
        recentSearches.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecentSearchesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recentSearches))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecentSearches in the database
        List<RecentSearches> recentSearchesList = recentSearchesRepository.findAll();
        assertThat(recentSearchesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRecentSearches() throws Exception {
        int databaseSizeBeforeUpdate = recentSearchesRepository.findAll().size();
        recentSearches.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecentSearchesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recentSearches)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RecentSearches in the database
        List<RecentSearches> recentSearchesList = recentSearchesRepository.findAll();
        assertThat(recentSearchesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRecentSearchesWithPatch() throws Exception {
        // Initialize the database
        recentSearchesRepository.saveAndFlush(recentSearches);

        int databaseSizeBeforeUpdate = recentSearchesRepository.findAll().size();

        // Update the recentSearches using partial update
        RecentSearches partialUpdatedRecentSearches = new RecentSearches();
        partialUpdatedRecentSearches.setId(recentSearches.getId());

        partialUpdatedRecentSearches
            .searchCount(UPDATED_SEARCH_COUNT)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY);

        restRecentSearchesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecentSearches.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRecentSearches))
            )
            .andExpect(status().isOk());

        // Validate the RecentSearches in the database
        List<RecentSearches> recentSearchesList = recentSearchesRepository.findAll();
        assertThat(recentSearchesList).hasSize(databaseSizeBeforeUpdate);
        RecentSearches testRecentSearches = recentSearchesList.get(recentSearchesList.size() - 1);
        assertThat(testRecentSearches.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testRecentSearches.getSearchText()).isEqualTo(DEFAULT_SEARCH_TEXT);
        assertThat(testRecentSearches.getSearchCount()).isEqualTo(UPDATED_SEARCH_COUNT);
        assertThat(testRecentSearches.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testRecentSearches.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
        assertThat(testRecentSearches.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testRecentSearches.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    void fullUpdateRecentSearchesWithPatch() throws Exception {
        // Initialize the database
        recentSearchesRepository.saveAndFlush(recentSearches);

        int databaseSizeBeforeUpdate = recentSearchesRepository.findAll().size();

        // Update the recentSearches using partial update
        RecentSearches partialUpdatedRecentSearches = new RecentSearches();
        partialUpdatedRecentSearches.setId(recentSearches.getId());

        partialUpdatedRecentSearches
            .userId(UPDATED_USER_ID)
            .searchText(UPDATED_SEARCH_TEXT)
            .searchCount(UPDATED_SEARCH_COUNT)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restRecentSearchesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecentSearches.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRecentSearches))
            )
            .andExpect(status().isOk());

        // Validate the RecentSearches in the database
        List<RecentSearches> recentSearchesList = recentSearchesRepository.findAll();
        assertThat(recentSearchesList).hasSize(databaseSizeBeforeUpdate);
        RecentSearches testRecentSearches = recentSearchesList.get(recentSearchesList.size() - 1);
        assertThat(testRecentSearches.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testRecentSearches.getSearchText()).isEqualTo(UPDATED_SEARCH_TEXT);
        assertThat(testRecentSearches.getSearchCount()).isEqualTo(UPDATED_SEARCH_COUNT);
        assertThat(testRecentSearches.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testRecentSearches.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
        assertThat(testRecentSearches.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testRecentSearches.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingRecentSearches() throws Exception {
        int databaseSizeBeforeUpdate = recentSearchesRepository.findAll().size();
        recentSearches.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecentSearchesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, recentSearches.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(recentSearches))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecentSearches in the database
        List<RecentSearches> recentSearchesList = recentSearchesRepository.findAll();
        assertThat(recentSearchesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRecentSearches() throws Exception {
        int databaseSizeBeforeUpdate = recentSearchesRepository.findAll().size();
        recentSearches.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecentSearchesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(recentSearches))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecentSearches in the database
        List<RecentSearches> recentSearchesList = recentSearchesRepository.findAll();
        assertThat(recentSearchesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRecentSearches() throws Exception {
        int databaseSizeBeforeUpdate = recentSearchesRepository.findAll().size();
        recentSearches.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecentSearchesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(recentSearches))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RecentSearches in the database
        List<RecentSearches> recentSearchesList = recentSearchesRepository.findAll();
        assertThat(recentSearchesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRecentSearches() throws Exception {
        // Initialize the database
        recentSearchesRepository.saveAndFlush(recentSearches);

        int databaseSizeBeforeDelete = recentSearchesRepository.findAll().size();

        // Delete the recentSearches
        restRecentSearchesMockMvc
            .perform(delete(ENTITY_API_URL_ID, recentSearches.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RecentSearches> recentSearchesList = recentSearchesRepository.findAll();
        assertThat(recentSearchesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
