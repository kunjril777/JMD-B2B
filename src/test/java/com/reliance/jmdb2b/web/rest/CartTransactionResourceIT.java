package com.reliance.jmdb2b.web.rest;

import static com.reliance.jmdb2b.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.reliance.jmdb2b.IntegrationTest;
import com.reliance.jmdb2b.domain.CartTransaction;
import com.reliance.jmdb2b.repository.CartTransactionRepository;
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
 * Integration tests for the {@link CartTransactionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CartTransactionResourceIT {

    private static final Long DEFAULT_CART_TOTAL_QUANTITY = 1L;
    private static final Long UPDATED_CART_TOTAL_QUANTITY = 2L;

    private static final Long DEFAULT_CART_TOTAL_PRICE = 1L;
    private static final Long UPDATED_CART_TOTAL_PRICE = 2L;

    private static final Long DEFAULT_BILLING_ADDRESS_ID = 1L;
    private static final Long UPDATED_BILLING_ADDRESS_ID = 2L;

    private static final Long DEFAULT_SHIPPING_ADDRESS_ID = 1L;
    private static final Long UPDATED_SHIPPING_ADDRESS_ID = 2L;

    private static final Double DEFAULT_DELIVERY_CHARGE = 1D;
    private static final Double UPDATED_DELIVERY_CHARGE = 2D;

    private static final String DEFAULT_COUPON_CODE = "AAAAAAAAAA";
    private static final String UPDATED_COUPON_CODE = "BBBBBBBBBB";

    private static final Long DEFAULT_CART_FINAL_TOTAL = 1L;
    private static final Long UPDATED_CART_FINAL_TOTAL = 2L;

    private static final ZonedDateTime DEFAULT_CREATED_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cart-transactions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CartTransactionRepository cartTransactionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCartTransactionMockMvc;

    private CartTransaction cartTransaction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CartTransaction createEntity(EntityManager em) {
        CartTransaction cartTransaction = new CartTransaction()
            .cartTotalQuantity(DEFAULT_CART_TOTAL_QUANTITY)
            .cartTotalPrice(DEFAULT_CART_TOTAL_PRICE)
            .billingAddressId(DEFAULT_BILLING_ADDRESS_ID)
            .shippingAddressId(DEFAULT_SHIPPING_ADDRESS_ID)
            .deliveryCharge(DEFAULT_DELIVERY_CHARGE)
            .couponCode(DEFAULT_COUPON_CODE)
            .cartFinalTotal(DEFAULT_CART_FINAL_TOTAL)
            .createdTime(DEFAULT_CREATED_TIME)
            .updatedTime(DEFAULT_UPDATED_TIME)
            .createdBy(DEFAULT_CREATED_BY)
            .updatedBy(DEFAULT_UPDATED_BY);
        return cartTransaction;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CartTransaction createUpdatedEntity(EntityManager em) {
        CartTransaction cartTransaction = new CartTransaction()
            .cartTotalQuantity(UPDATED_CART_TOTAL_QUANTITY)
            .cartTotalPrice(UPDATED_CART_TOTAL_PRICE)
            .billingAddressId(UPDATED_BILLING_ADDRESS_ID)
            .shippingAddressId(UPDATED_SHIPPING_ADDRESS_ID)
            .deliveryCharge(UPDATED_DELIVERY_CHARGE)
            .couponCode(UPDATED_COUPON_CODE)
            .cartFinalTotal(UPDATED_CART_FINAL_TOTAL)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);
        return cartTransaction;
    }

    @BeforeEach
    public void initTest() {
        cartTransaction = createEntity(em);
    }

    @Test
    @Transactional
    void createCartTransaction() throws Exception {
        int databaseSizeBeforeCreate = cartTransactionRepository.findAll().size();
        // Create the CartTransaction
        restCartTransactionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cartTransaction))
            )
            .andExpect(status().isCreated());

        // Validate the CartTransaction in the database
        List<CartTransaction> cartTransactionList = cartTransactionRepository.findAll();
        assertThat(cartTransactionList).hasSize(databaseSizeBeforeCreate + 1);
        CartTransaction testCartTransaction = cartTransactionList.get(cartTransactionList.size() - 1);
        assertThat(testCartTransaction.getCartTotalQuantity()).isEqualTo(DEFAULT_CART_TOTAL_QUANTITY);
        assertThat(testCartTransaction.getCartTotalPrice()).isEqualTo(DEFAULT_CART_TOTAL_PRICE);
        assertThat(testCartTransaction.getBillingAddressId()).isEqualTo(DEFAULT_BILLING_ADDRESS_ID);
        assertThat(testCartTransaction.getShippingAddressId()).isEqualTo(DEFAULT_SHIPPING_ADDRESS_ID);
        assertThat(testCartTransaction.getDeliveryCharge()).isEqualTo(DEFAULT_DELIVERY_CHARGE);
        assertThat(testCartTransaction.getCouponCode()).isEqualTo(DEFAULT_COUPON_CODE);
        assertThat(testCartTransaction.getCartFinalTotal()).isEqualTo(DEFAULT_CART_FINAL_TOTAL);
        assertThat(testCartTransaction.getCreatedTime()).isEqualTo(DEFAULT_CREATED_TIME);
        assertThat(testCartTransaction.getUpdatedTime()).isEqualTo(DEFAULT_UPDATED_TIME);
        assertThat(testCartTransaction.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testCartTransaction.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    void createCartTransactionWithExistingId() throws Exception {
        // Create the CartTransaction with an existing ID
        cartTransaction.setId(1L);

        int databaseSizeBeforeCreate = cartTransactionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCartTransactionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cartTransaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the CartTransaction in the database
        List<CartTransaction> cartTransactionList = cartTransactionRepository.findAll();
        assertThat(cartTransactionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCartTransactions() throws Exception {
        // Initialize the database
        cartTransactionRepository.saveAndFlush(cartTransaction);

        // Get all the cartTransactionList
        restCartTransactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cartTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].cartTotalQuantity").value(hasItem(DEFAULT_CART_TOTAL_QUANTITY.intValue())))
            .andExpect(jsonPath("$.[*].cartTotalPrice").value(hasItem(DEFAULT_CART_TOTAL_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].billingAddressId").value(hasItem(DEFAULT_BILLING_ADDRESS_ID.intValue())))
            .andExpect(jsonPath("$.[*].shippingAddressId").value(hasItem(DEFAULT_SHIPPING_ADDRESS_ID.intValue())))
            .andExpect(jsonPath("$.[*].deliveryCharge").value(hasItem(DEFAULT_DELIVERY_CHARGE.doubleValue())))
            .andExpect(jsonPath("$.[*].couponCode").value(hasItem(DEFAULT_COUPON_CODE)))
            .andExpect(jsonPath("$.[*].cartFinalTotal").value(hasItem(DEFAULT_CART_FINAL_TOTAL.intValue())))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(sameInstant(DEFAULT_CREATED_TIME))))
            .andExpect(jsonPath("$.[*].updatedTime").value(hasItem(sameInstant(DEFAULT_UPDATED_TIME))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }

    @Test
    @Transactional
    void getCartTransaction() throws Exception {
        // Initialize the database
        cartTransactionRepository.saveAndFlush(cartTransaction);

        // Get the cartTransaction
        restCartTransactionMockMvc
            .perform(get(ENTITY_API_URL_ID, cartTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cartTransaction.getId().intValue()))
            .andExpect(jsonPath("$.cartTotalQuantity").value(DEFAULT_CART_TOTAL_QUANTITY.intValue()))
            .andExpect(jsonPath("$.cartTotalPrice").value(DEFAULT_CART_TOTAL_PRICE.intValue()))
            .andExpect(jsonPath("$.billingAddressId").value(DEFAULT_BILLING_ADDRESS_ID.intValue()))
            .andExpect(jsonPath("$.shippingAddressId").value(DEFAULT_SHIPPING_ADDRESS_ID.intValue()))
            .andExpect(jsonPath("$.deliveryCharge").value(DEFAULT_DELIVERY_CHARGE.doubleValue()))
            .andExpect(jsonPath("$.couponCode").value(DEFAULT_COUPON_CODE))
            .andExpect(jsonPath("$.cartFinalTotal").value(DEFAULT_CART_FINAL_TOTAL.intValue()))
            .andExpect(jsonPath("$.createdTime").value(sameInstant(DEFAULT_CREATED_TIME)))
            .andExpect(jsonPath("$.updatedTime").value(sameInstant(DEFAULT_UPDATED_TIME)))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingCartTransaction() throws Exception {
        // Get the cartTransaction
        restCartTransactionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCartTransaction() throws Exception {
        // Initialize the database
        cartTransactionRepository.saveAndFlush(cartTransaction);

        int databaseSizeBeforeUpdate = cartTransactionRepository.findAll().size();

        // Update the cartTransaction
        CartTransaction updatedCartTransaction = cartTransactionRepository.findById(cartTransaction.getId()).get();
        // Disconnect from session so that the updates on updatedCartTransaction are not directly saved in db
        em.detach(updatedCartTransaction);
        updatedCartTransaction
            .cartTotalQuantity(UPDATED_CART_TOTAL_QUANTITY)
            .cartTotalPrice(UPDATED_CART_TOTAL_PRICE)
            .billingAddressId(UPDATED_BILLING_ADDRESS_ID)
            .shippingAddressId(UPDATED_SHIPPING_ADDRESS_ID)
            .deliveryCharge(UPDATED_DELIVERY_CHARGE)
            .couponCode(UPDATED_COUPON_CODE)
            .cartFinalTotal(UPDATED_CART_FINAL_TOTAL)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restCartTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCartTransaction.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCartTransaction))
            )
            .andExpect(status().isOk());

        // Validate the CartTransaction in the database
        List<CartTransaction> cartTransactionList = cartTransactionRepository.findAll();
        assertThat(cartTransactionList).hasSize(databaseSizeBeforeUpdate);
        CartTransaction testCartTransaction = cartTransactionList.get(cartTransactionList.size() - 1);
        assertThat(testCartTransaction.getCartTotalQuantity()).isEqualTo(UPDATED_CART_TOTAL_QUANTITY);
        assertThat(testCartTransaction.getCartTotalPrice()).isEqualTo(UPDATED_CART_TOTAL_PRICE);
        assertThat(testCartTransaction.getBillingAddressId()).isEqualTo(UPDATED_BILLING_ADDRESS_ID);
        assertThat(testCartTransaction.getShippingAddressId()).isEqualTo(UPDATED_SHIPPING_ADDRESS_ID);
        assertThat(testCartTransaction.getDeliveryCharge()).isEqualTo(UPDATED_DELIVERY_CHARGE);
        assertThat(testCartTransaction.getCouponCode()).isEqualTo(UPDATED_COUPON_CODE);
        assertThat(testCartTransaction.getCartFinalTotal()).isEqualTo(UPDATED_CART_FINAL_TOTAL);
        assertThat(testCartTransaction.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testCartTransaction.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
        assertThat(testCartTransaction.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testCartTransaction.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void putNonExistingCartTransaction() throws Exception {
        int databaseSizeBeforeUpdate = cartTransactionRepository.findAll().size();
        cartTransaction.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCartTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cartTransaction.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cartTransaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the CartTransaction in the database
        List<CartTransaction> cartTransactionList = cartTransactionRepository.findAll();
        assertThat(cartTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCartTransaction() throws Exception {
        int databaseSizeBeforeUpdate = cartTransactionRepository.findAll().size();
        cartTransaction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCartTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cartTransaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the CartTransaction in the database
        List<CartTransaction> cartTransactionList = cartTransactionRepository.findAll();
        assertThat(cartTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCartTransaction() throws Exception {
        int databaseSizeBeforeUpdate = cartTransactionRepository.findAll().size();
        cartTransaction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCartTransactionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cartTransaction))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CartTransaction in the database
        List<CartTransaction> cartTransactionList = cartTransactionRepository.findAll();
        assertThat(cartTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCartTransactionWithPatch() throws Exception {
        // Initialize the database
        cartTransactionRepository.saveAndFlush(cartTransaction);

        int databaseSizeBeforeUpdate = cartTransactionRepository.findAll().size();

        // Update the cartTransaction using partial update
        CartTransaction partialUpdatedCartTransaction = new CartTransaction();
        partialUpdatedCartTransaction.setId(cartTransaction.getId());

        partialUpdatedCartTransaction
            .cartTotalPrice(UPDATED_CART_TOTAL_PRICE)
            .shippingAddressId(UPDATED_SHIPPING_ADDRESS_ID)
            .deliveryCharge(UPDATED_DELIVERY_CHARGE)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedBy(UPDATED_UPDATED_BY);

        restCartTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCartTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCartTransaction))
            )
            .andExpect(status().isOk());

        // Validate the CartTransaction in the database
        List<CartTransaction> cartTransactionList = cartTransactionRepository.findAll();
        assertThat(cartTransactionList).hasSize(databaseSizeBeforeUpdate);
        CartTransaction testCartTransaction = cartTransactionList.get(cartTransactionList.size() - 1);
        assertThat(testCartTransaction.getCartTotalQuantity()).isEqualTo(DEFAULT_CART_TOTAL_QUANTITY);
        assertThat(testCartTransaction.getCartTotalPrice()).isEqualTo(UPDATED_CART_TOTAL_PRICE);
        assertThat(testCartTransaction.getBillingAddressId()).isEqualTo(DEFAULT_BILLING_ADDRESS_ID);
        assertThat(testCartTransaction.getShippingAddressId()).isEqualTo(UPDATED_SHIPPING_ADDRESS_ID);
        assertThat(testCartTransaction.getDeliveryCharge()).isEqualTo(UPDATED_DELIVERY_CHARGE);
        assertThat(testCartTransaction.getCouponCode()).isEqualTo(DEFAULT_COUPON_CODE);
        assertThat(testCartTransaction.getCartFinalTotal()).isEqualTo(DEFAULT_CART_FINAL_TOTAL);
        assertThat(testCartTransaction.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testCartTransaction.getUpdatedTime()).isEqualTo(DEFAULT_UPDATED_TIME);
        assertThat(testCartTransaction.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testCartTransaction.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void fullUpdateCartTransactionWithPatch() throws Exception {
        // Initialize the database
        cartTransactionRepository.saveAndFlush(cartTransaction);

        int databaseSizeBeforeUpdate = cartTransactionRepository.findAll().size();

        // Update the cartTransaction using partial update
        CartTransaction partialUpdatedCartTransaction = new CartTransaction();
        partialUpdatedCartTransaction.setId(cartTransaction.getId());

        partialUpdatedCartTransaction
            .cartTotalQuantity(UPDATED_CART_TOTAL_QUANTITY)
            .cartTotalPrice(UPDATED_CART_TOTAL_PRICE)
            .billingAddressId(UPDATED_BILLING_ADDRESS_ID)
            .shippingAddressId(UPDATED_SHIPPING_ADDRESS_ID)
            .deliveryCharge(UPDATED_DELIVERY_CHARGE)
            .couponCode(UPDATED_COUPON_CODE)
            .cartFinalTotal(UPDATED_CART_FINAL_TOTAL)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restCartTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCartTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCartTransaction))
            )
            .andExpect(status().isOk());

        // Validate the CartTransaction in the database
        List<CartTransaction> cartTransactionList = cartTransactionRepository.findAll();
        assertThat(cartTransactionList).hasSize(databaseSizeBeforeUpdate);
        CartTransaction testCartTransaction = cartTransactionList.get(cartTransactionList.size() - 1);
        assertThat(testCartTransaction.getCartTotalQuantity()).isEqualTo(UPDATED_CART_TOTAL_QUANTITY);
        assertThat(testCartTransaction.getCartTotalPrice()).isEqualTo(UPDATED_CART_TOTAL_PRICE);
        assertThat(testCartTransaction.getBillingAddressId()).isEqualTo(UPDATED_BILLING_ADDRESS_ID);
        assertThat(testCartTransaction.getShippingAddressId()).isEqualTo(UPDATED_SHIPPING_ADDRESS_ID);
        assertThat(testCartTransaction.getDeliveryCharge()).isEqualTo(UPDATED_DELIVERY_CHARGE);
        assertThat(testCartTransaction.getCouponCode()).isEqualTo(UPDATED_COUPON_CODE);
        assertThat(testCartTransaction.getCartFinalTotal()).isEqualTo(UPDATED_CART_FINAL_TOTAL);
        assertThat(testCartTransaction.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testCartTransaction.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
        assertThat(testCartTransaction.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testCartTransaction.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingCartTransaction() throws Exception {
        int databaseSizeBeforeUpdate = cartTransactionRepository.findAll().size();
        cartTransaction.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCartTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cartTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cartTransaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the CartTransaction in the database
        List<CartTransaction> cartTransactionList = cartTransactionRepository.findAll();
        assertThat(cartTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCartTransaction() throws Exception {
        int databaseSizeBeforeUpdate = cartTransactionRepository.findAll().size();
        cartTransaction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCartTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cartTransaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the CartTransaction in the database
        List<CartTransaction> cartTransactionList = cartTransactionRepository.findAll();
        assertThat(cartTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCartTransaction() throws Exception {
        int databaseSizeBeforeUpdate = cartTransactionRepository.findAll().size();
        cartTransaction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCartTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cartTransaction))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CartTransaction in the database
        List<CartTransaction> cartTransactionList = cartTransactionRepository.findAll();
        assertThat(cartTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCartTransaction() throws Exception {
        // Initialize the database
        cartTransactionRepository.saveAndFlush(cartTransaction);

        int databaseSizeBeforeDelete = cartTransactionRepository.findAll().size();

        // Delete the cartTransaction
        restCartTransactionMockMvc
            .perform(delete(ENTITY_API_URL_ID, cartTransaction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CartTransaction> cartTransactionList = cartTransactionRepository.findAll();
        assertThat(cartTransactionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
