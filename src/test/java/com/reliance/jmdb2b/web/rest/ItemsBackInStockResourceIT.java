package com.reliance.jmdb2b.web.rest;

import static com.reliance.jmdb2b.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.reliance.jmdb2b.IntegrationTest;
import com.reliance.jmdb2b.domain.ItemsBackInStock;
import com.reliance.jmdb2b.repository.ItemsBackInStockRepository;
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
 * Integration tests for the {@link ItemsBackInStockResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ItemsBackInStockResourceIT {

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final Long DEFAULT_PRODUCT_ID = 1L;
    private static final Long UPDATED_PRODUCT_ID = 2L;

    private static final Boolean DEFAULT_STOCK_STATUS = false;
    private static final Boolean UPDATED_STOCK_STATUS = true;

    private static final ZonedDateTime DEFAULT_STATUS_UPDATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_STATUS_UPDATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_CREATED_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/items-back-in-stocks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ItemsBackInStockRepository itemsBackInStockRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restItemsBackInStockMockMvc;

    private ItemsBackInStock itemsBackInStock;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemsBackInStock createEntity(EntityManager em) {
        ItemsBackInStock itemsBackInStock = new ItemsBackInStock()
            .userId(DEFAULT_USER_ID)
            .productId(DEFAULT_PRODUCT_ID)
            .stockStatus(DEFAULT_STOCK_STATUS)
            .statusUpdateTime(DEFAULT_STATUS_UPDATE_TIME)
            .createdTime(DEFAULT_CREATED_TIME)
            .updatedTime(DEFAULT_UPDATED_TIME)
            .createdBy(DEFAULT_CREATED_BY)
            .updatedBy(DEFAULT_UPDATED_BY);
        return itemsBackInStock;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemsBackInStock createUpdatedEntity(EntityManager em) {
        ItemsBackInStock itemsBackInStock = new ItemsBackInStock()
            .userId(UPDATED_USER_ID)
            .productId(UPDATED_PRODUCT_ID)
            .stockStatus(UPDATED_STOCK_STATUS)
            .statusUpdateTime(UPDATED_STATUS_UPDATE_TIME)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);
        return itemsBackInStock;
    }

    @BeforeEach
    public void initTest() {
        itemsBackInStock = createEntity(em);
    }

    @Test
    @Transactional
    void createItemsBackInStock() throws Exception {
        int databaseSizeBeforeCreate = itemsBackInStockRepository.findAll().size();
        // Create the ItemsBackInStock
        restItemsBackInStockMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemsBackInStock))
            )
            .andExpect(status().isCreated());

        // Validate the ItemsBackInStock in the database
        List<ItemsBackInStock> itemsBackInStockList = itemsBackInStockRepository.findAll();
        assertThat(itemsBackInStockList).hasSize(databaseSizeBeforeCreate + 1);
        ItemsBackInStock testItemsBackInStock = itemsBackInStockList.get(itemsBackInStockList.size() - 1);
        assertThat(testItemsBackInStock.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testItemsBackInStock.getProductId()).isEqualTo(DEFAULT_PRODUCT_ID);
        assertThat(testItemsBackInStock.getStockStatus()).isEqualTo(DEFAULT_STOCK_STATUS);
        assertThat(testItemsBackInStock.getStatusUpdateTime()).isEqualTo(DEFAULT_STATUS_UPDATE_TIME);
        assertThat(testItemsBackInStock.getCreatedTime()).isEqualTo(DEFAULT_CREATED_TIME);
        assertThat(testItemsBackInStock.getUpdatedTime()).isEqualTo(DEFAULT_UPDATED_TIME);
        assertThat(testItemsBackInStock.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testItemsBackInStock.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    void createItemsBackInStockWithExistingId() throws Exception {
        // Create the ItemsBackInStock with an existing ID
        itemsBackInStock.setId(1L);

        int databaseSizeBeforeCreate = itemsBackInStockRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemsBackInStockMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemsBackInStock))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemsBackInStock in the database
        List<ItemsBackInStock> itemsBackInStockList = itemsBackInStockRepository.findAll();
        assertThat(itemsBackInStockList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllItemsBackInStocks() throws Exception {
        // Initialize the database
        itemsBackInStockRepository.saveAndFlush(itemsBackInStock);

        // Get all the itemsBackInStockList
        restItemsBackInStockMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemsBackInStock.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.[*].stockStatus").value(hasItem(DEFAULT_STOCK_STATUS.booleanValue())))
            .andExpect(jsonPath("$.[*].statusUpdateTime").value(hasItem(sameInstant(DEFAULT_STATUS_UPDATE_TIME))))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(sameInstant(DEFAULT_CREATED_TIME))))
            .andExpect(jsonPath("$.[*].updatedTime").value(hasItem(sameInstant(DEFAULT_UPDATED_TIME))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }

    @Test
    @Transactional
    void getItemsBackInStock() throws Exception {
        // Initialize the database
        itemsBackInStockRepository.saveAndFlush(itemsBackInStock);

        // Get the itemsBackInStock
        restItemsBackInStockMockMvc
            .perform(get(ENTITY_API_URL_ID, itemsBackInStock.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(itemsBackInStock.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.productId").value(DEFAULT_PRODUCT_ID.intValue()))
            .andExpect(jsonPath("$.stockStatus").value(DEFAULT_STOCK_STATUS.booleanValue()))
            .andExpect(jsonPath("$.statusUpdateTime").value(sameInstant(DEFAULT_STATUS_UPDATE_TIME)))
            .andExpect(jsonPath("$.createdTime").value(sameInstant(DEFAULT_CREATED_TIME)))
            .andExpect(jsonPath("$.updatedTime").value(sameInstant(DEFAULT_UPDATED_TIME)))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingItemsBackInStock() throws Exception {
        // Get the itemsBackInStock
        restItemsBackInStockMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewItemsBackInStock() throws Exception {
        // Initialize the database
        itemsBackInStockRepository.saveAndFlush(itemsBackInStock);

        int databaseSizeBeforeUpdate = itemsBackInStockRepository.findAll().size();

        // Update the itemsBackInStock
        ItemsBackInStock updatedItemsBackInStock = itemsBackInStockRepository.findById(itemsBackInStock.getId()).get();
        // Disconnect from session so that the updates on updatedItemsBackInStock are not directly saved in db
        em.detach(updatedItemsBackInStock);
        updatedItemsBackInStock
            .userId(UPDATED_USER_ID)
            .productId(UPDATED_PRODUCT_ID)
            .stockStatus(UPDATED_STOCK_STATUS)
            .statusUpdateTime(UPDATED_STATUS_UPDATE_TIME)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restItemsBackInStockMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedItemsBackInStock.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedItemsBackInStock))
            )
            .andExpect(status().isOk());

        // Validate the ItemsBackInStock in the database
        List<ItemsBackInStock> itemsBackInStockList = itemsBackInStockRepository.findAll();
        assertThat(itemsBackInStockList).hasSize(databaseSizeBeforeUpdate);
        ItemsBackInStock testItemsBackInStock = itemsBackInStockList.get(itemsBackInStockList.size() - 1);
        assertThat(testItemsBackInStock.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testItemsBackInStock.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testItemsBackInStock.getStockStatus()).isEqualTo(UPDATED_STOCK_STATUS);
        assertThat(testItemsBackInStock.getStatusUpdateTime()).isEqualTo(UPDATED_STATUS_UPDATE_TIME);
        assertThat(testItemsBackInStock.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testItemsBackInStock.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
        assertThat(testItemsBackInStock.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testItemsBackInStock.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void putNonExistingItemsBackInStock() throws Exception {
        int databaseSizeBeforeUpdate = itemsBackInStockRepository.findAll().size();
        itemsBackInStock.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemsBackInStockMockMvc
            .perform(
                put(ENTITY_API_URL_ID, itemsBackInStock.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemsBackInStock))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemsBackInStock in the database
        List<ItemsBackInStock> itemsBackInStockList = itemsBackInStockRepository.findAll();
        assertThat(itemsBackInStockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchItemsBackInStock() throws Exception {
        int databaseSizeBeforeUpdate = itemsBackInStockRepository.findAll().size();
        itemsBackInStock.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemsBackInStockMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemsBackInStock))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemsBackInStock in the database
        List<ItemsBackInStock> itemsBackInStockList = itemsBackInStockRepository.findAll();
        assertThat(itemsBackInStockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamItemsBackInStock() throws Exception {
        int databaseSizeBeforeUpdate = itemsBackInStockRepository.findAll().size();
        itemsBackInStock.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemsBackInStockMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemsBackInStock))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ItemsBackInStock in the database
        List<ItemsBackInStock> itemsBackInStockList = itemsBackInStockRepository.findAll();
        assertThat(itemsBackInStockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateItemsBackInStockWithPatch() throws Exception {
        // Initialize the database
        itemsBackInStockRepository.saveAndFlush(itemsBackInStock);

        int databaseSizeBeforeUpdate = itemsBackInStockRepository.findAll().size();

        // Update the itemsBackInStock using partial update
        ItemsBackInStock partialUpdatedItemsBackInStock = new ItemsBackInStock();
        partialUpdatedItemsBackInStock.setId(itemsBackInStock.getId());

        partialUpdatedItemsBackInStock
            .productId(UPDATED_PRODUCT_ID)
            .stockStatus(UPDATED_STOCK_STATUS)
            .createdTime(UPDATED_CREATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restItemsBackInStockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItemsBackInStock.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedItemsBackInStock))
            )
            .andExpect(status().isOk());

        // Validate the ItemsBackInStock in the database
        List<ItemsBackInStock> itemsBackInStockList = itemsBackInStockRepository.findAll();
        assertThat(itemsBackInStockList).hasSize(databaseSizeBeforeUpdate);
        ItemsBackInStock testItemsBackInStock = itemsBackInStockList.get(itemsBackInStockList.size() - 1);
        assertThat(testItemsBackInStock.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testItemsBackInStock.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testItemsBackInStock.getStockStatus()).isEqualTo(UPDATED_STOCK_STATUS);
        assertThat(testItemsBackInStock.getStatusUpdateTime()).isEqualTo(DEFAULT_STATUS_UPDATE_TIME);
        assertThat(testItemsBackInStock.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testItemsBackInStock.getUpdatedTime()).isEqualTo(DEFAULT_UPDATED_TIME);
        assertThat(testItemsBackInStock.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testItemsBackInStock.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void fullUpdateItemsBackInStockWithPatch() throws Exception {
        // Initialize the database
        itemsBackInStockRepository.saveAndFlush(itemsBackInStock);

        int databaseSizeBeforeUpdate = itemsBackInStockRepository.findAll().size();

        // Update the itemsBackInStock using partial update
        ItemsBackInStock partialUpdatedItemsBackInStock = new ItemsBackInStock();
        partialUpdatedItemsBackInStock.setId(itemsBackInStock.getId());

        partialUpdatedItemsBackInStock
            .userId(UPDATED_USER_ID)
            .productId(UPDATED_PRODUCT_ID)
            .stockStatus(UPDATED_STOCK_STATUS)
            .statusUpdateTime(UPDATED_STATUS_UPDATE_TIME)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restItemsBackInStockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItemsBackInStock.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedItemsBackInStock))
            )
            .andExpect(status().isOk());

        // Validate the ItemsBackInStock in the database
        List<ItemsBackInStock> itemsBackInStockList = itemsBackInStockRepository.findAll();
        assertThat(itemsBackInStockList).hasSize(databaseSizeBeforeUpdate);
        ItemsBackInStock testItemsBackInStock = itemsBackInStockList.get(itemsBackInStockList.size() - 1);
        assertThat(testItemsBackInStock.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testItemsBackInStock.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testItemsBackInStock.getStockStatus()).isEqualTo(UPDATED_STOCK_STATUS);
        assertThat(testItemsBackInStock.getStatusUpdateTime()).isEqualTo(UPDATED_STATUS_UPDATE_TIME);
        assertThat(testItemsBackInStock.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testItemsBackInStock.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
        assertThat(testItemsBackInStock.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testItemsBackInStock.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingItemsBackInStock() throws Exception {
        int databaseSizeBeforeUpdate = itemsBackInStockRepository.findAll().size();
        itemsBackInStock.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemsBackInStockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, itemsBackInStock.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemsBackInStock))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemsBackInStock in the database
        List<ItemsBackInStock> itemsBackInStockList = itemsBackInStockRepository.findAll();
        assertThat(itemsBackInStockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchItemsBackInStock() throws Exception {
        int databaseSizeBeforeUpdate = itemsBackInStockRepository.findAll().size();
        itemsBackInStock.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemsBackInStockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemsBackInStock))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemsBackInStock in the database
        List<ItemsBackInStock> itemsBackInStockList = itemsBackInStockRepository.findAll();
        assertThat(itemsBackInStockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamItemsBackInStock() throws Exception {
        int databaseSizeBeforeUpdate = itemsBackInStockRepository.findAll().size();
        itemsBackInStock.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemsBackInStockMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemsBackInStock))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ItemsBackInStock in the database
        List<ItemsBackInStock> itemsBackInStockList = itemsBackInStockRepository.findAll();
        assertThat(itemsBackInStockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteItemsBackInStock() throws Exception {
        // Initialize the database
        itemsBackInStockRepository.saveAndFlush(itemsBackInStock);

        int databaseSizeBeforeDelete = itemsBackInStockRepository.findAll().size();

        // Delete the itemsBackInStock
        restItemsBackInStockMockMvc
            .perform(delete(ENTITY_API_URL_ID, itemsBackInStock.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ItemsBackInStock> itemsBackInStockList = itemsBackInStockRepository.findAll();
        assertThat(itemsBackInStockList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
