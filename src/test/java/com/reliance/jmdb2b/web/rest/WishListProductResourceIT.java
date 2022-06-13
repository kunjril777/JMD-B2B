package com.reliance.jmdb2b.web.rest;

import static com.reliance.jmdb2b.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.reliance.jmdb2b.IntegrationTest;
import com.reliance.jmdb2b.domain.WishListProduct;
import com.reliance.jmdb2b.repository.WishListProductRepository;
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
 * Integration tests for the {@link WishListProductResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WishListProductResourceIT {

    private static final Long DEFAULT_QUANTITY = 1L;
    private static final Long UPDATED_QUANTITY = 2L;

    private static final Long DEFAULT_PRODUCT_ID = 1L;
    private static final Long UPDATED_PRODUCT_ID = 2L;

    private static final Long DEFAULT_PRICE = 1L;
    private static final Long UPDATED_PRICE = 2L;

    private static final ZonedDateTime DEFAULT_CREATED_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/wish-list-products";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WishListProductRepository wishListProductRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWishListProductMockMvc;

    private WishListProduct wishListProduct;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WishListProduct createEntity(EntityManager em) {
        WishListProduct wishListProduct = new WishListProduct()
            .quantity(DEFAULT_QUANTITY)
            .productId(DEFAULT_PRODUCT_ID)
            .price(DEFAULT_PRICE)
            .createdTime(DEFAULT_CREATED_TIME)
            .updatedTime(DEFAULT_UPDATED_TIME)
            .createdBy(DEFAULT_CREATED_BY)
            .updatedBy(DEFAULT_UPDATED_BY);
        return wishListProduct;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WishListProduct createUpdatedEntity(EntityManager em) {
        WishListProduct wishListProduct = new WishListProduct()
            .quantity(UPDATED_QUANTITY)
            .productId(UPDATED_PRODUCT_ID)
            .price(UPDATED_PRICE)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);
        return wishListProduct;
    }

    @BeforeEach
    public void initTest() {
        wishListProduct = createEntity(em);
    }

    @Test
    @Transactional
    void createWishListProduct() throws Exception {
        int databaseSizeBeforeCreate = wishListProductRepository.findAll().size();
        // Create the WishListProduct
        restWishListProductMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wishListProduct))
            )
            .andExpect(status().isCreated());

        // Validate the WishListProduct in the database
        List<WishListProduct> wishListProductList = wishListProductRepository.findAll();
        assertThat(wishListProductList).hasSize(databaseSizeBeforeCreate + 1);
        WishListProduct testWishListProduct = wishListProductList.get(wishListProductList.size() - 1);
        assertThat(testWishListProduct.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testWishListProduct.getProductId()).isEqualTo(DEFAULT_PRODUCT_ID);
        assertThat(testWishListProduct.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testWishListProduct.getCreatedTime()).isEqualTo(DEFAULT_CREATED_TIME);
        assertThat(testWishListProduct.getUpdatedTime()).isEqualTo(DEFAULT_UPDATED_TIME);
        assertThat(testWishListProduct.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testWishListProduct.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    void createWishListProductWithExistingId() throws Exception {
        // Create the WishListProduct with an existing ID
        wishListProduct.setId(1L);

        int databaseSizeBeforeCreate = wishListProductRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWishListProductMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wishListProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the WishListProduct in the database
        List<WishListProduct> wishListProductList = wishListProductRepository.findAll();
        assertThat(wishListProductList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllWishListProducts() throws Exception {
        // Initialize the database
        wishListProductRepository.saveAndFlush(wishListProduct);

        // Get all the wishListProductList
        restWishListProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wishListProduct.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.intValue())))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(sameInstant(DEFAULT_CREATED_TIME))))
            .andExpect(jsonPath("$.[*].updatedTime").value(hasItem(sameInstant(DEFAULT_UPDATED_TIME))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }

    @Test
    @Transactional
    void getWishListProduct() throws Exception {
        // Initialize the database
        wishListProductRepository.saveAndFlush(wishListProduct);

        // Get the wishListProduct
        restWishListProductMockMvc
            .perform(get(ENTITY_API_URL_ID, wishListProduct.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(wishListProduct.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY.intValue()))
            .andExpect(jsonPath("$.productId").value(DEFAULT_PRODUCT_ID.intValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()))
            .andExpect(jsonPath("$.createdTime").value(sameInstant(DEFAULT_CREATED_TIME)))
            .andExpect(jsonPath("$.updatedTime").value(sameInstant(DEFAULT_UPDATED_TIME)))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingWishListProduct() throws Exception {
        // Get the wishListProduct
        restWishListProductMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWishListProduct() throws Exception {
        // Initialize the database
        wishListProductRepository.saveAndFlush(wishListProduct);

        int databaseSizeBeforeUpdate = wishListProductRepository.findAll().size();

        // Update the wishListProduct
        WishListProduct updatedWishListProduct = wishListProductRepository.findById(wishListProduct.getId()).get();
        // Disconnect from session so that the updates on updatedWishListProduct are not directly saved in db
        em.detach(updatedWishListProduct);
        updatedWishListProduct
            .quantity(UPDATED_QUANTITY)
            .productId(UPDATED_PRODUCT_ID)
            .price(UPDATED_PRICE)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restWishListProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWishListProduct.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedWishListProduct))
            )
            .andExpect(status().isOk());

        // Validate the WishListProduct in the database
        List<WishListProduct> wishListProductList = wishListProductRepository.findAll();
        assertThat(wishListProductList).hasSize(databaseSizeBeforeUpdate);
        WishListProduct testWishListProduct = wishListProductList.get(wishListProductList.size() - 1);
        assertThat(testWishListProduct.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testWishListProduct.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testWishListProduct.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testWishListProduct.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testWishListProduct.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
        assertThat(testWishListProduct.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testWishListProduct.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void putNonExistingWishListProduct() throws Exception {
        int databaseSizeBeforeUpdate = wishListProductRepository.findAll().size();
        wishListProduct.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWishListProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, wishListProduct.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(wishListProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the WishListProduct in the database
        List<WishListProduct> wishListProductList = wishListProductRepository.findAll();
        assertThat(wishListProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWishListProduct() throws Exception {
        int databaseSizeBeforeUpdate = wishListProductRepository.findAll().size();
        wishListProduct.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWishListProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(wishListProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the WishListProduct in the database
        List<WishListProduct> wishListProductList = wishListProductRepository.findAll();
        assertThat(wishListProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWishListProduct() throws Exception {
        int databaseSizeBeforeUpdate = wishListProductRepository.findAll().size();
        wishListProduct.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWishListProductMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wishListProduct))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WishListProduct in the database
        List<WishListProduct> wishListProductList = wishListProductRepository.findAll();
        assertThat(wishListProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWishListProductWithPatch() throws Exception {
        // Initialize the database
        wishListProductRepository.saveAndFlush(wishListProduct);

        int databaseSizeBeforeUpdate = wishListProductRepository.findAll().size();

        // Update the wishListProduct using partial update
        WishListProduct partialUpdatedWishListProduct = new WishListProduct();
        partialUpdatedWishListProduct.setId(wishListProduct.getId());

        partialUpdatedWishListProduct
            .productId(UPDATED_PRODUCT_ID)
            .price(UPDATED_PRICE)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restWishListProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWishListProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWishListProduct))
            )
            .andExpect(status().isOk());

        // Validate the WishListProduct in the database
        List<WishListProduct> wishListProductList = wishListProductRepository.findAll();
        assertThat(wishListProductList).hasSize(databaseSizeBeforeUpdate);
        WishListProduct testWishListProduct = wishListProductList.get(wishListProductList.size() - 1);
        assertThat(testWishListProduct.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testWishListProduct.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testWishListProduct.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testWishListProduct.getCreatedTime()).isEqualTo(DEFAULT_CREATED_TIME);
        assertThat(testWishListProduct.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
        assertThat(testWishListProduct.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testWishListProduct.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void fullUpdateWishListProductWithPatch() throws Exception {
        // Initialize the database
        wishListProductRepository.saveAndFlush(wishListProduct);

        int databaseSizeBeforeUpdate = wishListProductRepository.findAll().size();

        // Update the wishListProduct using partial update
        WishListProduct partialUpdatedWishListProduct = new WishListProduct();
        partialUpdatedWishListProduct.setId(wishListProduct.getId());

        partialUpdatedWishListProduct
            .quantity(UPDATED_QUANTITY)
            .productId(UPDATED_PRODUCT_ID)
            .price(UPDATED_PRICE)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restWishListProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWishListProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWishListProduct))
            )
            .andExpect(status().isOk());

        // Validate the WishListProduct in the database
        List<WishListProduct> wishListProductList = wishListProductRepository.findAll();
        assertThat(wishListProductList).hasSize(databaseSizeBeforeUpdate);
        WishListProduct testWishListProduct = wishListProductList.get(wishListProductList.size() - 1);
        assertThat(testWishListProduct.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testWishListProduct.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testWishListProduct.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testWishListProduct.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testWishListProduct.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
        assertThat(testWishListProduct.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testWishListProduct.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingWishListProduct() throws Exception {
        int databaseSizeBeforeUpdate = wishListProductRepository.findAll().size();
        wishListProduct.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWishListProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, wishListProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(wishListProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the WishListProduct in the database
        List<WishListProduct> wishListProductList = wishListProductRepository.findAll();
        assertThat(wishListProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWishListProduct() throws Exception {
        int databaseSizeBeforeUpdate = wishListProductRepository.findAll().size();
        wishListProduct.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWishListProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(wishListProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the WishListProduct in the database
        List<WishListProduct> wishListProductList = wishListProductRepository.findAll();
        assertThat(wishListProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWishListProduct() throws Exception {
        int databaseSizeBeforeUpdate = wishListProductRepository.findAll().size();
        wishListProduct.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWishListProductMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(wishListProduct))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WishListProduct in the database
        List<WishListProduct> wishListProductList = wishListProductRepository.findAll();
        assertThat(wishListProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWishListProduct() throws Exception {
        // Initialize the database
        wishListProductRepository.saveAndFlush(wishListProduct);

        int databaseSizeBeforeDelete = wishListProductRepository.findAll().size();

        // Delete the wishListProduct
        restWishListProductMockMvc
            .perform(delete(ENTITY_API_URL_ID, wishListProduct.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WishListProduct> wishListProductList = wishListProductRepository.findAll();
        assertThat(wishListProductList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
