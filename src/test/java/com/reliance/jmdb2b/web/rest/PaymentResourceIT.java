package com.reliance.jmdb2b.web.rest;

import static com.reliance.jmdb2b.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.reliance.jmdb2b.IntegrationTest;
import com.reliance.jmdb2b.domain.Payment;
import com.reliance.jmdb2b.domain.enumeration.CLIENTTRANSACTIONTYPE;
import com.reliance.jmdb2b.domain.enumeration.PAYMENTAGGREGATOR;
import com.reliance.jmdb2b.domain.enumeration.PAYMENTGATEWAY;
import com.reliance.jmdb2b.domain.enumeration.PAYMENTMETHOD;
import com.reliance.jmdb2b.domain.enumeration.PAYMENTSTATUS;
import com.reliance.jmdb2b.repository.PaymentRepository;
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
 * Integration tests for the {@link PaymentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaymentResourceIT {

    private static final String DEFAULT_PG_TRANSACTION_ID = "AAAAAAAAAA";
    private static final String UPDATED_PG_TRANSACTION_ID = "BBBBBBBBBB";

    private static final CLIENTTRANSACTIONTYPE DEFAULT_CLIENT_TRANSACTION_TYPE = CLIENTTRANSACTIONTYPE.ORDER_PAYMNET;
    private static final CLIENTTRANSACTIONTYPE UPDATED_CLIENT_TRANSACTION_TYPE = CLIENTTRANSACTIONTYPE.LEDGER_CREDIT;

    private static final Long DEFAULT_CLIENT_TRANSACTION_ID = 1L;
    private static final Long UPDATED_CLIENT_TRANSACTION_ID = 2L;

    private static final String DEFAULT_PG_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_PG_STATUS = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_TRANSACTIONINIT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TRANSACTIONINIT_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_STATUS_UPDATE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_STATUS_UPDATE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Float DEFAULT_AMOUNT = 1F;
    private static final Float UPDATED_AMOUNT = 2F;

    private static final String DEFAULT_REDIRECTION_URL = "AAAAAAAAAA";
    private static final String UPDATED_REDIRECTION_URL = "BBBBBBBBBB";

    private static final PAYMENTAGGREGATOR DEFAULT_PAYMENT_AGGREGATOR = PAYMENTAGGREGATOR.JIOPG;
    private static final PAYMENTAGGREGATOR UPDATED_PAYMENT_AGGREGATOR = PAYMENTAGGREGATOR.JIOCREDITS;

    private static final PAYMENTGATEWAY DEFAULT_PAYMENTGATEWAY = PAYMENTGATEWAY.RAZORPAY;
    private static final PAYMENTGATEWAY UPDATED_PAYMENTGATEWAY = PAYMENTGATEWAY.JUSPAY;

    private static final PAYMENTSTATUS DEFAULT_PAYMENT_STATUS = PAYMENTSTATUS.SUCCESSFUL;
    private static final PAYMENTSTATUS UPDATED_PAYMENT_STATUS = PAYMENTSTATUS.PENDING;

    private static final PAYMENTMETHOD DEFAULT_PAYMENT_METHOD = PAYMENTMETHOD.CREDITCARD;
    private static final PAYMENTMETHOD UPDATED_PAYMENT_METHOD = PAYMENTMETHOD.DEBITCARD;

    private static final ZonedDateTime DEFAULT_CREATED_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/payments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentMockMvc;

    private Payment payment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Payment createEntity(EntityManager em) {
        Payment payment = new Payment()
            .pgTransactionId(DEFAULT_PG_TRANSACTION_ID)
            .clientTransactionType(DEFAULT_CLIENT_TRANSACTION_TYPE)
            .clientTransactionId(DEFAULT_CLIENT_TRANSACTION_ID)
            .pgStatus(DEFAULT_PG_STATUS)
            .transactioninitDate(DEFAULT_TRANSACTIONINIT_DATE)
            .statusUpdateDate(DEFAULT_STATUS_UPDATE_DATE)
            .amount(DEFAULT_AMOUNT)
            .redirectionUrl(DEFAULT_REDIRECTION_URL)
            .paymentAggregator(DEFAULT_PAYMENT_AGGREGATOR)
            .paymentgateway(DEFAULT_PAYMENTGATEWAY)
            .paymentStatus(DEFAULT_PAYMENT_STATUS)
            .paymentMethod(DEFAULT_PAYMENT_METHOD)
            .createdTime(DEFAULT_CREATED_TIME)
            .updatedTime(DEFAULT_UPDATED_TIME)
            .createdBy(DEFAULT_CREATED_BY)
            .updatedBy(DEFAULT_UPDATED_BY);
        return payment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Payment createUpdatedEntity(EntityManager em) {
        Payment payment = new Payment()
            .pgTransactionId(UPDATED_PG_TRANSACTION_ID)
            .clientTransactionType(UPDATED_CLIENT_TRANSACTION_TYPE)
            .clientTransactionId(UPDATED_CLIENT_TRANSACTION_ID)
            .pgStatus(UPDATED_PG_STATUS)
            .transactioninitDate(UPDATED_TRANSACTIONINIT_DATE)
            .statusUpdateDate(UPDATED_STATUS_UPDATE_DATE)
            .amount(UPDATED_AMOUNT)
            .redirectionUrl(UPDATED_REDIRECTION_URL)
            .paymentAggregator(UPDATED_PAYMENT_AGGREGATOR)
            .paymentgateway(UPDATED_PAYMENTGATEWAY)
            .paymentStatus(UPDATED_PAYMENT_STATUS)
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);
        return payment;
    }

    @BeforeEach
    public void initTest() {
        payment = createEntity(em);
    }

    @Test
    @Transactional
    void createPayment() throws Exception {
        int databaseSizeBeforeCreate = paymentRepository.findAll().size();
        // Create the Payment
        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payment)))
            .andExpect(status().isCreated());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeCreate + 1);
        Payment testPayment = paymentList.get(paymentList.size() - 1);
        assertThat(testPayment.getPgTransactionId()).isEqualTo(DEFAULT_PG_TRANSACTION_ID);
        assertThat(testPayment.getClientTransactionType()).isEqualTo(DEFAULT_CLIENT_TRANSACTION_TYPE);
        assertThat(testPayment.getClientTransactionId()).isEqualTo(DEFAULT_CLIENT_TRANSACTION_ID);
        assertThat(testPayment.getPgStatus()).isEqualTo(DEFAULT_PG_STATUS);
        assertThat(testPayment.getTransactioninitDate()).isEqualTo(DEFAULT_TRANSACTIONINIT_DATE);
        assertThat(testPayment.getStatusUpdateDate()).isEqualTo(DEFAULT_STATUS_UPDATE_DATE);
        assertThat(testPayment.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testPayment.getRedirectionUrl()).isEqualTo(DEFAULT_REDIRECTION_URL);
        assertThat(testPayment.getPaymentAggregator()).isEqualTo(DEFAULT_PAYMENT_AGGREGATOR);
        assertThat(testPayment.getPaymentgateway()).isEqualTo(DEFAULT_PAYMENTGATEWAY);
        assertThat(testPayment.getPaymentStatus()).isEqualTo(DEFAULT_PAYMENT_STATUS);
        assertThat(testPayment.getPaymentMethod()).isEqualTo(DEFAULT_PAYMENT_METHOD);
        assertThat(testPayment.getCreatedTime()).isEqualTo(DEFAULT_CREATED_TIME);
        assertThat(testPayment.getUpdatedTime()).isEqualTo(DEFAULT_UPDATED_TIME);
        assertThat(testPayment.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testPayment.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    void createPaymentWithExistingId() throws Exception {
        // Create the Payment with an existing ID
        payment.setId(1L);

        int databaseSizeBeforeCreate = paymentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payment)))
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPayments() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payment.getId().intValue())))
            .andExpect(jsonPath("$.[*].pgTransactionId").value(hasItem(DEFAULT_PG_TRANSACTION_ID)))
            .andExpect(jsonPath("$.[*].clientTransactionType").value(hasItem(DEFAULT_CLIENT_TRANSACTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].clientTransactionId").value(hasItem(DEFAULT_CLIENT_TRANSACTION_ID.intValue())))
            .andExpect(jsonPath("$.[*].pgStatus").value(hasItem(DEFAULT_PG_STATUS)))
            .andExpect(jsonPath("$.[*].transactioninitDate").value(hasItem(sameInstant(DEFAULT_TRANSACTIONINIT_DATE))))
            .andExpect(jsonPath("$.[*].statusUpdateDate").value(hasItem(sameInstant(DEFAULT_STATUS_UPDATE_DATE))))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].redirectionUrl").value(hasItem(DEFAULT_REDIRECTION_URL)))
            .andExpect(jsonPath("$.[*].paymentAggregator").value(hasItem(DEFAULT_PAYMENT_AGGREGATOR.toString())))
            .andExpect(jsonPath("$.[*].paymentgateway").value(hasItem(DEFAULT_PAYMENTGATEWAY.toString())))
            .andExpect(jsonPath("$.[*].paymentStatus").value(hasItem(DEFAULT_PAYMENT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(sameInstant(DEFAULT_CREATED_TIME))))
            .andExpect(jsonPath("$.[*].updatedTime").value(hasItem(sameInstant(DEFAULT_UPDATED_TIME))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }

    @Test
    @Transactional
    void getPayment() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get the payment
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL_ID, payment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(payment.getId().intValue()))
            .andExpect(jsonPath("$.pgTransactionId").value(DEFAULT_PG_TRANSACTION_ID))
            .andExpect(jsonPath("$.clientTransactionType").value(DEFAULT_CLIENT_TRANSACTION_TYPE.toString()))
            .andExpect(jsonPath("$.clientTransactionId").value(DEFAULT_CLIENT_TRANSACTION_ID.intValue()))
            .andExpect(jsonPath("$.pgStatus").value(DEFAULT_PG_STATUS))
            .andExpect(jsonPath("$.transactioninitDate").value(sameInstant(DEFAULT_TRANSACTIONINIT_DATE)))
            .andExpect(jsonPath("$.statusUpdateDate").value(sameInstant(DEFAULT_STATUS_UPDATE_DATE)))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.redirectionUrl").value(DEFAULT_REDIRECTION_URL))
            .andExpect(jsonPath("$.paymentAggregator").value(DEFAULT_PAYMENT_AGGREGATOR.toString()))
            .andExpect(jsonPath("$.paymentgateway").value(DEFAULT_PAYMENTGATEWAY.toString()))
            .andExpect(jsonPath("$.paymentStatus").value(DEFAULT_PAYMENT_STATUS.toString()))
            .andExpect(jsonPath("$.paymentMethod").value(DEFAULT_PAYMENT_METHOD.toString()))
            .andExpect(jsonPath("$.createdTime").value(sameInstant(DEFAULT_CREATED_TIME)))
            .andExpect(jsonPath("$.updatedTime").value(sameInstant(DEFAULT_UPDATED_TIME)))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingPayment() throws Exception {
        // Get the payment
        restPaymentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPayment() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();

        // Update the payment
        Payment updatedPayment = paymentRepository.findById(payment.getId()).get();
        // Disconnect from session so that the updates on updatedPayment are not directly saved in db
        em.detach(updatedPayment);
        updatedPayment
            .pgTransactionId(UPDATED_PG_TRANSACTION_ID)
            .clientTransactionType(UPDATED_CLIENT_TRANSACTION_TYPE)
            .clientTransactionId(UPDATED_CLIENT_TRANSACTION_ID)
            .pgStatus(UPDATED_PG_STATUS)
            .transactioninitDate(UPDATED_TRANSACTIONINIT_DATE)
            .statusUpdateDate(UPDATED_STATUS_UPDATE_DATE)
            .amount(UPDATED_AMOUNT)
            .redirectionUrl(UPDATED_REDIRECTION_URL)
            .paymentAggregator(UPDATED_PAYMENT_AGGREGATOR)
            .paymentgateway(UPDATED_PAYMENTGATEWAY)
            .paymentStatus(UPDATED_PAYMENT_STATUS)
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPayment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPayment))
            )
            .andExpect(status().isOk());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
        Payment testPayment = paymentList.get(paymentList.size() - 1);
        assertThat(testPayment.getPgTransactionId()).isEqualTo(UPDATED_PG_TRANSACTION_ID);
        assertThat(testPayment.getClientTransactionType()).isEqualTo(UPDATED_CLIENT_TRANSACTION_TYPE);
        assertThat(testPayment.getClientTransactionId()).isEqualTo(UPDATED_CLIENT_TRANSACTION_ID);
        assertThat(testPayment.getPgStatus()).isEqualTo(UPDATED_PG_STATUS);
        assertThat(testPayment.getTransactioninitDate()).isEqualTo(UPDATED_TRANSACTIONINIT_DATE);
        assertThat(testPayment.getStatusUpdateDate()).isEqualTo(UPDATED_STATUS_UPDATE_DATE);
        assertThat(testPayment.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testPayment.getRedirectionUrl()).isEqualTo(UPDATED_REDIRECTION_URL);
        assertThat(testPayment.getPaymentAggregator()).isEqualTo(UPDATED_PAYMENT_AGGREGATOR);
        assertThat(testPayment.getPaymentgateway()).isEqualTo(UPDATED_PAYMENTGATEWAY);
        assertThat(testPayment.getPaymentStatus()).isEqualTo(UPDATED_PAYMENT_STATUS);
        assertThat(testPayment.getPaymentMethod()).isEqualTo(UPDATED_PAYMENT_METHOD);
        assertThat(testPayment.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testPayment.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
        assertThat(testPayment.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPayment.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void putNonExistingPayment() throws Exception {
        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();
        payment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, payment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(payment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPayment() throws Exception {
        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();
        payment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(payment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPayment() throws Exception {
        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();
        payment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaymentWithPatch() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();

        // Update the payment using partial update
        Payment partialUpdatedPayment = new Payment();
        partialUpdatedPayment.setId(payment.getId());

        partialUpdatedPayment
            .clientTransactionType(UPDATED_CLIENT_TRANSACTION_TYPE)
            .transactioninitDate(UPDATED_TRANSACTIONINIT_DATE)
            .amount(UPDATED_AMOUNT)
            .paymentgateway(UPDATED_PAYMENTGATEWAY)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedBy(UPDATED_UPDATED_BY);

        restPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPayment))
            )
            .andExpect(status().isOk());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
        Payment testPayment = paymentList.get(paymentList.size() - 1);
        assertThat(testPayment.getPgTransactionId()).isEqualTo(DEFAULT_PG_TRANSACTION_ID);
        assertThat(testPayment.getClientTransactionType()).isEqualTo(UPDATED_CLIENT_TRANSACTION_TYPE);
        assertThat(testPayment.getClientTransactionId()).isEqualTo(DEFAULT_CLIENT_TRANSACTION_ID);
        assertThat(testPayment.getPgStatus()).isEqualTo(DEFAULT_PG_STATUS);
        assertThat(testPayment.getTransactioninitDate()).isEqualTo(UPDATED_TRANSACTIONINIT_DATE);
        assertThat(testPayment.getStatusUpdateDate()).isEqualTo(DEFAULT_STATUS_UPDATE_DATE);
        assertThat(testPayment.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testPayment.getRedirectionUrl()).isEqualTo(DEFAULT_REDIRECTION_URL);
        assertThat(testPayment.getPaymentAggregator()).isEqualTo(DEFAULT_PAYMENT_AGGREGATOR);
        assertThat(testPayment.getPaymentgateway()).isEqualTo(UPDATED_PAYMENTGATEWAY);
        assertThat(testPayment.getPaymentStatus()).isEqualTo(DEFAULT_PAYMENT_STATUS);
        assertThat(testPayment.getPaymentMethod()).isEqualTo(DEFAULT_PAYMENT_METHOD);
        assertThat(testPayment.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testPayment.getUpdatedTime()).isEqualTo(DEFAULT_UPDATED_TIME);
        assertThat(testPayment.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testPayment.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void fullUpdatePaymentWithPatch() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();

        // Update the payment using partial update
        Payment partialUpdatedPayment = new Payment();
        partialUpdatedPayment.setId(payment.getId());

        partialUpdatedPayment
            .pgTransactionId(UPDATED_PG_TRANSACTION_ID)
            .clientTransactionType(UPDATED_CLIENT_TRANSACTION_TYPE)
            .clientTransactionId(UPDATED_CLIENT_TRANSACTION_ID)
            .pgStatus(UPDATED_PG_STATUS)
            .transactioninitDate(UPDATED_TRANSACTIONINIT_DATE)
            .statusUpdateDate(UPDATED_STATUS_UPDATE_DATE)
            .amount(UPDATED_AMOUNT)
            .redirectionUrl(UPDATED_REDIRECTION_URL)
            .paymentAggregator(UPDATED_PAYMENT_AGGREGATOR)
            .paymentgateway(UPDATED_PAYMENTGATEWAY)
            .paymentStatus(UPDATED_PAYMENT_STATUS)
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPayment))
            )
            .andExpect(status().isOk());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
        Payment testPayment = paymentList.get(paymentList.size() - 1);
        assertThat(testPayment.getPgTransactionId()).isEqualTo(UPDATED_PG_TRANSACTION_ID);
        assertThat(testPayment.getClientTransactionType()).isEqualTo(UPDATED_CLIENT_TRANSACTION_TYPE);
        assertThat(testPayment.getClientTransactionId()).isEqualTo(UPDATED_CLIENT_TRANSACTION_ID);
        assertThat(testPayment.getPgStatus()).isEqualTo(UPDATED_PG_STATUS);
        assertThat(testPayment.getTransactioninitDate()).isEqualTo(UPDATED_TRANSACTIONINIT_DATE);
        assertThat(testPayment.getStatusUpdateDate()).isEqualTo(UPDATED_STATUS_UPDATE_DATE);
        assertThat(testPayment.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testPayment.getRedirectionUrl()).isEqualTo(UPDATED_REDIRECTION_URL);
        assertThat(testPayment.getPaymentAggregator()).isEqualTo(UPDATED_PAYMENT_AGGREGATOR);
        assertThat(testPayment.getPaymentgateway()).isEqualTo(UPDATED_PAYMENTGATEWAY);
        assertThat(testPayment.getPaymentStatus()).isEqualTo(UPDATED_PAYMENT_STATUS);
        assertThat(testPayment.getPaymentMethod()).isEqualTo(UPDATED_PAYMENT_METHOD);
        assertThat(testPayment.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testPayment.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
        assertThat(testPayment.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPayment.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingPayment() throws Exception {
        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();
        payment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, payment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(payment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPayment() throws Exception {
        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();
        payment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(payment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPayment() throws Exception {
        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();
        payment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(payment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePayment() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        int databaseSizeBeforeDelete = paymentRepository.findAll().size();

        // Delete the payment
        restPaymentMockMvc
            .perform(delete(ENTITY_API_URL_ID, payment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
