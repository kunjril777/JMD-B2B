package com.reliance.jmdb2b.web.rest;

import static com.reliance.jmdb2b.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.reliance.jmdb2b.IntegrationTest;
import com.reliance.jmdb2b.domain.CartProduct;
import com.reliance.jmdb2b.repository.CartProductRepository;
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
 * Integration tests for the {@link CartProductResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CartProductResourceIT {

    private static final Long DEFAULT_QUANTITY = 1L;
    private static final Long UPDATED_QUANTITY = 2L;

    private static final Long DEFAULT_PRODUCT_VARIANT_ID = 1L;
    private static final Long UPDATED_PRODUCT_VARIANT_ID = 2L;

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

    private static final String ENTITY_API_URL = "/api/cart-products";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CartProductRepository cartProductRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCartProductMockMvc;

    private CartProduct cartProduct;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CartProduct createEntity(EntityManager em) {
        CartProduct cartProduct = new CartProduct()
            .quantity(DEFAULT_QUANTITY)
            .productVariantId(DEFAULT_PRODUCT_VARIANT_ID)
            .price(DEFAULT_PRICE)
            .createdTime(DEFAULT_CREATED_TIME)
            .updatedTime(DEFAULT_UPDATED_TIME)
            .createdBy(DEFAULT_CREATED_BY)
            .updatedBy(DEFAULT_UPDATED_BY);
        return cartProduct;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CartProduct createUpdatedEntity(EntityManager em) {
        CartProduct cartProduct = new CartProduct()
            .quantity(UPDATED_QUANTITY)
            .productVariantId(UPDATED_PRODUCT_VARIANT_ID)
            .price(UPDATED_PRICE)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);
        return cartProduct;
    }

    @BeforeEach
    public void initTest() {
        cartProduct = createEntity(em);
    }

    @Test
    @Transactional
    void createCartProduct() throws Exception {
        int databaseSizeBeforeCreate = cartProductRepository.findAll().size();
        // Create the CartProduct
        restCartProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cartProduct)))
            .andExpect(status().isCreated());

        // Validate the CartProduct in the database
        List<CartProduct> cartProductList = cartProductRepository.findAll();
        assertThat(cartProductList).hasSize(databaseSizeBeforeCreate + 1);
        CartProduct testCartProduct = cartProductList.get(cartProductList.size() - 1);
        assertThat(testCartProduct.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testCartProduct.getProductVariantId()).isEqualTo(DEFAULT_PRODUCT_VARIANT_ID);
        assertThat(testCartProduct.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testCartProduct.getCreatedTime()).isEqualTo(DEFAULT_CREATED_TIME);
        assertThat(testCartProduct.getUpdatedTime()).isEqualTo(DEFAULT_UPDATED_TIME);
        assertThat(testCartProduct.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testCartProduct.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    void createCartProductWithExistingId() throws Exception {
        // Create the CartProduct with an existing ID
        cartProduct.setId(1L);

        int databaseSizeBeforeCreate = cartProductRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCartProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cartProduct)))
            .andExpect(status().isBadRequest());

        // Validate the CartProduct in the database
        List<CartProduct> cartProductList = cartProductRepository.findAll();
        assertThat(cartProductList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCartProducts() throws Exception {
        // Initialize the database
        cartProductRepository.saveAndFlush(cartProduct);

        // Get all the cartProductList
        restCartProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cartProduct.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.intValue())))
            .andExpect(jsonPath("$.[*].productVariantId").value(hasItem(DEFAULT_PRODUCT_VARIANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(sameInstant(DEFAULT_CREATED_TIME))))
            .andExpect(jsonPath("$.[*].updatedTime").value(hasItem(sameInstant(DEFAULT_UPDATED_TIME))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }

    @Test
    @Transactional
    void getCartProduct() throws Exception {
        // Initialize the database
        cartProductRepository.saveAndFlush(cartProduct);

        // Get the cartProduct
        restCartProductMockMvc
            .perform(get(ENTITY_API_URL_ID, cartProduct.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cartProduct.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY.intValue()))
            .andExpect(jsonPath("$.productVariantId").value(DEFAULT_PRODUCT_VARIANT_ID.intValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()))
            .andExpect(jsonPath("$.createdTime").value(sameInstant(DEFAULT_CREATED_TIME)))
            .andExpect(jsonPath("$.updatedTime").value(sameInstant(DEFAULT_UPDATED_TIME)))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingCartProduct() throws Exception {
        // Get the cartProduct
        restCartProductMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCartProduct() throws Exception {
        // Initialize the database
        cartProductRepository.saveAndFlush(cartProduct);

        int databaseSizeBeforeUpdate = cartProductRepository.findAll().size();

        // Update the cartProduct
        CartProduct updatedCartProduct = cartProductRepository.findById(cartProduct.getId()).get();
        // Disconnect from session so that the updates on updatedCartProduct are not directly saved in db
        em.detach(updatedCartProduct);
        updatedCartProduct
            .quantity(UPDATED_QUANTITY)
            .productVariantId(UPDATED_PRODUCT_VARIANT_ID)
            .price(UPDATED_PRICE)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restCartProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCartProduct.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCartProduct))
            )
            .andExpect(status().isOk());

        // Validate the CartProduct in the database
        List<CartProduct> cartProductList = cartProductRepository.findAll();
        assertThat(cartProductList).hasSize(databaseSizeBeforeUpdate);
        CartProduct testCartProduct = cartProductList.get(cartProductList.size() - 1);
        assertThat(testCartProduct.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testCartProduct.getProductVariantId()).isEqualTo(UPDATED_PRODUCT_VARIANT_ID);
        assertThat(testCartProduct.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testCartProduct.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testCartProduct.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
        assertThat(testCartProduct.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testCartProduct.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void putNonExistingCartProduct() throws Exception {
        int databaseSizeBeforeUpdate = cartProductRepository.findAll().size();
        cartProduct.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCartProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cartProduct.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cartProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the CartProduct in the database
        List<CartProduct> cartProductList = cartProductRepository.findAll();
        assertThat(cartProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCartProduct() throws Exception {
        int databaseSizeBeforeUpdate = cartProductRepository.findAll().size();
        cartProduct.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCartProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cartProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the CartProduct in the database
        List<CartProduct> cartProductList = cartProductRepository.findAll();
        assertThat(cartProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCartProduct() throws Exception {
        int databaseSizeBeforeUpdate = cartProductRepository.findAll().size();
        cartProduct.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCartProductMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cartProduct)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CartProduct in the database
        List<CartProduct> cartProductList = cartProductRepository.findAll();
        assertThat(cartProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCartProductWithPatch() throws Exception {
        // Initialize the database
        cartProductRepository.saveAndFlush(cartProduct);

        int databaseSizeBeforeUpdate = cartProductRepository.findAll().size();

        // Update the cartProduct using partial update
        CartProduct partialUpdatedCartProduct = new CartProduct();
        partialUpdatedCartProduct.setId(cartProduct.getId());

        partialUpdatedCartProduct
            .quantity(UPDATED_QUANTITY)
            .productVariantId(UPDATED_PRODUCT_VARIANT_ID)
            .price(UPDATED_PRICE)
            .createdBy(UPDATED_CREATED_BY);

        restCartProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCartProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCartProduct))
            )
            .andExpect(status().isOk());

        // Validate the CartProduct in the database
        List<CartProduct> cartProductList = cartProductRepository.findAll();
        assertThat(cartProductList).hasSize(databaseSizeBeforeUpdate);
        CartProduct testCartProduct = cartProductList.get(cartProductList.size() - 1);
        assertThat(testCartProduct.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testCartProduct.getProductVariantId()).isEqualTo(UPDATED_PRODUCT_VARIANT_ID);
        assertThat(testCartProduct.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testCartProduct.getCreatedTime()).isEqualTo(DEFAULT_CREATED_TIME);
        assertThat(testCartProduct.getUpdatedTime()).isEqualTo(DEFAULT_UPDATED_TIME);
        assertThat(testCartProduct.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testCartProduct.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    void fullUpdateCartProductWithPatch() throws Exception {
        // Initialize the database
        cartProductRepository.saveAndFlush(cartProduct);

        int databaseSizeBeforeUpdate = cartProductRepository.findAll().size();

        // Update the cartProduct using partial update
        CartProduct partialUpdatedCartProduct = new CartProduct();
        partialUpdatedCartProduct.setId(cartProduct.getId());

        partialUpdatedCartProduct
            .quantity(UPDATED_QUANTITY)
            .productVariantId(UPDATED_PRODUCT_VARIANT_ID)
            .price(UPDATED_PRICE)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restCartProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCartProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCartProduct))
            )
            .andExpect(status().isOk());

        // Validate the CartProduct in the database
        List<CartProduct> cartProductList = cartProductRepository.findAll();
        assertThat(cartProductList).hasSize(databaseSizeBeforeUpdate);
        CartProduct testCartProduct = cartProductList.get(cartProductList.size() - 1);
        assertThat(testCartProduct.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testCartProduct.getProductVariantId()).isEqualTo(UPDATED_PRODUCT_VARIANT_ID);
        assertThat(testCartProduct.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testCartProduct.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testCartProduct.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
        assertThat(testCartProduct.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testCartProduct.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingCartProduct() throws Exception {
        int databaseSizeBeforeUpdate = cartProductRepository.findAll().size();
        cartProduct.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCartProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cartProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cartProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the CartProduct in the database
        List<CartProduct> cartProductList = cartProductRepository.findAll();
        assertThat(cartProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCartProduct() throws Exception {
        int databaseSizeBeforeUpdate = cartProductRepository.findAll().size();
        cartProduct.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCartProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cartProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the CartProduct in the database
        List<CartProduct> cartProductList = cartProductRepository.findAll();
        assertThat(cartProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCartProduct() throws Exception {
        int databaseSizeBeforeUpdate = cartProductRepository.findAll().size();
        cartProduct.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCartProductMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cartProduct))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CartProduct in the database
        List<CartProduct> cartProductList = cartProductRepository.findAll();
        assertThat(cartProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCartProduct() throws Exception {
        // Initialize the database
        cartProductRepository.saveAndFlush(cartProduct);

        int databaseSizeBeforeDelete = cartProductRepository.findAll().size();

        // Delete the cartProduct
        restCartProductMockMvc
            .perform(delete(ENTITY_API_URL_ID, cartProduct.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CartProduct> cartProductList = cartProductRepository.findAll();
        assertThat(cartProductList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
