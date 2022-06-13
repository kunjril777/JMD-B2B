package com.reliance.jmdb2b.web.rest;

import static com.reliance.jmdb2b.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.reliance.jmdb2b.IntegrationTest;
import com.reliance.jmdb2b.domain.UserB2B;
import com.reliance.jmdb2b.repository.UserB2BRepository;
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
 * Integration tests for the {@link UserB2BResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserB2BResourceIT {

    private static final String DEFAULT_AGENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_AGENT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/user-b-2-bs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserB2BRepository userB2BRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserB2BMockMvc;

    private UserB2B userB2B;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserB2B createEntity(EntityManager em) {
        UserB2B userB2B = new UserB2B()
            .agentId(DEFAULT_AGENT_ID)
            .email(DEFAULT_EMAIL)
            .password(DEFAULT_PASSWORD)
            .createdTime(DEFAULT_CREATED_TIME)
            .updatedTime(DEFAULT_UPDATED_TIME)
            .createdBy(DEFAULT_CREATED_BY)
            .updatedBy(DEFAULT_UPDATED_BY);
        return userB2B;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserB2B createUpdatedEntity(EntityManager em) {
        UserB2B userB2B = new UserB2B()
            .agentId(UPDATED_AGENT_ID)
            .email(UPDATED_EMAIL)
            .password(UPDATED_PASSWORD)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);
        return userB2B;
    }

    @BeforeEach
    public void initTest() {
        userB2B = createEntity(em);
    }

    @Test
    @Transactional
    void createUserB2B() throws Exception {
        int databaseSizeBeforeCreate = userB2BRepository.findAll().size();
        // Create the UserB2B
        restUserB2BMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userB2B)))
            .andExpect(status().isCreated());

        // Validate the UserB2B in the database
        List<UserB2B> userB2BList = userB2BRepository.findAll();
        assertThat(userB2BList).hasSize(databaseSizeBeforeCreate + 1);
        UserB2B testUserB2B = userB2BList.get(userB2BList.size() - 1);
        assertThat(testUserB2B.getAgentId()).isEqualTo(DEFAULT_AGENT_ID);
        assertThat(testUserB2B.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testUserB2B.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testUserB2B.getCreatedTime()).isEqualTo(DEFAULT_CREATED_TIME);
        assertThat(testUserB2B.getUpdatedTime()).isEqualTo(DEFAULT_UPDATED_TIME);
        assertThat(testUserB2B.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testUserB2B.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    void createUserB2BWithExistingId() throws Exception {
        // Create the UserB2B with an existing ID
        userB2B.setId(1L);

        int databaseSizeBeforeCreate = userB2BRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserB2BMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userB2B)))
            .andExpect(status().isBadRequest());

        // Validate the UserB2B in the database
        List<UserB2B> userB2BList = userB2BRepository.findAll();
        assertThat(userB2BList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAgentIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = userB2BRepository.findAll().size();
        // set the field null
        userB2B.setAgentId(null);

        // Create the UserB2B, which fails.

        restUserB2BMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userB2B)))
            .andExpect(status().isBadRequest());

        List<UserB2B> userB2BList = userB2BRepository.findAll();
        assertThat(userB2BList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = userB2BRepository.findAll().size();
        // set the field null
        userB2B.setEmail(null);

        // Create the UserB2B, which fails.

        restUserB2BMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userB2B)))
            .andExpect(status().isBadRequest());

        List<UserB2B> userB2BList = userB2BRepository.findAll();
        assertThat(userB2BList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPasswordIsRequired() throws Exception {
        int databaseSizeBeforeTest = userB2BRepository.findAll().size();
        // set the field null
        userB2B.setPassword(null);

        // Create the UserB2B, which fails.

        restUserB2BMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userB2B)))
            .andExpect(status().isBadRequest());

        List<UserB2B> userB2BList = userB2BRepository.findAll();
        assertThat(userB2BList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = userB2BRepository.findAll().size();
        // set the field null
        userB2B.setCreatedTime(null);

        // Create the UserB2B, which fails.

        restUserB2BMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userB2B)))
            .andExpect(status().isBadRequest());

        List<UserB2B> userB2BList = userB2BRepository.findAll();
        assertThat(userB2BList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = userB2BRepository.findAll().size();
        // set the field null
        userB2B.setUpdatedTime(null);

        // Create the UserB2B, which fails.

        restUserB2BMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userB2B)))
            .andExpect(status().isBadRequest());

        List<UserB2B> userB2BList = userB2BRepository.findAll();
        assertThat(userB2BList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = userB2BRepository.findAll().size();
        // set the field null
        userB2B.setCreatedBy(null);

        // Create the UserB2B, which fails.

        restUserB2BMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userB2B)))
            .andExpect(status().isBadRequest());

        List<UserB2B> userB2BList = userB2BRepository.findAll();
        assertThat(userB2BList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = userB2BRepository.findAll().size();
        // set the field null
        userB2B.setUpdatedBy(null);

        // Create the UserB2B, which fails.

        restUserB2BMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userB2B)))
            .andExpect(status().isBadRequest());

        List<UserB2B> userB2BList = userB2BRepository.findAll();
        assertThat(userB2BList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserB2BS() throws Exception {
        // Initialize the database
        userB2BRepository.saveAndFlush(userB2B);

        // Get all the userB2BList
        restUserB2BMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userB2B.getId().intValue())))
            .andExpect(jsonPath("$.[*].agentId").value(hasItem(DEFAULT_AGENT_ID)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(sameInstant(DEFAULT_CREATED_TIME))))
            .andExpect(jsonPath("$.[*].updatedTime").value(hasItem(sameInstant(DEFAULT_UPDATED_TIME))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }

    @Test
    @Transactional
    void getUserB2B() throws Exception {
        // Initialize the database
        userB2BRepository.saveAndFlush(userB2B);

        // Get the userB2B
        restUserB2BMockMvc
            .perform(get(ENTITY_API_URL_ID, userB2B.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userB2B.getId().intValue()))
            .andExpect(jsonPath("$.agentId").value(DEFAULT_AGENT_ID))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD))
            .andExpect(jsonPath("$.createdTime").value(sameInstant(DEFAULT_CREATED_TIME)))
            .andExpect(jsonPath("$.updatedTime").value(sameInstant(DEFAULT_UPDATED_TIME)))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingUserB2B() throws Exception {
        // Get the userB2B
        restUserB2BMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUserB2B() throws Exception {
        // Initialize the database
        userB2BRepository.saveAndFlush(userB2B);

        int databaseSizeBeforeUpdate = userB2BRepository.findAll().size();

        // Update the userB2B
        UserB2B updatedUserB2B = userB2BRepository.findById(userB2B.getId()).get();
        // Disconnect from session so that the updates on updatedUserB2B are not directly saved in db
        em.detach(updatedUserB2B);
        updatedUserB2B
            .agentId(UPDATED_AGENT_ID)
            .email(UPDATED_EMAIL)
            .password(UPDATED_PASSWORD)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restUserB2BMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUserB2B.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUserB2B))
            )
            .andExpect(status().isOk());

        // Validate the UserB2B in the database
        List<UserB2B> userB2BList = userB2BRepository.findAll();
        assertThat(userB2BList).hasSize(databaseSizeBeforeUpdate);
        UserB2B testUserB2B = userB2BList.get(userB2BList.size() - 1);
        assertThat(testUserB2B.getAgentId()).isEqualTo(UPDATED_AGENT_ID);
        assertThat(testUserB2B.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUserB2B.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testUserB2B.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testUserB2B.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
        assertThat(testUserB2B.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUserB2B.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void putNonExistingUserB2B() throws Exception {
        int databaseSizeBeforeUpdate = userB2BRepository.findAll().size();
        userB2B.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserB2BMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userB2B.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userB2B))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserB2B in the database
        List<UserB2B> userB2BList = userB2BRepository.findAll();
        assertThat(userB2BList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserB2B() throws Exception {
        int databaseSizeBeforeUpdate = userB2BRepository.findAll().size();
        userB2B.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserB2BMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userB2B))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserB2B in the database
        List<UserB2B> userB2BList = userB2BRepository.findAll();
        assertThat(userB2BList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserB2B() throws Exception {
        int databaseSizeBeforeUpdate = userB2BRepository.findAll().size();
        userB2B.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserB2BMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userB2B)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserB2B in the database
        List<UserB2B> userB2BList = userB2BRepository.findAll();
        assertThat(userB2BList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserB2BWithPatch() throws Exception {
        // Initialize the database
        userB2BRepository.saveAndFlush(userB2B);

        int databaseSizeBeforeUpdate = userB2BRepository.findAll().size();

        // Update the userB2B using partial update
        UserB2B partialUpdatedUserB2B = new UserB2B();
        partialUpdatedUserB2B.setId(userB2B.getId());

        partialUpdatedUserB2B
            .email(UPDATED_EMAIL)
            .password(UPDATED_PASSWORD)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .updatedBy(UPDATED_UPDATED_BY);

        restUserB2BMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserB2B.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserB2B))
            )
            .andExpect(status().isOk());

        // Validate the UserB2B in the database
        List<UserB2B> userB2BList = userB2BRepository.findAll();
        assertThat(userB2BList).hasSize(databaseSizeBeforeUpdate);
        UserB2B testUserB2B = userB2BList.get(userB2BList.size() - 1);
        assertThat(testUserB2B.getAgentId()).isEqualTo(DEFAULT_AGENT_ID);
        assertThat(testUserB2B.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUserB2B.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testUserB2B.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testUserB2B.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
        assertThat(testUserB2B.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testUserB2B.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void fullUpdateUserB2BWithPatch() throws Exception {
        // Initialize the database
        userB2BRepository.saveAndFlush(userB2B);

        int databaseSizeBeforeUpdate = userB2BRepository.findAll().size();

        // Update the userB2B using partial update
        UserB2B partialUpdatedUserB2B = new UserB2B();
        partialUpdatedUserB2B.setId(userB2B.getId());

        partialUpdatedUserB2B
            .agentId(UPDATED_AGENT_ID)
            .email(UPDATED_EMAIL)
            .password(UPDATED_PASSWORD)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restUserB2BMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserB2B.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserB2B))
            )
            .andExpect(status().isOk());

        // Validate the UserB2B in the database
        List<UserB2B> userB2BList = userB2BRepository.findAll();
        assertThat(userB2BList).hasSize(databaseSizeBeforeUpdate);
        UserB2B testUserB2B = userB2BList.get(userB2BList.size() - 1);
        assertThat(testUserB2B.getAgentId()).isEqualTo(UPDATED_AGENT_ID);
        assertThat(testUserB2B.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUserB2B.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testUserB2B.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testUserB2B.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
        assertThat(testUserB2B.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUserB2B.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingUserB2B() throws Exception {
        int databaseSizeBeforeUpdate = userB2BRepository.findAll().size();
        userB2B.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserB2BMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userB2B.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userB2B))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserB2B in the database
        List<UserB2B> userB2BList = userB2BRepository.findAll();
        assertThat(userB2BList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserB2B() throws Exception {
        int databaseSizeBeforeUpdate = userB2BRepository.findAll().size();
        userB2B.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserB2BMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userB2B))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserB2B in the database
        List<UserB2B> userB2BList = userB2BRepository.findAll();
        assertThat(userB2BList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserB2B() throws Exception {
        int databaseSizeBeforeUpdate = userB2BRepository.findAll().size();
        userB2B.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserB2BMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(userB2B)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserB2B in the database
        List<UserB2B> userB2BList = userB2BRepository.findAll();
        assertThat(userB2BList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserB2B() throws Exception {
        // Initialize the database
        userB2BRepository.saveAndFlush(userB2B);

        int databaseSizeBeforeDelete = userB2BRepository.findAll().size();

        // Delete the userB2B
        restUserB2BMockMvc
            .perform(delete(ENTITY_API_URL_ID, userB2B.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserB2B> userB2BList = userB2BRepository.findAll();
        assertThat(userB2BList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
