package com.reliance.jmdb2b.web.rest;

import static com.reliance.jmdb2b.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.reliance.jmdb2b.IntegrationTest;
import com.reliance.jmdb2b.domain.ProductReview;
import com.reliance.jmdb2b.repository.ProductReviewRepository;
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
 * Integration tests for the {@link ProductReviewResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductReviewResourceIT {

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final String DEFAULT_REVIEWER = "AAAAAAAAAA";
    private static final String UPDATED_REVIEWER = "BBBBBBBBBB";

    private static final Long DEFAULT_RATING = 1L;
    private static final Long UPDATED_RATING = 2L;

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/product-reviews";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductReviewRepository productReviewRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductReviewMockMvc;

    private ProductReview productReview;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductReview createEntity(EntityManager em) {
        ProductReview productReview = new ProductReview()
            .userId(DEFAULT_USER_ID)
            .reviewer(DEFAULT_REVIEWER)
            .rating(DEFAULT_RATING)
            .comment(DEFAULT_COMMENT)
            .createdTime(DEFAULT_CREATED_TIME)
            .updatedTime(DEFAULT_UPDATED_TIME)
            .createdBy(DEFAULT_CREATED_BY)
            .updatedBy(DEFAULT_UPDATED_BY);
        return productReview;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductReview createUpdatedEntity(EntityManager em) {
        ProductReview productReview = new ProductReview()
            .userId(UPDATED_USER_ID)
            .reviewer(UPDATED_REVIEWER)
            .rating(UPDATED_RATING)
            .comment(UPDATED_COMMENT)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);
        return productReview;
    }

    @BeforeEach
    public void initTest() {
        productReview = createEntity(em);
    }

    @Test
    @Transactional
    void createProductReview() throws Exception {
        int databaseSizeBeforeCreate = productReviewRepository.findAll().size();
        // Create the ProductReview
        restProductReviewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productReview)))
            .andExpect(status().isCreated());

        // Validate the ProductReview in the database
        List<ProductReview> productReviewList = productReviewRepository.findAll();
        assertThat(productReviewList).hasSize(databaseSizeBeforeCreate + 1);
        ProductReview testProductReview = productReviewList.get(productReviewList.size() - 1);
        assertThat(testProductReview.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testProductReview.getReviewer()).isEqualTo(DEFAULT_REVIEWER);
        assertThat(testProductReview.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testProductReview.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testProductReview.getCreatedTime()).isEqualTo(DEFAULT_CREATED_TIME);
        assertThat(testProductReview.getUpdatedTime()).isEqualTo(DEFAULT_UPDATED_TIME);
        assertThat(testProductReview.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testProductReview.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    void createProductReviewWithExistingId() throws Exception {
        // Create the ProductReview with an existing ID
        productReview.setId(1L);

        int databaseSizeBeforeCreate = productReviewRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductReviewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productReview)))
            .andExpect(status().isBadRequest());

        // Validate the ProductReview in the database
        List<ProductReview> productReviewList = productReviewRepository.findAll();
        assertThat(productReviewList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProductReviews() throws Exception {
        // Initialize the database
        productReviewRepository.saveAndFlush(productReview);

        // Get all the productReviewList
        restProductReviewMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productReview.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].reviewer").value(hasItem(DEFAULT_REVIEWER)))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING.intValue())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(sameInstant(DEFAULT_CREATED_TIME))))
            .andExpect(jsonPath("$.[*].updatedTime").value(hasItem(sameInstant(DEFAULT_UPDATED_TIME))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }

    @Test
    @Transactional
    void getProductReview() throws Exception {
        // Initialize the database
        productReviewRepository.saveAndFlush(productReview);

        // Get the productReview
        restProductReviewMockMvc
            .perform(get(ENTITY_API_URL_ID, productReview.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productReview.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.reviewer").value(DEFAULT_REVIEWER))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING.intValue()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT))
            .andExpect(jsonPath("$.createdTime").value(sameInstant(DEFAULT_CREATED_TIME)))
            .andExpect(jsonPath("$.updatedTime").value(sameInstant(DEFAULT_UPDATED_TIME)))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingProductReview() throws Exception {
        // Get the productReview
        restProductReviewMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProductReview() throws Exception {
        // Initialize the database
        productReviewRepository.saveAndFlush(productReview);

        int databaseSizeBeforeUpdate = productReviewRepository.findAll().size();

        // Update the productReview
        ProductReview updatedProductReview = productReviewRepository.findById(productReview.getId()).get();
        // Disconnect from session so that the updates on updatedProductReview are not directly saved in db
        em.detach(updatedProductReview);
        updatedProductReview
            .userId(UPDATED_USER_ID)
            .reviewer(UPDATED_REVIEWER)
            .rating(UPDATED_RATING)
            .comment(UPDATED_COMMENT)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restProductReviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductReview.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProductReview))
            )
            .andExpect(status().isOk());

        // Validate the ProductReview in the database
        List<ProductReview> productReviewList = productReviewRepository.findAll();
        assertThat(productReviewList).hasSize(databaseSizeBeforeUpdate);
        ProductReview testProductReview = productReviewList.get(productReviewList.size() - 1);
        assertThat(testProductReview.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testProductReview.getReviewer()).isEqualTo(UPDATED_REVIEWER);
        assertThat(testProductReview.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testProductReview.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testProductReview.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testProductReview.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
        assertThat(testProductReview.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testProductReview.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void putNonExistingProductReview() throws Exception {
        int databaseSizeBeforeUpdate = productReviewRepository.findAll().size();
        productReview.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductReviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productReview.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productReview))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductReview in the database
        List<ProductReview> productReviewList = productReviewRepository.findAll();
        assertThat(productReviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductReview() throws Exception {
        int databaseSizeBeforeUpdate = productReviewRepository.findAll().size();
        productReview.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductReviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productReview))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductReview in the database
        List<ProductReview> productReviewList = productReviewRepository.findAll();
        assertThat(productReviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductReview() throws Exception {
        int databaseSizeBeforeUpdate = productReviewRepository.findAll().size();
        productReview.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductReviewMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productReview)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductReview in the database
        List<ProductReview> productReviewList = productReviewRepository.findAll();
        assertThat(productReviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductReviewWithPatch() throws Exception {
        // Initialize the database
        productReviewRepository.saveAndFlush(productReview);

        int databaseSizeBeforeUpdate = productReviewRepository.findAll().size();

        // Update the productReview using partial update
        ProductReview partialUpdatedProductReview = new ProductReview();
        partialUpdatedProductReview.setId(productReview.getId());

        partialUpdatedProductReview.userId(UPDATED_USER_ID).reviewer(UPDATED_REVIEWER).rating(UPDATED_RATING).comment(UPDATED_COMMENT);

        restProductReviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductReview.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductReview))
            )
            .andExpect(status().isOk());

        // Validate the ProductReview in the database
        List<ProductReview> productReviewList = productReviewRepository.findAll();
        assertThat(productReviewList).hasSize(databaseSizeBeforeUpdate);
        ProductReview testProductReview = productReviewList.get(productReviewList.size() - 1);
        assertThat(testProductReview.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testProductReview.getReviewer()).isEqualTo(UPDATED_REVIEWER);
        assertThat(testProductReview.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testProductReview.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testProductReview.getCreatedTime()).isEqualTo(DEFAULT_CREATED_TIME);
        assertThat(testProductReview.getUpdatedTime()).isEqualTo(DEFAULT_UPDATED_TIME);
        assertThat(testProductReview.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testProductReview.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    void fullUpdateProductReviewWithPatch() throws Exception {
        // Initialize the database
        productReviewRepository.saveAndFlush(productReview);

        int databaseSizeBeforeUpdate = productReviewRepository.findAll().size();

        // Update the productReview using partial update
        ProductReview partialUpdatedProductReview = new ProductReview();
        partialUpdatedProductReview.setId(productReview.getId());

        partialUpdatedProductReview
            .userId(UPDATED_USER_ID)
            .reviewer(UPDATED_REVIEWER)
            .rating(UPDATED_RATING)
            .comment(UPDATED_COMMENT)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restProductReviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductReview.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductReview))
            )
            .andExpect(status().isOk());

        // Validate the ProductReview in the database
        List<ProductReview> productReviewList = productReviewRepository.findAll();
        assertThat(productReviewList).hasSize(databaseSizeBeforeUpdate);
        ProductReview testProductReview = productReviewList.get(productReviewList.size() - 1);
        assertThat(testProductReview.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testProductReview.getReviewer()).isEqualTo(UPDATED_REVIEWER);
        assertThat(testProductReview.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testProductReview.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testProductReview.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testProductReview.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
        assertThat(testProductReview.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testProductReview.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingProductReview() throws Exception {
        int databaseSizeBeforeUpdate = productReviewRepository.findAll().size();
        productReview.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductReviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productReview.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productReview))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductReview in the database
        List<ProductReview> productReviewList = productReviewRepository.findAll();
        assertThat(productReviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductReview() throws Exception {
        int databaseSizeBeforeUpdate = productReviewRepository.findAll().size();
        productReview.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductReviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productReview))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductReview in the database
        List<ProductReview> productReviewList = productReviewRepository.findAll();
        assertThat(productReviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductReview() throws Exception {
        int databaseSizeBeforeUpdate = productReviewRepository.findAll().size();
        productReview.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductReviewMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(productReview))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductReview in the database
        List<ProductReview> productReviewList = productReviewRepository.findAll();
        assertThat(productReviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductReview() throws Exception {
        // Initialize the database
        productReviewRepository.saveAndFlush(productReview);

        int databaseSizeBeforeDelete = productReviewRepository.findAll().size();

        // Delete the productReview
        restProductReviewMockMvc
            .perform(delete(ENTITY_API_URL_ID, productReview.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductReview> productReviewList = productReviewRepository.findAll();
        assertThat(productReviewList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
