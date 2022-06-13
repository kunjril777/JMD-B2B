package com.reliance.jmdb2b.web.rest;

import static com.reliance.jmdb2b.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.reliance.jmdb2b.IntegrationTest;
import com.reliance.jmdb2b.domain.TrendingProducts;
import com.reliance.jmdb2b.repository.TrendingProductsRepository;
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
 * Integration tests for the {@link TrendingProductsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TrendingProductsResourceIT {

    private static final Long DEFAULT_PRODUCT_ID = 1L;
    private static final Long UPDATED_PRODUCT_ID = 2L;

    private static final Long DEFAULT_CATEGORY_ID = 1L;
    private static final Long UPDATED_CATEGORY_ID = 2L;

    private static final Long DEFAULT_SOLD_QUANTITY = 1L;
    private static final Long UPDATED_SOLD_QUANTITY = 2L;

    private static final Long DEFAULT_VIEW_COUNT = 1L;
    private static final Long UPDATED_VIEW_COUNT = 2L;

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

    private static final String ENTITY_API_URL = "/api/trending-products";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TrendingProductsRepository trendingProductsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTrendingProductsMockMvc;

    private TrendingProducts trendingProducts;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrendingProducts createEntity(EntityManager em) {
        TrendingProducts trendingProducts = new TrendingProducts()
            .productId(DEFAULT_PRODUCT_ID)
            .categoryId(DEFAULT_CATEGORY_ID)
            .soldQuantity(DEFAULT_SOLD_QUANTITY)
            .viewCount(DEFAULT_VIEW_COUNT)
            .soldDate(DEFAULT_SOLD_DATE)
            .createdTime(DEFAULT_CREATED_TIME)
            .updatedTime(DEFAULT_UPDATED_TIME)
            .createdBy(DEFAULT_CREATED_BY)
            .updatedBy(DEFAULT_UPDATED_BY);
        return trendingProducts;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrendingProducts createUpdatedEntity(EntityManager em) {
        TrendingProducts trendingProducts = new TrendingProducts()
            .productId(UPDATED_PRODUCT_ID)
            .categoryId(UPDATED_CATEGORY_ID)
            .soldQuantity(UPDATED_SOLD_QUANTITY)
            .viewCount(UPDATED_VIEW_COUNT)
            .soldDate(UPDATED_SOLD_DATE)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);
        return trendingProducts;
    }

    @BeforeEach
    public void initTest() {
        trendingProducts = createEntity(em);
    }

    @Test
    @Transactional
    void createTrendingProducts() throws Exception {
        int databaseSizeBeforeCreate = trendingProductsRepository.findAll().size();
        // Create the TrendingProducts
        restTrendingProductsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trendingProducts))
            )
            .andExpect(status().isCreated());

        // Validate the TrendingProducts in the database
        List<TrendingProducts> trendingProductsList = trendingProductsRepository.findAll();
        assertThat(trendingProductsList).hasSize(databaseSizeBeforeCreate + 1);
        TrendingProducts testTrendingProducts = trendingProductsList.get(trendingProductsList.size() - 1);
        assertThat(testTrendingProducts.getProductId()).isEqualTo(DEFAULT_PRODUCT_ID);
        assertThat(testTrendingProducts.getCategoryId()).isEqualTo(DEFAULT_CATEGORY_ID);
        assertThat(testTrendingProducts.getSoldQuantity()).isEqualTo(DEFAULT_SOLD_QUANTITY);
        assertThat(testTrendingProducts.getViewCount()).isEqualTo(DEFAULT_VIEW_COUNT);
        assertThat(testTrendingProducts.getSoldDate()).isEqualTo(DEFAULT_SOLD_DATE);
        assertThat(testTrendingProducts.getCreatedTime()).isEqualTo(DEFAULT_CREATED_TIME);
        assertThat(testTrendingProducts.getUpdatedTime()).isEqualTo(DEFAULT_UPDATED_TIME);
        assertThat(testTrendingProducts.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testTrendingProducts.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    void createTrendingProductsWithExistingId() throws Exception {
        // Create the TrendingProducts with an existing ID
        trendingProducts.setId(1L);

        int databaseSizeBeforeCreate = trendingProductsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrendingProductsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trendingProducts))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrendingProducts in the database
        List<TrendingProducts> trendingProductsList = trendingProductsRepository.findAll();
        assertThat(trendingProductsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTrendingProducts() throws Exception {
        // Initialize the database
        trendingProductsRepository.saveAndFlush(trendingProducts);

        // Get all the trendingProductsList
        restTrendingProductsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trendingProducts.getId().intValue())))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.[*].categoryId").value(hasItem(DEFAULT_CATEGORY_ID.intValue())))
            .andExpect(jsonPath("$.[*].soldQuantity").value(hasItem(DEFAULT_SOLD_QUANTITY.intValue())))
            .andExpect(jsonPath("$.[*].viewCount").value(hasItem(DEFAULT_VIEW_COUNT.intValue())))
            .andExpect(jsonPath("$.[*].soldDate").value(hasItem(sameInstant(DEFAULT_SOLD_DATE))))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(sameInstant(DEFAULT_CREATED_TIME))))
            .andExpect(jsonPath("$.[*].updatedTime").value(hasItem(sameInstant(DEFAULT_UPDATED_TIME))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }

    @Test
    @Transactional
    void getTrendingProducts() throws Exception {
        // Initialize the database
        trendingProductsRepository.saveAndFlush(trendingProducts);

        // Get the trendingProducts
        restTrendingProductsMockMvc
            .perform(get(ENTITY_API_URL_ID, trendingProducts.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(trendingProducts.getId().intValue()))
            .andExpect(jsonPath("$.productId").value(DEFAULT_PRODUCT_ID.intValue()))
            .andExpect(jsonPath("$.categoryId").value(DEFAULT_CATEGORY_ID.intValue()))
            .andExpect(jsonPath("$.soldQuantity").value(DEFAULT_SOLD_QUANTITY.intValue()))
            .andExpect(jsonPath("$.viewCount").value(DEFAULT_VIEW_COUNT.intValue()))
            .andExpect(jsonPath("$.soldDate").value(sameInstant(DEFAULT_SOLD_DATE)))
            .andExpect(jsonPath("$.createdTime").value(sameInstant(DEFAULT_CREATED_TIME)))
            .andExpect(jsonPath("$.updatedTime").value(sameInstant(DEFAULT_UPDATED_TIME)))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingTrendingProducts() throws Exception {
        // Get the trendingProducts
        restTrendingProductsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTrendingProducts() throws Exception {
        // Initialize the database
        trendingProductsRepository.saveAndFlush(trendingProducts);

        int databaseSizeBeforeUpdate = trendingProductsRepository.findAll().size();

        // Update the trendingProducts
        TrendingProducts updatedTrendingProducts = trendingProductsRepository.findById(trendingProducts.getId()).get();
        // Disconnect from session so that the updates on updatedTrendingProducts are not directly saved in db
        em.detach(updatedTrendingProducts);
        updatedTrendingProducts
            .productId(UPDATED_PRODUCT_ID)
            .categoryId(UPDATED_CATEGORY_ID)
            .soldQuantity(UPDATED_SOLD_QUANTITY)
            .viewCount(UPDATED_VIEW_COUNT)
            .soldDate(UPDATED_SOLD_DATE)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restTrendingProductsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTrendingProducts.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTrendingProducts))
            )
            .andExpect(status().isOk());

        // Validate the TrendingProducts in the database
        List<TrendingProducts> trendingProductsList = trendingProductsRepository.findAll();
        assertThat(trendingProductsList).hasSize(databaseSizeBeforeUpdate);
        TrendingProducts testTrendingProducts = trendingProductsList.get(trendingProductsList.size() - 1);
        assertThat(testTrendingProducts.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testTrendingProducts.getCategoryId()).isEqualTo(UPDATED_CATEGORY_ID);
        assertThat(testTrendingProducts.getSoldQuantity()).isEqualTo(UPDATED_SOLD_QUANTITY);
        assertThat(testTrendingProducts.getViewCount()).isEqualTo(UPDATED_VIEW_COUNT);
        assertThat(testTrendingProducts.getSoldDate()).isEqualTo(UPDATED_SOLD_DATE);
        assertThat(testTrendingProducts.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testTrendingProducts.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
        assertThat(testTrendingProducts.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testTrendingProducts.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void putNonExistingTrendingProducts() throws Exception {
        int databaseSizeBeforeUpdate = trendingProductsRepository.findAll().size();
        trendingProducts.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrendingProductsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trendingProducts.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trendingProducts))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrendingProducts in the database
        List<TrendingProducts> trendingProductsList = trendingProductsRepository.findAll();
        assertThat(trendingProductsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTrendingProducts() throws Exception {
        int databaseSizeBeforeUpdate = trendingProductsRepository.findAll().size();
        trendingProducts.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrendingProductsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trendingProducts))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrendingProducts in the database
        List<TrendingProducts> trendingProductsList = trendingProductsRepository.findAll();
        assertThat(trendingProductsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTrendingProducts() throws Exception {
        int databaseSizeBeforeUpdate = trendingProductsRepository.findAll().size();
        trendingProducts.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrendingProductsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trendingProducts))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TrendingProducts in the database
        List<TrendingProducts> trendingProductsList = trendingProductsRepository.findAll();
        assertThat(trendingProductsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTrendingProductsWithPatch() throws Exception {
        // Initialize the database
        trendingProductsRepository.saveAndFlush(trendingProducts);

        int databaseSizeBeforeUpdate = trendingProductsRepository.findAll().size();

        // Update the trendingProducts using partial update
        TrendingProducts partialUpdatedTrendingProducts = new TrendingProducts();
        partialUpdatedTrendingProducts.setId(trendingProducts.getId());

        partialUpdatedTrendingProducts
            .categoryId(UPDATED_CATEGORY_ID)
            .soldQuantity(UPDATED_SOLD_QUANTITY)
            .createdTime(UPDATED_CREATED_TIME)
            .createdBy(UPDATED_CREATED_BY);

        restTrendingProductsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrendingProducts.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTrendingProducts))
            )
            .andExpect(status().isOk());

        // Validate the TrendingProducts in the database
        List<TrendingProducts> trendingProductsList = trendingProductsRepository.findAll();
        assertThat(trendingProductsList).hasSize(databaseSizeBeforeUpdate);
        TrendingProducts testTrendingProducts = trendingProductsList.get(trendingProductsList.size() - 1);
        assertThat(testTrendingProducts.getProductId()).isEqualTo(DEFAULT_PRODUCT_ID);
        assertThat(testTrendingProducts.getCategoryId()).isEqualTo(UPDATED_CATEGORY_ID);
        assertThat(testTrendingProducts.getSoldQuantity()).isEqualTo(UPDATED_SOLD_QUANTITY);
        assertThat(testTrendingProducts.getViewCount()).isEqualTo(DEFAULT_VIEW_COUNT);
        assertThat(testTrendingProducts.getSoldDate()).isEqualTo(DEFAULT_SOLD_DATE);
        assertThat(testTrendingProducts.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testTrendingProducts.getUpdatedTime()).isEqualTo(DEFAULT_UPDATED_TIME);
        assertThat(testTrendingProducts.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testTrendingProducts.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    void fullUpdateTrendingProductsWithPatch() throws Exception {
        // Initialize the database
        trendingProductsRepository.saveAndFlush(trendingProducts);

        int databaseSizeBeforeUpdate = trendingProductsRepository.findAll().size();

        // Update the trendingProducts using partial update
        TrendingProducts partialUpdatedTrendingProducts = new TrendingProducts();
        partialUpdatedTrendingProducts.setId(trendingProducts.getId());

        partialUpdatedTrendingProducts
            .productId(UPDATED_PRODUCT_ID)
            .categoryId(UPDATED_CATEGORY_ID)
            .soldQuantity(UPDATED_SOLD_QUANTITY)
            .viewCount(UPDATED_VIEW_COUNT)
            .soldDate(UPDATED_SOLD_DATE)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restTrendingProductsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrendingProducts.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTrendingProducts))
            )
            .andExpect(status().isOk());

        // Validate the TrendingProducts in the database
        List<TrendingProducts> trendingProductsList = trendingProductsRepository.findAll();
        assertThat(trendingProductsList).hasSize(databaseSizeBeforeUpdate);
        TrendingProducts testTrendingProducts = trendingProductsList.get(trendingProductsList.size() - 1);
        assertThat(testTrendingProducts.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testTrendingProducts.getCategoryId()).isEqualTo(UPDATED_CATEGORY_ID);
        assertThat(testTrendingProducts.getSoldQuantity()).isEqualTo(UPDATED_SOLD_QUANTITY);
        assertThat(testTrendingProducts.getViewCount()).isEqualTo(UPDATED_VIEW_COUNT);
        assertThat(testTrendingProducts.getSoldDate()).isEqualTo(UPDATED_SOLD_DATE);
        assertThat(testTrendingProducts.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testTrendingProducts.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
        assertThat(testTrendingProducts.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testTrendingProducts.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingTrendingProducts() throws Exception {
        int databaseSizeBeforeUpdate = trendingProductsRepository.findAll().size();
        trendingProducts.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrendingProductsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, trendingProducts.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trendingProducts))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrendingProducts in the database
        List<TrendingProducts> trendingProductsList = trendingProductsRepository.findAll();
        assertThat(trendingProductsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTrendingProducts() throws Exception {
        int databaseSizeBeforeUpdate = trendingProductsRepository.findAll().size();
        trendingProducts.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrendingProductsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trendingProducts))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrendingProducts in the database
        List<TrendingProducts> trendingProductsList = trendingProductsRepository.findAll();
        assertThat(trendingProductsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTrendingProducts() throws Exception {
        int databaseSizeBeforeUpdate = trendingProductsRepository.findAll().size();
        trendingProducts.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrendingProductsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trendingProducts))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TrendingProducts in the database
        List<TrendingProducts> trendingProductsList = trendingProductsRepository.findAll();
        assertThat(trendingProductsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTrendingProducts() throws Exception {
        // Initialize the database
        trendingProductsRepository.saveAndFlush(trendingProducts);

        int databaseSizeBeforeDelete = trendingProductsRepository.findAll().size();

        // Delete the trendingProducts
        restTrendingProductsMockMvc
            .perform(delete(ENTITY_API_URL_ID, trendingProducts.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TrendingProducts> trendingProductsList = trendingProductsRepository.findAll();
        assertThat(trendingProductsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
