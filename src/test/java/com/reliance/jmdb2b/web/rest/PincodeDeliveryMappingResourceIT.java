package com.reliance.jmdb2b.web.rest;

import static com.reliance.jmdb2b.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.reliance.jmdb2b.IntegrationTest;
import com.reliance.jmdb2b.domain.PincodeDeliveryMapping;
import com.reliance.jmdb2b.domain.enumeration.WEIGHT;
import com.reliance.jmdb2b.repository.PincodeDeliveryMappingRepository;
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
 * Integration tests for the {@link PincodeDeliveryMappingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PincodeDeliveryMappingResourceIT {

    private static final Long DEFAULT_ORIGIN_LOCATION = 1L;
    private static final Long UPDATED_ORIGIN_LOCATION = 2L;

    private static final Long DEFAULT_DELIVERY_PINCODE = 1L;
    private static final Long UPDATED_DELIVERY_PINCODE = 2L;

    private static final WEIGHT DEFAULT_WEIGHT = WEIGHT.HEAVY;
    private static final WEIGHT UPDATED_WEIGHT = WEIGHT.MEDIUM;

    private static final Boolean DEFAULT_DELIVERABLE = false;
    private static final Boolean UPDATED_DELIVERABLE = true;

    private static final ZonedDateTime DEFAULT_NUM_OF_DAYS_TO_DELIVERY = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_NUM_OF_DAYS_TO_DELIVERY = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_CREATED_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/pincode-delivery-mappings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PincodeDeliveryMappingRepository pincodeDeliveryMappingRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPincodeDeliveryMappingMockMvc;

    private PincodeDeliveryMapping pincodeDeliveryMapping;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PincodeDeliveryMapping createEntity(EntityManager em) {
        PincodeDeliveryMapping pincodeDeliveryMapping = new PincodeDeliveryMapping()
            .originLocation(DEFAULT_ORIGIN_LOCATION)
            .deliveryPincode(DEFAULT_DELIVERY_PINCODE)
            .weight(DEFAULT_WEIGHT)
            .deliverable(DEFAULT_DELIVERABLE)
            .numOfDaysToDelivery(DEFAULT_NUM_OF_DAYS_TO_DELIVERY)
            .createdTime(DEFAULT_CREATED_TIME)
            .updatedTime(DEFAULT_UPDATED_TIME)
            .createdBy(DEFAULT_CREATED_BY)
            .updatedBy(DEFAULT_UPDATED_BY);
        return pincodeDeliveryMapping;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PincodeDeliveryMapping createUpdatedEntity(EntityManager em) {
        PincodeDeliveryMapping pincodeDeliveryMapping = new PincodeDeliveryMapping()
            .originLocation(UPDATED_ORIGIN_LOCATION)
            .deliveryPincode(UPDATED_DELIVERY_PINCODE)
            .weight(UPDATED_WEIGHT)
            .deliverable(UPDATED_DELIVERABLE)
            .numOfDaysToDelivery(UPDATED_NUM_OF_DAYS_TO_DELIVERY)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);
        return pincodeDeliveryMapping;
    }

    @BeforeEach
    public void initTest() {
        pincodeDeliveryMapping = createEntity(em);
    }

    @Test
    @Transactional
    void createPincodeDeliveryMapping() throws Exception {
        int databaseSizeBeforeCreate = pincodeDeliveryMappingRepository.findAll().size();
        // Create the PincodeDeliveryMapping
        restPincodeDeliveryMappingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pincodeDeliveryMapping))
            )
            .andExpect(status().isCreated());

        // Validate the PincodeDeliveryMapping in the database
        List<PincodeDeliveryMapping> pincodeDeliveryMappingList = pincodeDeliveryMappingRepository.findAll();
        assertThat(pincodeDeliveryMappingList).hasSize(databaseSizeBeforeCreate + 1);
        PincodeDeliveryMapping testPincodeDeliveryMapping = pincodeDeliveryMappingList.get(pincodeDeliveryMappingList.size() - 1);
        assertThat(testPincodeDeliveryMapping.getOriginLocation()).isEqualTo(DEFAULT_ORIGIN_LOCATION);
        assertThat(testPincodeDeliveryMapping.getDeliveryPincode()).isEqualTo(DEFAULT_DELIVERY_PINCODE);
        assertThat(testPincodeDeliveryMapping.getWeight()).isEqualTo(DEFAULT_WEIGHT);
        assertThat(testPincodeDeliveryMapping.getDeliverable()).isEqualTo(DEFAULT_DELIVERABLE);
        assertThat(testPincodeDeliveryMapping.getNumOfDaysToDelivery()).isEqualTo(DEFAULT_NUM_OF_DAYS_TO_DELIVERY);
        assertThat(testPincodeDeliveryMapping.getCreatedTime()).isEqualTo(DEFAULT_CREATED_TIME);
        assertThat(testPincodeDeliveryMapping.getUpdatedTime()).isEqualTo(DEFAULT_UPDATED_TIME);
        assertThat(testPincodeDeliveryMapping.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testPincodeDeliveryMapping.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    void createPincodeDeliveryMappingWithExistingId() throws Exception {
        // Create the PincodeDeliveryMapping with an existing ID
        pincodeDeliveryMapping.setId(1L);

        int databaseSizeBeforeCreate = pincodeDeliveryMappingRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPincodeDeliveryMappingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pincodeDeliveryMapping))
            )
            .andExpect(status().isBadRequest());

        // Validate the PincodeDeliveryMapping in the database
        List<PincodeDeliveryMapping> pincodeDeliveryMappingList = pincodeDeliveryMappingRepository.findAll();
        assertThat(pincodeDeliveryMappingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPincodeDeliveryMappings() throws Exception {
        // Initialize the database
        pincodeDeliveryMappingRepository.saveAndFlush(pincodeDeliveryMapping);

        // Get all the pincodeDeliveryMappingList
        restPincodeDeliveryMappingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pincodeDeliveryMapping.getId().intValue())))
            .andExpect(jsonPath("$.[*].originLocation").value(hasItem(DEFAULT_ORIGIN_LOCATION.intValue())))
            .andExpect(jsonPath("$.[*].deliveryPincode").value(hasItem(DEFAULT_DELIVERY_PINCODE.intValue())))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT.toString())))
            .andExpect(jsonPath("$.[*].deliverable").value(hasItem(DEFAULT_DELIVERABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].numOfDaysToDelivery").value(hasItem(sameInstant(DEFAULT_NUM_OF_DAYS_TO_DELIVERY))))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(sameInstant(DEFAULT_CREATED_TIME))))
            .andExpect(jsonPath("$.[*].updatedTime").value(hasItem(sameInstant(DEFAULT_UPDATED_TIME))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }

    @Test
    @Transactional
    void getPincodeDeliveryMapping() throws Exception {
        // Initialize the database
        pincodeDeliveryMappingRepository.saveAndFlush(pincodeDeliveryMapping);

        // Get the pincodeDeliveryMapping
        restPincodeDeliveryMappingMockMvc
            .perform(get(ENTITY_API_URL_ID, pincodeDeliveryMapping.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pincodeDeliveryMapping.getId().intValue()))
            .andExpect(jsonPath("$.originLocation").value(DEFAULT_ORIGIN_LOCATION.intValue()))
            .andExpect(jsonPath("$.deliveryPincode").value(DEFAULT_DELIVERY_PINCODE.intValue()))
            .andExpect(jsonPath("$.weight").value(DEFAULT_WEIGHT.toString()))
            .andExpect(jsonPath("$.deliverable").value(DEFAULT_DELIVERABLE.booleanValue()))
            .andExpect(jsonPath("$.numOfDaysToDelivery").value(sameInstant(DEFAULT_NUM_OF_DAYS_TO_DELIVERY)))
            .andExpect(jsonPath("$.createdTime").value(sameInstant(DEFAULT_CREATED_TIME)))
            .andExpect(jsonPath("$.updatedTime").value(sameInstant(DEFAULT_UPDATED_TIME)))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingPincodeDeliveryMapping() throws Exception {
        // Get the pincodeDeliveryMapping
        restPincodeDeliveryMappingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPincodeDeliveryMapping() throws Exception {
        // Initialize the database
        pincodeDeliveryMappingRepository.saveAndFlush(pincodeDeliveryMapping);

        int databaseSizeBeforeUpdate = pincodeDeliveryMappingRepository.findAll().size();

        // Update the pincodeDeliveryMapping
        PincodeDeliveryMapping updatedPincodeDeliveryMapping = pincodeDeliveryMappingRepository
            .findById(pincodeDeliveryMapping.getId())
            .get();
        // Disconnect from session so that the updates on updatedPincodeDeliveryMapping are not directly saved in db
        em.detach(updatedPincodeDeliveryMapping);
        updatedPincodeDeliveryMapping
            .originLocation(UPDATED_ORIGIN_LOCATION)
            .deliveryPincode(UPDATED_DELIVERY_PINCODE)
            .weight(UPDATED_WEIGHT)
            .deliverable(UPDATED_DELIVERABLE)
            .numOfDaysToDelivery(UPDATED_NUM_OF_DAYS_TO_DELIVERY)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restPincodeDeliveryMappingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPincodeDeliveryMapping.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPincodeDeliveryMapping))
            )
            .andExpect(status().isOk());

        // Validate the PincodeDeliveryMapping in the database
        List<PincodeDeliveryMapping> pincodeDeliveryMappingList = pincodeDeliveryMappingRepository.findAll();
        assertThat(pincodeDeliveryMappingList).hasSize(databaseSizeBeforeUpdate);
        PincodeDeliveryMapping testPincodeDeliveryMapping = pincodeDeliveryMappingList.get(pincodeDeliveryMappingList.size() - 1);
        assertThat(testPincodeDeliveryMapping.getOriginLocation()).isEqualTo(UPDATED_ORIGIN_LOCATION);
        assertThat(testPincodeDeliveryMapping.getDeliveryPincode()).isEqualTo(UPDATED_DELIVERY_PINCODE);
        assertThat(testPincodeDeliveryMapping.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testPincodeDeliveryMapping.getDeliverable()).isEqualTo(UPDATED_DELIVERABLE);
        assertThat(testPincodeDeliveryMapping.getNumOfDaysToDelivery()).isEqualTo(UPDATED_NUM_OF_DAYS_TO_DELIVERY);
        assertThat(testPincodeDeliveryMapping.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testPincodeDeliveryMapping.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
        assertThat(testPincodeDeliveryMapping.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPincodeDeliveryMapping.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void putNonExistingPincodeDeliveryMapping() throws Exception {
        int databaseSizeBeforeUpdate = pincodeDeliveryMappingRepository.findAll().size();
        pincodeDeliveryMapping.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPincodeDeliveryMappingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pincodeDeliveryMapping.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pincodeDeliveryMapping))
            )
            .andExpect(status().isBadRequest());

        // Validate the PincodeDeliveryMapping in the database
        List<PincodeDeliveryMapping> pincodeDeliveryMappingList = pincodeDeliveryMappingRepository.findAll();
        assertThat(pincodeDeliveryMappingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPincodeDeliveryMapping() throws Exception {
        int databaseSizeBeforeUpdate = pincodeDeliveryMappingRepository.findAll().size();
        pincodeDeliveryMapping.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPincodeDeliveryMappingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pincodeDeliveryMapping))
            )
            .andExpect(status().isBadRequest());

        // Validate the PincodeDeliveryMapping in the database
        List<PincodeDeliveryMapping> pincodeDeliveryMappingList = pincodeDeliveryMappingRepository.findAll();
        assertThat(pincodeDeliveryMappingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPincodeDeliveryMapping() throws Exception {
        int databaseSizeBeforeUpdate = pincodeDeliveryMappingRepository.findAll().size();
        pincodeDeliveryMapping.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPincodeDeliveryMappingMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pincodeDeliveryMapping))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PincodeDeliveryMapping in the database
        List<PincodeDeliveryMapping> pincodeDeliveryMappingList = pincodeDeliveryMappingRepository.findAll();
        assertThat(pincodeDeliveryMappingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePincodeDeliveryMappingWithPatch() throws Exception {
        // Initialize the database
        pincodeDeliveryMappingRepository.saveAndFlush(pincodeDeliveryMapping);

        int databaseSizeBeforeUpdate = pincodeDeliveryMappingRepository.findAll().size();

        // Update the pincodeDeliveryMapping using partial update
        PincodeDeliveryMapping partialUpdatedPincodeDeliveryMapping = new PincodeDeliveryMapping();
        partialUpdatedPincodeDeliveryMapping.setId(pincodeDeliveryMapping.getId());

        partialUpdatedPincodeDeliveryMapping.deliveryPincode(UPDATED_DELIVERY_PINCODE).updatedBy(UPDATED_UPDATED_BY);

        restPincodeDeliveryMappingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPincodeDeliveryMapping.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPincodeDeliveryMapping))
            )
            .andExpect(status().isOk());

        // Validate the PincodeDeliveryMapping in the database
        List<PincodeDeliveryMapping> pincodeDeliveryMappingList = pincodeDeliveryMappingRepository.findAll();
        assertThat(pincodeDeliveryMappingList).hasSize(databaseSizeBeforeUpdate);
        PincodeDeliveryMapping testPincodeDeliveryMapping = pincodeDeliveryMappingList.get(pincodeDeliveryMappingList.size() - 1);
        assertThat(testPincodeDeliveryMapping.getOriginLocation()).isEqualTo(DEFAULT_ORIGIN_LOCATION);
        assertThat(testPincodeDeliveryMapping.getDeliveryPincode()).isEqualTo(UPDATED_DELIVERY_PINCODE);
        assertThat(testPincodeDeliveryMapping.getWeight()).isEqualTo(DEFAULT_WEIGHT);
        assertThat(testPincodeDeliveryMapping.getDeliverable()).isEqualTo(DEFAULT_DELIVERABLE);
        assertThat(testPincodeDeliveryMapping.getNumOfDaysToDelivery()).isEqualTo(DEFAULT_NUM_OF_DAYS_TO_DELIVERY);
        assertThat(testPincodeDeliveryMapping.getCreatedTime()).isEqualTo(DEFAULT_CREATED_TIME);
        assertThat(testPincodeDeliveryMapping.getUpdatedTime()).isEqualTo(DEFAULT_UPDATED_TIME);
        assertThat(testPincodeDeliveryMapping.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testPincodeDeliveryMapping.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void fullUpdatePincodeDeliveryMappingWithPatch() throws Exception {
        // Initialize the database
        pincodeDeliveryMappingRepository.saveAndFlush(pincodeDeliveryMapping);

        int databaseSizeBeforeUpdate = pincodeDeliveryMappingRepository.findAll().size();

        // Update the pincodeDeliveryMapping using partial update
        PincodeDeliveryMapping partialUpdatedPincodeDeliveryMapping = new PincodeDeliveryMapping();
        partialUpdatedPincodeDeliveryMapping.setId(pincodeDeliveryMapping.getId());

        partialUpdatedPincodeDeliveryMapping
            .originLocation(UPDATED_ORIGIN_LOCATION)
            .deliveryPincode(UPDATED_DELIVERY_PINCODE)
            .weight(UPDATED_WEIGHT)
            .deliverable(UPDATED_DELIVERABLE)
            .numOfDaysToDelivery(UPDATED_NUM_OF_DAYS_TO_DELIVERY)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restPincodeDeliveryMappingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPincodeDeliveryMapping.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPincodeDeliveryMapping))
            )
            .andExpect(status().isOk());

        // Validate the PincodeDeliveryMapping in the database
        List<PincodeDeliveryMapping> pincodeDeliveryMappingList = pincodeDeliveryMappingRepository.findAll();
        assertThat(pincodeDeliveryMappingList).hasSize(databaseSizeBeforeUpdate);
        PincodeDeliveryMapping testPincodeDeliveryMapping = pincodeDeliveryMappingList.get(pincodeDeliveryMappingList.size() - 1);
        assertThat(testPincodeDeliveryMapping.getOriginLocation()).isEqualTo(UPDATED_ORIGIN_LOCATION);
        assertThat(testPincodeDeliveryMapping.getDeliveryPincode()).isEqualTo(UPDATED_DELIVERY_PINCODE);
        assertThat(testPincodeDeliveryMapping.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testPincodeDeliveryMapping.getDeliverable()).isEqualTo(UPDATED_DELIVERABLE);
        assertThat(testPincodeDeliveryMapping.getNumOfDaysToDelivery()).isEqualTo(UPDATED_NUM_OF_DAYS_TO_DELIVERY);
        assertThat(testPincodeDeliveryMapping.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testPincodeDeliveryMapping.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
        assertThat(testPincodeDeliveryMapping.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPincodeDeliveryMapping.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingPincodeDeliveryMapping() throws Exception {
        int databaseSizeBeforeUpdate = pincodeDeliveryMappingRepository.findAll().size();
        pincodeDeliveryMapping.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPincodeDeliveryMappingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pincodeDeliveryMapping.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pincodeDeliveryMapping))
            )
            .andExpect(status().isBadRequest());

        // Validate the PincodeDeliveryMapping in the database
        List<PincodeDeliveryMapping> pincodeDeliveryMappingList = pincodeDeliveryMappingRepository.findAll();
        assertThat(pincodeDeliveryMappingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPincodeDeliveryMapping() throws Exception {
        int databaseSizeBeforeUpdate = pincodeDeliveryMappingRepository.findAll().size();
        pincodeDeliveryMapping.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPincodeDeliveryMappingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pincodeDeliveryMapping))
            )
            .andExpect(status().isBadRequest());

        // Validate the PincodeDeliveryMapping in the database
        List<PincodeDeliveryMapping> pincodeDeliveryMappingList = pincodeDeliveryMappingRepository.findAll();
        assertThat(pincodeDeliveryMappingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPincodeDeliveryMapping() throws Exception {
        int databaseSizeBeforeUpdate = pincodeDeliveryMappingRepository.findAll().size();
        pincodeDeliveryMapping.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPincodeDeliveryMappingMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pincodeDeliveryMapping))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PincodeDeliveryMapping in the database
        List<PincodeDeliveryMapping> pincodeDeliveryMappingList = pincodeDeliveryMappingRepository.findAll();
        assertThat(pincodeDeliveryMappingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePincodeDeliveryMapping() throws Exception {
        // Initialize the database
        pincodeDeliveryMappingRepository.saveAndFlush(pincodeDeliveryMapping);

        int databaseSizeBeforeDelete = pincodeDeliveryMappingRepository.findAll().size();

        // Delete the pincodeDeliveryMapping
        restPincodeDeliveryMappingMockMvc
            .perform(delete(ENTITY_API_URL_ID, pincodeDeliveryMapping.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PincodeDeliveryMapping> pincodeDeliveryMappingList = pincodeDeliveryMappingRepository.findAll();
        assertThat(pincodeDeliveryMappingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
