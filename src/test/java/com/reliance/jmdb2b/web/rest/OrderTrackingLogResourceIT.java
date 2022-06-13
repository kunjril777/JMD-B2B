package com.reliance.jmdb2b.web.rest;

import static com.reliance.jmdb2b.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.reliance.jmdb2b.IntegrationTest;
import com.reliance.jmdb2b.domain.OrderTrackingLog;
import com.reliance.jmdb2b.domain.enumeration.ORDERSTATUS;
import com.reliance.jmdb2b.repository.OrderTrackingLogRepository;
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
 * Integration tests for the {@link OrderTrackingLogResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrderTrackingLogResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ORDERSTATUS DEFAULT_ORDER_TRACKING_STATUS = ORDERSTATUS.ORDERED;
    private static final ORDERSTATUS UPDATED_ORDER_TRACKING_STATUS = ORDERSTATUS.SHIPPED;

    private static final ZonedDateTime DEFAULT_CREATED_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/order-tracking-logs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrderTrackingLogRepository orderTrackingLogRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderTrackingLogMockMvc;

    private OrderTrackingLog orderTrackingLog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderTrackingLog createEntity(EntityManager em) {
        OrderTrackingLog orderTrackingLog = new OrderTrackingLog()
            .description(DEFAULT_DESCRIPTION)
            .orderTrackingStatus(DEFAULT_ORDER_TRACKING_STATUS)
            .createdTime(DEFAULT_CREATED_TIME)
            .updatedTime(DEFAULT_UPDATED_TIME)
            .createdBy(DEFAULT_CREATED_BY)
            .updatedBy(DEFAULT_UPDATED_BY);
        return orderTrackingLog;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderTrackingLog createUpdatedEntity(EntityManager em) {
        OrderTrackingLog orderTrackingLog = new OrderTrackingLog()
            .description(UPDATED_DESCRIPTION)
            .orderTrackingStatus(UPDATED_ORDER_TRACKING_STATUS)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);
        return orderTrackingLog;
    }

    @BeforeEach
    public void initTest() {
        orderTrackingLog = createEntity(em);
    }

    @Test
    @Transactional
    void createOrderTrackingLog() throws Exception {
        int databaseSizeBeforeCreate = orderTrackingLogRepository.findAll().size();
        // Create the OrderTrackingLog
        restOrderTrackingLogMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderTrackingLog))
            )
            .andExpect(status().isCreated());

        // Validate the OrderTrackingLog in the database
        List<OrderTrackingLog> orderTrackingLogList = orderTrackingLogRepository.findAll();
        assertThat(orderTrackingLogList).hasSize(databaseSizeBeforeCreate + 1);
        OrderTrackingLog testOrderTrackingLog = orderTrackingLogList.get(orderTrackingLogList.size() - 1);
        assertThat(testOrderTrackingLog.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testOrderTrackingLog.getOrderTrackingStatus()).isEqualTo(DEFAULT_ORDER_TRACKING_STATUS);
        assertThat(testOrderTrackingLog.getCreatedTime()).isEqualTo(DEFAULT_CREATED_TIME);
        assertThat(testOrderTrackingLog.getUpdatedTime()).isEqualTo(DEFAULT_UPDATED_TIME);
        assertThat(testOrderTrackingLog.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testOrderTrackingLog.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    void createOrderTrackingLogWithExistingId() throws Exception {
        // Create the OrderTrackingLog with an existing ID
        orderTrackingLog.setId(1L);

        int databaseSizeBeforeCreate = orderTrackingLogRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderTrackingLogMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderTrackingLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderTrackingLog in the database
        List<OrderTrackingLog> orderTrackingLogList = orderTrackingLogRepository.findAll();
        assertThat(orderTrackingLogList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrderTrackingLogs() throws Exception {
        // Initialize the database
        orderTrackingLogRepository.saveAndFlush(orderTrackingLog);

        // Get all the orderTrackingLogList
        restOrderTrackingLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderTrackingLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].orderTrackingStatus").value(hasItem(DEFAULT_ORDER_TRACKING_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(sameInstant(DEFAULT_CREATED_TIME))))
            .andExpect(jsonPath("$.[*].updatedTime").value(hasItem(sameInstant(DEFAULT_UPDATED_TIME))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }

    @Test
    @Transactional
    void getOrderTrackingLog() throws Exception {
        // Initialize the database
        orderTrackingLogRepository.saveAndFlush(orderTrackingLog);

        // Get the orderTrackingLog
        restOrderTrackingLogMockMvc
            .perform(get(ENTITY_API_URL_ID, orderTrackingLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderTrackingLog.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.orderTrackingStatus").value(DEFAULT_ORDER_TRACKING_STATUS.toString()))
            .andExpect(jsonPath("$.createdTime").value(sameInstant(DEFAULT_CREATED_TIME)))
            .andExpect(jsonPath("$.updatedTime").value(sameInstant(DEFAULT_UPDATED_TIME)))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingOrderTrackingLog() throws Exception {
        // Get the orderTrackingLog
        restOrderTrackingLogMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrderTrackingLog() throws Exception {
        // Initialize the database
        orderTrackingLogRepository.saveAndFlush(orderTrackingLog);

        int databaseSizeBeforeUpdate = orderTrackingLogRepository.findAll().size();

        // Update the orderTrackingLog
        OrderTrackingLog updatedOrderTrackingLog = orderTrackingLogRepository.findById(orderTrackingLog.getId()).get();
        // Disconnect from session so that the updates on updatedOrderTrackingLog are not directly saved in db
        em.detach(updatedOrderTrackingLog);
        updatedOrderTrackingLog
            .description(UPDATED_DESCRIPTION)
            .orderTrackingStatus(UPDATED_ORDER_TRACKING_STATUS)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restOrderTrackingLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrderTrackingLog.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrderTrackingLog))
            )
            .andExpect(status().isOk());

        // Validate the OrderTrackingLog in the database
        List<OrderTrackingLog> orderTrackingLogList = orderTrackingLogRepository.findAll();
        assertThat(orderTrackingLogList).hasSize(databaseSizeBeforeUpdate);
        OrderTrackingLog testOrderTrackingLog = orderTrackingLogList.get(orderTrackingLogList.size() - 1);
        assertThat(testOrderTrackingLog.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOrderTrackingLog.getOrderTrackingStatus()).isEqualTo(UPDATED_ORDER_TRACKING_STATUS);
        assertThat(testOrderTrackingLog.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testOrderTrackingLog.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
        assertThat(testOrderTrackingLog.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testOrderTrackingLog.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void putNonExistingOrderTrackingLog() throws Exception {
        int databaseSizeBeforeUpdate = orderTrackingLogRepository.findAll().size();
        orderTrackingLog.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderTrackingLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderTrackingLog.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderTrackingLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderTrackingLog in the database
        List<OrderTrackingLog> orderTrackingLogList = orderTrackingLogRepository.findAll();
        assertThat(orderTrackingLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderTrackingLog() throws Exception {
        int databaseSizeBeforeUpdate = orderTrackingLogRepository.findAll().size();
        orderTrackingLog.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderTrackingLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderTrackingLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderTrackingLog in the database
        List<OrderTrackingLog> orderTrackingLogList = orderTrackingLogRepository.findAll();
        assertThat(orderTrackingLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderTrackingLog() throws Exception {
        int databaseSizeBeforeUpdate = orderTrackingLogRepository.findAll().size();
        orderTrackingLog.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderTrackingLogMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderTrackingLog))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderTrackingLog in the database
        List<OrderTrackingLog> orderTrackingLogList = orderTrackingLogRepository.findAll();
        assertThat(orderTrackingLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderTrackingLogWithPatch() throws Exception {
        // Initialize the database
        orderTrackingLogRepository.saveAndFlush(orderTrackingLog);

        int databaseSizeBeforeUpdate = orderTrackingLogRepository.findAll().size();

        // Update the orderTrackingLog using partial update
        OrderTrackingLog partialUpdatedOrderTrackingLog = new OrderTrackingLog();
        partialUpdatedOrderTrackingLog.setId(orderTrackingLog.getId());

        partialUpdatedOrderTrackingLog
            .orderTrackingStatus(UPDATED_ORDER_TRACKING_STATUS)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedBy(UPDATED_UPDATED_BY);

        restOrderTrackingLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderTrackingLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderTrackingLog))
            )
            .andExpect(status().isOk());

        // Validate the OrderTrackingLog in the database
        List<OrderTrackingLog> orderTrackingLogList = orderTrackingLogRepository.findAll();
        assertThat(orderTrackingLogList).hasSize(databaseSizeBeforeUpdate);
        OrderTrackingLog testOrderTrackingLog = orderTrackingLogList.get(orderTrackingLogList.size() - 1);
        assertThat(testOrderTrackingLog.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testOrderTrackingLog.getOrderTrackingStatus()).isEqualTo(UPDATED_ORDER_TRACKING_STATUS);
        assertThat(testOrderTrackingLog.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testOrderTrackingLog.getUpdatedTime()).isEqualTo(DEFAULT_UPDATED_TIME);
        assertThat(testOrderTrackingLog.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testOrderTrackingLog.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void fullUpdateOrderTrackingLogWithPatch() throws Exception {
        // Initialize the database
        orderTrackingLogRepository.saveAndFlush(orderTrackingLog);

        int databaseSizeBeforeUpdate = orderTrackingLogRepository.findAll().size();

        // Update the orderTrackingLog using partial update
        OrderTrackingLog partialUpdatedOrderTrackingLog = new OrderTrackingLog();
        partialUpdatedOrderTrackingLog.setId(orderTrackingLog.getId());

        partialUpdatedOrderTrackingLog
            .description(UPDATED_DESCRIPTION)
            .orderTrackingStatus(UPDATED_ORDER_TRACKING_STATUS)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restOrderTrackingLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderTrackingLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderTrackingLog))
            )
            .andExpect(status().isOk());

        // Validate the OrderTrackingLog in the database
        List<OrderTrackingLog> orderTrackingLogList = orderTrackingLogRepository.findAll();
        assertThat(orderTrackingLogList).hasSize(databaseSizeBeforeUpdate);
        OrderTrackingLog testOrderTrackingLog = orderTrackingLogList.get(orderTrackingLogList.size() - 1);
        assertThat(testOrderTrackingLog.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOrderTrackingLog.getOrderTrackingStatus()).isEqualTo(UPDATED_ORDER_TRACKING_STATUS);
        assertThat(testOrderTrackingLog.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testOrderTrackingLog.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
        assertThat(testOrderTrackingLog.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testOrderTrackingLog.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingOrderTrackingLog() throws Exception {
        int databaseSizeBeforeUpdate = orderTrackingLogRepository.findAll().size();
        orderTrackingLog.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderTrackingLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderTrackingLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderTrackingLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderTrackingLog in the database
        List<OrderTrackingLog> orderTrackingLogList = orderTrackingLogRepository.findAll();
        assertThat(orderTrackingLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderTrackingLog() throws Exception {
        int databaseSizeBeforeUpdate = orderTrackingLogRepository.findAll().size();
        orderTrackingLog.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderTrackingLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderTrackingLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderTrackingLog in the database
        List<OrderTrackingLog> orderTrackingLogList = orderTrackingLogRepository.findAll();
        assertThat(orderTrackingLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderTrackingLog() throws Exception {
        int databaseSizeBeforeUpdate = orderTrackingLogRepository.findAll().size();
        orderTrackingLog.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderTrackingLogMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderTrackingLog))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderTrackingLog in the database
        List<OrderTrackingLog> orderTrackingLogList = orderTrackingLogRepository.findAll();
        assertThat(orderTrackingLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrderTrackingLog() throws Exception {
        // Initialize the database
        orderTrackingLogRepository.saveAndFlush(orderTrackingLog);

        int databaseSizeBeforeDelete = orderTrackingLogRepository.findAll().size();

        // Delete the orderTrackingLog
        restOrderTrackingLogMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderTrackingLog.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrderTrackingLog> orderTrackingLogList = orderTrackingLogRepository.findAll();
        assertThat(orderTrackingLogList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
