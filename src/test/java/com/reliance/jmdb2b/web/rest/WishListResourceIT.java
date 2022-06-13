package com.reliance.jmdb2b.web.rest;

import static com.reliance.jmdb2b.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.reliance.jmdb2b.IntegrationTest;
import com.reliance.jmdb2b.domain.WishList;
import com.reliance.jmdb2b.repository.WishListRepository;
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
 * Integration tests for the {@link WishListResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WishListResourceIT {

    private static final String DEFAULT_WISH_LIST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_WISH_LIST_NAME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/wish-lists";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WishListRepository wishListRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWishListMockMvc;

    private WishList wishList;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WishList createEntity(EntityManager em) {
        WishList wishList = new WishList()
            .wishListName(DEFAULT_WISH_LIST_NAME)
            .createdTime(DEFAULT_CREATED_TIME)
            .updatedTime(DEFAULT_UPDATED_TIME)
            .createdBy(DEFAULT_CREATED_BY)
            .updatedBy(DEFAULT_UPDATED_BY);
        return wishList;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WishList createUpdatedEntity(EntityManager em) {
        WishList wishList = new WishList()
            .wishListName(UPDATED_WISH_LIST_NAME)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);
        return wishList;
    }

    @BeforeEach
    public void initTest() {
        wishList = createEntity(em);
    }

    @Test
    @Transactional
    void createWishList() throws Exception {
        int databaseSizeBeforeCreate = wishListRepository.findAll().size();
        // Create the WishList
        restWishListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wishList)))
            .andExpect(status().isCreated());

        // Validate the WishList in the database
        List<WishList> wishListList = wishListRepository.findAll();
        assertThat(wishListList).hasSize(databaseSizeBeforeCreate + 1);
        WishList testWishList = wishListList.get(wishListList.size() - 1);
        assertThat(testWishList.getWishListName()).isEqualTo(DEFAULT_WISH_LIST_NAME);
        assertThat(testWishList.getCreatedTime()).isEqualTo(DEFAULT_CREATED_TIME);
        assertThat(testWishList.getUpdatedTime()).isEqualTo(DEFAULT_UPDATED_TIME);
        assertThat(testWishList.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testWishList.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    void createWishListWithExistingId() throws Exception {
        // Create the WishList with an existing ID
        wishList.setId(1L);

        int databaseSizeBeforeCreate = wishListRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWishListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wishList)))
            .andExpect(status().isBadRequest());

        // Validate the WishList in the database
        List<WishList> wishListList = wishListRepository.findAll();
        assertThat(wishListList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllWishLists() throws Exception {
        // Initialize the database
        wishListRepository.saveAndFlush(wishList);

        // Get all the wishListList
        restWishListMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wishList.getId().intValue())))
            .andExpect(jsonPath("$.[*].wishListName").value(hasItem(DEFAULT_WISH_LIST_NAME)))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(sameInstant(DEFAULT_CREATED_TIME))))
            .andExpect(jsonPath("$.[*].updatedTime").value(hasItem(sameInstant(DEFAULT_UPDATED_TIME))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }

    @Test
    @Transactional
    void getWishList() throws Exception {
        // Initialize the database
        wishListRepository.saveAndFlush(wishList);

        // Get the wishList
        restWishListMockMvc
            .perform(get(ENTITY_API_URL_ID, wishList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(wishList.getId().intValue()))
            .andExpect(jsonPath("$.wishListName").value(DEFAULT_WISH_LIST_NAME))
            .andExpect(jsonPath("$.createdTime").value(sameInstant(DEFAULT_CREATED_TIME)))
            .andExpect(jsonPath("$.updatedTime").value(sameInstant(DEFAULT_UPDATED_TIME)))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingWishList() throws Exception {
        // Get the wishList
        restWishListMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWishList() throws Exception {
        // Initialize the database
        wishListRepository.saveAndFlush(wishList);

        int databaseSizeBeforeUpdate = wishListRepository.findAll().size();

        // Update the wishList
        WishList updatedWishList = wishListRepository.findById(wishList.getId()).get();
        // Disconnect from session so that the updates on updatedWishList are not directly saved in db
        em.detach(updatedWishList);
        updatedWishList
            .wishListName(UPDATED_WISH_LIST_NAME)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restWishListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWishList.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedWishList))
            )
            .andExpect(status().isOk());

        // Validate the WishList in the database
        List<WishList> wishListList = wishListRepository.findAll();
        assertThat(wishListList).hasSize(databaseSizeBeforeUpdate);
        WishList testWishList = wishListList.get(wishListList.size() - 1);
        assertThat(testWishList.getWishListName()).isEqualTo(UPDATED_WISH_LIST_NAME);
        assertThat(testWishList.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testWishList.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
        assertThat(testWishList.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testWishList.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void putNonExistingWishList() throws Exception {
        int databaseSizeBeforeUpdate = wishListRepository.findAll().size();
        wishList.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWishListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, wishList.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(wishList))
            )
            .andExpect(status().isBadRequest());

        // Validate the WishList in the database
        List<WishList> wishListList = wishListRepository.findAll();
        assertThat(wishListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWishList() throws Exception {
        int databaseSizeBeforeUpdate = wishListRepository.findAll().size();
        wishList.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWishListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(wishList))
            )
            .andExpect(status().isBadRequest());

        // Validate the WishList in the database
        List<WishList> wishListList = wishListRepository.findAll();
        assertThat(wishListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWishList() throws Exception {
        int databaseSizeBeforeUpdate = wishListRepository.findAll().size();
        wishList.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWishListMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wishList)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WishList in the database
        List<WishList> wishListList = wishListRepository.findAll();
        assertThat(wishListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWishListWithPatch() throws Exception {
        // Initialize the database
        wishListRepository.saveAndFlush(wishList);

        int databaseSizeBeforeUpdate = wishListRepository.findAll().size();

        // Update the wishList using partial update
        WishList partialUpdatedWishList = new WishList();
        partialUpdatedWishList.setId(wishList.getId());

        partialUpdatedWishList.createdTime(UPDATED_CREATED_TIME).updatedTime(UPDATED_UPDATED_TIME).createdBy(UPDATED_CREATED_BY);

        restWishListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWishList.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWishList))
            )
            .andExpect(status().isOk());

        // Validate the WishList in the database
        List<WishList> wishListList = wishListRepository.findAll();
        assertThat(wishListList).hasSize(databaseSizeBeforeUpdate);
        WishList testWishList = wishListList.get(wishListList.size() - 1);
        assertThat(testWishList.getWishListName()).isEqualTo(DEFAULT_WISH_LIST_NAME);
        assertThat(testWishList.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testWishList.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
        assertThat(testWishList.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testWishList.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    void fullUpdateWishListWithPatch() throws Exception {
        // Initialize the database
        wishListRepository.saveAndFlush(wishList);

        int databaseSizeBeforeUpdate = wishListRepository.findAll().size();

        // Update the wishList using partial update
        WishList partialUpdatedWishList = new WishList();
        partialUpdatedWishList.setId(wishList.getId());

        partialUpdatedWishList
            .wishListName(UPDATED_WISH_LIST_NAME)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restWishListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWishList.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWishList))
            )
            .andExpect(status().isOk());

        // Validate the WishList in the database
        List<WishList> wishListList = wishListRepository.findAll();
        assertThat(wishListList).hasSize(databaseSizeBeforeUpdate);
        WishList testWishList = wishListList.get(wishListList.size() - 1);
        assertThat(testWishList.getWishListName()).isEqualTo(UPDATED_WISH_LIST_NAME);
        assertThat(testWishList.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testWishList.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
        assertThat(testWishList.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testWishList.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingWishList() throws Exception {
        int databaseSizeBeforeUpdate = wishListRepository.findAll().size();
        wishList.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWishListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, wishList.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(wishList))
            )
            .andExpect(status().isBadRequest());

        // Validate the WishList in the database
        List<WishList> wishListList = wishListRepository.findAll();
        assertThat(wishListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWishList() throws Exception {
        int databaseSizeBeforeUpdate = wishListRepository.findAll().size();
        wishList.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWishListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(wishList))
            )
            .andExpect(status().isBadRequest());

        // Validate the WishList in the database
        List<WishList> wishListList = wishListRepository.findAll();
        assertThat(wishListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWishList() throws Exception {
        int databaseSizeBeforeUpdate = wishListRepository.findAll().size();
        wishList.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWishListMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(wishList)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WishList in the database
        List<WishList> wishListList = wishListRepository.findAll();
        assertThat(wishListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWishList() throws Exception {
        // Initialize the database
        wishListRepository.saveAndFlush(wishList);

        int databaseSizeBeforeDelete = wishListRepository.findAll().size();

        // Delete the wishList
        restWishListMockMvc
            .perform(delete(ENTITY_API_URL_ID, wishList.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WishList> wishListList = wishListRepository.findAll();
        assertThat(wishListList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
