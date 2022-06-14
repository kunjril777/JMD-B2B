package com.reliance.jmdb2b.web.rest;

import static com.reliance.jmdb2b.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.reliance.jmdb2b.IntegrationTest;
import com.reliance.jmdb2b.domain.ProductVariant;
import com.reliance.jmdb2b.repository.ProductVariantRepository;
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
 * Integration tests for the {@link ProductVariantResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductVariantResourceIT {

    private static final Long DEFAULT_PRODUCT_VARIANT_ID = 1L;
    private static final Long UPDATED_PRODUCT_VARIANT_ID = 2L;

    private static final Float DEFAULT_PRODUCT_PRICE = 1F;
    private static final Float UPDATED_PRODUCT_PRICE = 2F;

    private static final Float DEFAULT_DEAL_PRICE = 1F;
    private static final Float UPDATED_DEAL_PRICE = 2F;

    private static final Float DEFAULT_MRP = 1F;
    private static final Float UPDATED_MRP = 2F;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGES = "AAAAAAAAAA";
    private static final String UPDATED_IMAGES = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/product-variants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductVariantMockMvc;

    private ProductVariant productVariant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductVariant createEntity(EntityManager em) {
        ProductVariant productVariant = new ProductVariant()
            .productVariantId(DEFAULT_PRODUCT_VARIANT_ID)
            .productPrice(DEFAULT_PRODUCT_PRICE)
            .dealPrice(DEFAULT_DEAL_PRICE)
            .mrp(DEFAULT_MRP)
            .description(DEFAULT_DESCRIPTION)
            .title(DEFAULT_TITLE)
            .images(DEFAULT_IMAGES)
            .createdTime(DEFAULT_CREATED_TIME)
            .updatedTime(DEFAULT_UPDATED_TIME)
            .createdBy(DEFAULT_CREATED_BY)
            .updatedBy(DEFAULT_UPDATED_BY);
        return productVariant;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductVariant createUpdatedEntity(EntityManager em) {
        ProductVariant productVariant = new ProductVariant()
            .productVariantId(UPDATED_PRODUCT_VARIANT_ID)
            .productPrice(UPDATED_PRODUCT_PRICE)
            .dealPrice(UPDATED_DEAL_PRICE)
            .mrp(UPDATED_MRP)
            .description(UPDATED_DESCRIPTION)
            .title(UPDATED_TITLE)
            .images(UPDATED_IMAGES)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);
        return productVariant;
    }

    @BeforeEach
    public void initTest() {
        productVariant = createEntity(em);
    }

    @Test
    @Transactional
    void createProductVariant() throws Exception {
        int databaseSizeBeforeCreate = productVariantRepository.findAll().size();
        // Create the ProductVariant
        restProductVariantMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productVariant))
            )
            .andExpect(status().isCreated());

        // Validate the ProductVariant in the database
        List<ProductVariant> productVariantList = productVariantRepository.findAll();
        assertThat(productVariantList).hasSize(databaseSizeBeforeCreate + 1);
        ProductVariant testProductVariant = productVariantList.get(productVariantList.size() - 1);
        assertThat(testProductVariant.getProductVariantId()).isEqualTo(DEFAULT_PRODUCT_VARIANT_ID);
        assertThat(testProductVariant.getProductPrice()).isEqualTo(DEFAULT_PRODUCT_PRICE);
        assertThat(testProductVariant.getDealPrice()).isEqualTo(DEFAULT_DEAL_PRICE);
        assertThat(testProductVariant.getMrp()).isEqualTo(DEFAULT_MRP);
        assertThat(testProductVariant.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProductVariant.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testProductVariant.getImages()).isEqualTo(DEFAULT_IMAGES);
        assertThat(testProductVariant.getCreatedTime()).isEqualTo(DEFAULT_CREATED_TIME);
        assertThat(testProductVariant.getUpdatedTime()).isEqualTo(DEFAULT_UPDATED_TIME);
        assertThat(testProductVariant.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testProductVariant.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    void createProductVariantWithExistingId() throws Exception {
        // Create the ProductVariant with an existing ID
        productVariant.setId(1L);

        int databaseSizeBeforeCreate = productVariantRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductVariantMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productVariant))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductVariant in the database
        List<ProductVariant> productVariantList = productVariantRepository.findAll();
        assertThat(productVariantList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkProductPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = productVariantRepository.findAll().size();
        // set the field null
        productVariant.setProductPrice(null);

        // Create the ProductVariant, which fails.

        restProductVariantMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productVariant))
            )
            .andExpect(status().isBadRequest());

        List<ProductVariant> productVariantList = productVariantRepository.findAll();
        assertThat(productVariantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductVariants() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList
        restProductVariantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productVariant.getId().intValue())))
            .andExpect(jsonPath("$.[*].productVariantId").value(hasItem(DEFAULT_PRODUCT_VARIANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].productPrice").value(hasItem(DEFAULT_PRODUCT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].dealPrice").value(hasItem(DEFAULT_DEAL_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].mrp").value(hasItem(DEFAULT_MRP.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].images").value(hasItem(DEFAULT_IMAGES)))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(sameInstant(DEFAULT_CREATED_TIME))))
            .andExpect(jsonPath("$.[*].updatedTime").value(hasItem(sameInstant(DEFAULT_UPDATED_TIME))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }

    @Test
    @Transactional
    void getProductVariant() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get the productVariant
        restProductVariantMockMvc
            .perform(get(ENTITY_API_URL_ID, productVariant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productVariant.getId().intValue()))
            .andExpect(jsonPath("$.productVariantId").value(DEFAULT_PRODUCT_VARIANT_ID.intValue()))
            .andExpect(jsonPath("$.productPrice").value(DEFAULT_PRODUCT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.dealPrice").value(DEFAULT_DEAL_PRICE.doubleValue()))
            .andExpect(jsonPath("$.mrp").value(DEFAULT_MRP.doubleValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.images").value(DEFAULT_IMAGES))
            .andExpect(jsonPath("$.createdTime").value(sameInstant(DEFAULT_CREATED_TIME)))
            .andExpect(jsonPath("$.updatedTime").value(sameInstant(DEFAULT_UPDATED_TIME)))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingProductVariant() throws Exception {
        // Get the productVariant
        restProductVariantMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProductVariant() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        int databaseSizeBeforeUpdate = productVariantRepository.findAll().size();

        // Update the productVariant
        ProductVariant updatedProductVariant = productVariantRepository.findById(productVariant.getId()).get();
        // Disconnect from session so that the updates on updatedProductVariant are not directly saved in db
        em.detach(updatedProductVariant);
        updatedProductVariant
            .productVariantId(UPDATED_PRODUCT_VARIANT_ID)
            .productPrice(UPDATED_PRODUCT_PRICE)
            .dealPrice(UPDATED_DEAL_PRICE)
            .mrp(UPDATED_MRP)
            .description(UPDATED_DESCRIPTION)
            .title(UPDATED_TITLE)
            .images(UPDATED_IMAGES)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restProductVariantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductVariant.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProductVariant))
            )
            .andExpect(status().isOk());

        // Validate the ProductVariant in the database
        List<ProductVariant> productVariantList = productVariantRepository.findAll();
        assertThat(productVariantList).hasSize(databaseSizeBeforeUpdate);
        ProductVariant testProductVariant = productVariantList.get(productVariantList.size() - 1);
        assertThat(testProductVariant.getProductVariantId()).isEqualTo(UPDATED_PRODUCT_VARIANT_ID);
        assertThat(testProductVariant.getProductPrice()).isEqualTo(UPDATED_PRODUCT_PRICE);
        assertThat(testProductVariant.getDealPrice()).isEqualTo(UPDATED_DEAL_PRICE);
        assertThat(testProductVariant.getMrp()).isEqualTo(UPDATED_MRP);
        assertThat(testProductVariant.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProductVariant.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testProductVariant.getImages()).isEqualTo(UPDATED_IMAGES);
        assertThat(testProductVariant.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testProductVariant.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
        assertThat(testProductVariant.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testProductVariant.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void putNonExistingProductVariant() throws Exception {
        int databaseSizeBeforeUpdate = productVariantRepository.findAll().size();
        productVariant.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductVariantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productVariant.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productVariant))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductVariant in the database
        List<ProductVariant> productVariantList = productVariantRepository.findAll();
        assertThat(productVariantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductVariant() throws Exception {
        int databaseSizeBeforeUpdate = productVariantRepository.findAll().size();
        productVariant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductVariantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productVariant))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductVariant in the database
        List<ProductVariant> productVariantList = productVariantRepository.findAll();
        assertThat(productVariantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductVariant() throws Exception {
        int databaseSizeBeforeUpdate = productVariantRepository.findAll().size();
        productVariant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductVariantMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productVariant)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductVariant in the database
        List<ProductVariant> productVariantList = productVariantRepository.findAll();
        assertThat(productVariantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductVariantWithPatch() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        int databaseSizeBeforeUpdate = productVariantRepository.findAll().size();

        // Update the productVariant using partial update
        ProductVariant partialUpdatedProductVariant = new ProductVariant();
        partialUpdatedProductVariant.setId(productVariant.getId());

        partialUpdatedProductVariant
            .productPrice(UPDATED_PRODUCT_PRICE)
            .mrp(UPDATED_MRP)
            .createdTime(UPDATED_CREATED_TIME)
            .createdBy(UPDATED_CREATED_BY);

        restProductVariantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductVariant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductVariant))
            )
            .andExpect(status().isOk());

        // Validate the ProductVariant in the database
        List<ProductVariant> productVariantList = productVariantRepository.findAll();
        assertThat(productVariantList).hasSize(databaseSizeBeforeUpdate);
        ProductVariant testProductVariant = productVariantList.get(productVariantList.size() - 1);
        assertThat(testProductVariant.getProductVariantId()).isEqualTo(DEFAULT_PRODUCT_VARIANT_ID);
        assertThat(testProductVariant.getProductPrice()).isEqualTo(UPDATED_PRODUCT_PRICE);
        assertThat(testProductVariant.getDealPrice()).isEqualTo(DEFAULT_DEAL_PRICE);
        assertThat(testProductVariant.getMrp()).isEqualTo(UPDATED_MRP);
        assertThat(testProductVariant.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProductVariant.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testProductVariant.getImages()).isEqualTo(DEFAULT_IMAGES);
        assertThat(testProductVariant.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testProductVariant.getUpdatedTime()).isEqualTo(DEFAULT_UPDATED_TIME);
        assertThat(testProductVariant.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testProductVariant.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    void fullUpdateProductVariantWithPatch() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        int databaseSizeBeforeUpdate = productVariantRepository.findAll().size();

        // Update the productVariant using partial update
        ProductVariant partialUpdatedProductVariant = new ProductVariant();
        partialUpdatedProductVariant.setId(productVariant.getId());

        partialUpdatedProductVariant
            .productVariantId(UPDATED_PRODUCT_VARIANT_ID)
            .productPrice(UPDATED_PRODUCT_PRICE)
            .dealPrice(UPDATED_DEAL_PRICE)
            .mrp(UPDATED_MRP)
            .description(UPDATED_DESCRIPTION)
            .title(UPDATED_TITLE)
            .images(UPDATED_IMAGES)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restProductVariantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductVariant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductVariant))
            )
            .andExpect(status().isOk());

        // Validate the ProductVariant in the database
        List<ProductVariant> productVariantList = productVariantRepository.findAll();
        assertThat(productVariantList).hasSize(databaseSizeBeforeUpdate);
        ProductVariant testProductVariant = productVariantList.get(productVariantList.size() - 1);
        assertThat(testProductVariant.getProductVariantId()).isEqualTo(UPDATED_PRODUCT_VARIANT_ID);
        assertThat(testProductVariant.getProductPrice()).isEqualTo(UPDATED_PRODUCT_PRICE);
        assertThat(testProductVariant.getDealPrice()).isEqualTo(UPDATED_DEAL_PRICE);
        assertThat(testProductVariant.getMrp()).isEqualTo(UPDATED_MRP);
        assertThat(testProductVariant.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProductVariant.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testProductVariant.getImages()).isEqualTo(UPDATED_IMAGES);
        assertThat(testProductVariant.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testProductVariant.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
        assertThat(testProductVariant.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testProductVariant.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingProductVariant() throws Exception {
        int databaseSizeBeforeUpdate = productVariantRepository.findAll().size();
        productVariant.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductVariantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productVariant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productVariant))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductVariant in the database
        List<ProductVariant> productVariantList = productVariantRepository.findAll();
        assertThat(productVariantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductVariant() throws Exception {
        int databaseSizeBeforeUpdate = productVariantRepository.findAll().size();
        productVariant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductVariantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productVariant))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductVariant in the database
        List<ProductVariant> productVariantList = productVariantRepository.findAll();
        assertThat(productVariantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductVariant() throws Exception {
        int databaseSizeBeforeUpdate = productVariantRepository.findAll().size();
        productVariant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductVariantMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(productVariant))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductVariant in the database
        List<ProductVariant> productVariantList = productVariantRepository.findAll();
        assertThat(productVariantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductVariant() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        int databaseSizeBeforeDelete = productVariantRepository.findAll().size();

        // Delete the productVariant
        restProductVariantMockMvc
            .perform(delete(ENTITY_API_URL_ID, productVariant.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductVariant> productVariantList = productVariantRepository.findAll();
        assertThat(productVariantList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
