package com.reliance.jmdb2b.web.rest;

import static com.reliance.jmdb2b.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.reliance.jmdb2b.IntegrationTest;
import com.reliance.jmdb2b.domain.ProductInventory;
import com.reliance.jmdb2b.repository.ProductInventoryRepository;
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
 * Integration tests for the {@link ProductInventoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductInventoryResourceIT {

    private static final Long DEFAULT_LOCATION = 1L;
    private static final Long UPDATED_LOCATION = 2L;

    private static final Long DEFAULT_STOCK_QUANTITY = 1L;
    private static final Long UPDATED_STOCK_QUANTITY = 2L;

    private static final Boolean DEFAULT_STOCK_STATUS = false;
    private static final Boolean UPDATED_STOCK_STATUS = true;

    private static final ZonedDateTime DEFAULT_CREATED_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/product-inventories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductInventoryRepository productInventoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductInventoryMockMvc;

    private ProductInventory productInventory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductInventory createEntity(EntityManager em) {
        ProductInventory productInventory = new ProductInventory()
            .location(DEFAULT_LOCATION)
            .stockQuantity(DEFAULT_STOCK_QUANTITY)
            .stockStatus(DEFAULT_STOCK_STATUS)
            .createdTime(DEFAULT_CREATED_TIME)
            .updatedTime(DEFAULT_UPDATED_TIME)
            .createdBy(DEFAULT_CREATED_BY)
            .updatedBy(DEFAULT_UPDATED_BY);
        return productInventory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductInventory createUpdatedEntity(EntityManager em) {
        ProductInventory productInventory = new ProductInventory()
            .location(UPDATED_LOCATION)
            .stockQuantity(UPDATED_STOCK_QUANTITY)
            .stockStatus(UPDATED_STOCK_STATUS)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);
        return productInventory;
    }

    @BeforeEach
    public void initTest() {
        productInventory = createEntity(em);
    }

    @Test
    @Transactional
    void createProductInventory() throws Exception {
        int databaseSizeBeforeCreate = productInventoryRepository.findAll().size();
        // Create the ProductInventory
        restProductInventoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productInventory))
            )
            .andExpect(status().isCreated());

        // Validate the ProductInventory in the database
        List<ProductInventory> productInventoryList = productInventoryRepository.findAll();
        assertThat(productInventoryList).hasSize(databaseSizeBeforeCreate + 1);
        ProductInventory testProductInventory = productInventoryList.get(productInventoryList.size() - 1);
        assertThat(testProductInventory.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testProductInventory.getStockQuantity()).isEqualTo(DEFAULT_STOCK_QUANTITY);
        assertThat(testProductInventory.getStockStatus()).isEqualTo(DEFAULT_STOCK_STATUS);
        assertThat(testProductInventory.getCreatedTime()).isEqualTo(DEFAULT_CREATED_TIME);
        assertThat(testProductInventory.getUpdatedTime()).isEqualTo(DEFAULT_UPDATED_TIME);
        assertThat(testProductInventory.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testProductInventory.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    void createProductInventoryWithExistingId() throws Exception {
        // Create the ProductInventory with an existing ID
        productInventory.setId(1L);

        int databaseSizeBeforeCreate = productInventoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductInventoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productInventory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductInventory in the database
        List<ProductInventory> productInventoryList = productInventoryRepository.findAll();
        assertThat(productInventoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProductInventories() throws Exception {
        // Initialize the database
        productInventoryRepository.saveAndFlush(productInventory);

        // Get all the productInventoryList
        restProductInventoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productInventory.getId().intValue())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.intValue())))
            .andExpect(jsonPath("$.[*].stockQuantity").value(hasItem(DEFAULT_STOCK_QUANTITY.intValue())))
            .andExpect(jsonPath("$.[*].stockStatus").value(hasItem(DEFAULT_STOCK_STATUS.booleanValue())))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(sameInstant(DEFAULT_CREATED_TIME))))
            .andExpect(jsonPath("$.[*].updatedTime").value(hasItem(sameInstant(DEFAULT_UPDATED_TIME))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }

    @Test
    @Transactional
    void getProductInventory() throws Exception {
        // Initialize the database
        productInventoryRepository.saveAndFlush(productInventory);

        // Get the productInventory
        restProductInventoryMockMvc
            .perform(get(ENTITY_API_URL_ID, productInventory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productInventory.getId().intValue()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.intValue()))
            .andExpect(jsonPath("$.stockQuantity").value(DEFAULT_STOCK_QUANTITY.intValue()))
            .andExpect(jsonPath("$.stockStatus").value(DEFAULT_STOCK_STATUS.booleanValue()))
            .andExpect(jsonPath("$.createdTime").value(sameInstant(DEFAULT_CREATED_TIME)))
            .andExpect(jsonPath("$.updatedTime").value(sameInstant(DEFAULT_UPDATED_TIME)))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingProductInventory() throws Exception {
        // Get the productInventory
        restProductInventoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProductInventory() throws Exception {
        // Initialize the database
        productInventoryRepository.saveAndFlush(productInventory);

        int databaseSizeBeforeUpdate = productInventoryRepository.findAll().size();

        // Update the productInventory
        ProductInventory updatedProductInventory = productInventoryRepository.findById(productInventory.getId()).get();
        // Disconnect from session so that the updates on updatedProductInventory are not directly saved in db
        em.detach(updatedProductInventory);
        updatedProductInventory
            .location(UPDATED_LOCATION)
            .stockQuantity(UPDATED_STOCK_QUANTITY)
            .stockStatus(UPDATED_STOCK_STATUS)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restProductInventoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductInventory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProductInventory))
            )
            .andExpect(status().isOk());

        // Validate the ProductInventory in the database
        List<ProductInventory> productInventoryList = productInventoryRepository.findAll();
        assertThat(productInventoryList).hasSize(databaseSizeBeforeUpdate);
        ProductInventory testProductInventory = productInventoryList.get(productInventoryList.size() - 1);
        assertThat(testProductInventory.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testProductInventory.getStockQuantity()).isEqualTo(UPDATED_STOCK_QUANTITY);
        assertThat(testProductInventory.getStockStatus()).isEqualTo(UPDATED_STOCK_STATUS);
        assertThat(testProductInventory.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testProductInventory.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
        assertThat(testProductInventory.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testProductInventory.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void putNonExistingProductInventory() throws Exception {
        int databaseSizeBeforeUpdate = productInventoryRepository.findAll().size();
        productInventory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductInventoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productInventory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productInventory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductInventory in the database
        List<ProductInventory> productInventoryList = productInventoryRepository.findAll();
        assertThat(productInventoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductInventory() throws Exception {
        int databaseSizeBeforeUpdate = productInventoryRepository.findAll().size();
        productInventory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductInventoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productInventory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductInventory in the database
        List<ProductInventory> productInventoryList = productInventoryRepository.findAll();
        assertThat(productInventoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductInventory() throws Exception {
        int databaseSizeBeforeUpdate = productInventoryRepository.findAll().size();
        productInventory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductInventoryMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productInventory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductInventory in the database
        List<ProductInventory> productInventoryList = productInventoryRepository.findAll();
        assertThat(productInventoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductInventoryWithPatch() throws Exception {
        // Initialize the database
        productInventoryRepository.saveAndFlush(productInventory);

        int databaseSizeBeforeUpdate = productInventoryRepository.findAll().size();

        // Update the productInventory using partial update
        ProductInventory partialUpdatedProductInventory = new ProductInventory();
        partialUpdatedProductInventory.setId(productInventory.getId());

        partialUpdatedProductInventory
            .location(UPDATED_LOCATION)
            .stockQuantity(UPDATED_STOCK_QUANTITY)
            .createdTime(UPDATED_CREATED_TIME)
            .createdBy(UPDATED_CREATED_BY);

        restProductInventoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductInventory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductInventory))
            )
            .andExpect(status().isOk());

        // Validate the ProductInventory in the database
        List<ProductInventory> productInventoryList = productInventoryRepository.findAll();
        assertThat(productInventoryList).hasSize(databaseSizeBeforeUpdate);
        ProductInventory testProductInventory = productInventoryList.get(productInventoryList.size() - 1);
        assertThat(testProductInventory.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testProductInventory.getStockQuantity()).isEqualTo(UPDATED_STOCK_QUANTITY);
        assertThat(testProductInventory.getStockStatus()).isEqualTo(DEFAULT_STOCK_STATUS);
        assertThat(testProductInventory.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testProductInventory.getUpdatedTime()).isEqualTo(DEFAULT_UPDATED_TIME);
        assertThat(testProductInventory.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testProductInventory.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    void fullUpdateProductInventoryWithPatch() throws Exception {
        // Initialize the database
        productInventoryRepository.saveAndFlush(productInventory);

        int databaseSizeBeforeUpdate = productInventoryRepository.findAll().size();

        // Update the productInventory using partial update
        ProductInventory partialUpdatedProductInventory = new ProductInventory();
        partialUpdatedProductInventory.setId(productInventory.getId());

        partialUpdatedProductInventory
            .location(UPDATED_LOCATION)
            .stockQuantity(UPDATED_STOCK_QUANTITY)
            .stockStatus(UPDATED_STOCK_STATUS)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restProductInventoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductInventory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductInventory))
            )
            .andExpect(status().isOk());

        // Validate the ProductInventory in the database
        List<ProductInventory> productInventoryList = productInventoryRepository.findAll();
        assertThat(productInventoryList).hasSize(databaseSizeBeforeUpdate);
        ProductInventory testProductInventory = productInventoryList.get(productInventoryList.size() - 1);
        assertThat(testProductInventory.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testProductInventory.getStockQuantity()).isEqualTo(UPDATED_STOCK_QUANTITY);
        assertThat(testProductInventory.getStockStatus()).isEqualTo(UPDATED_STOCK_STATUS);
        assertThat(testProductInventory.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testProductInventory.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
        assertThat(testProductInventory.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testProductInventory.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingProductInventory() throws Exception {
        int databaseSizeBeforeUpdate = productInventoryRepository.findAll().size();
        productInventory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductInventoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productInventory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productInventory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductInventory in the database
        List<ProductInventory> productInventoryList = productInventoryRepository.findAll();
        assertThat(productInventoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductInventory() throws Exception {
        int databaseSizeBeforeUpdate = productInventoryRepository.findAll().size();
        productInventory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductInventoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productInventory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductInventory in the database
        List<ProductInventory> productInventoryList = productInventoryRepository.findAll();
        assertThat(productInventoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductInventory() throws Exception {
        int databaseSizeBeforeUpdate = productInventoryRepository.findAll().size();
        productInventory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductInventoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productInventory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductInventory in the database
        List<ProductInventory> productInventoryList = productInventoryRepository.findAll();
        assertThat(productInventoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductInventory() throws Exception {
        // Initialize the database
        productInventoryRepository.saveAndFlush(productInventory);

        int databaseSizeBeforeDelete = productInventoryRepository.findAll().size();

        // Delete the productInventory
        restProductInventoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, productInventory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductInventory> productInventoryList = productInventoryRepository.findAll();
        assertThat(productInventoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
