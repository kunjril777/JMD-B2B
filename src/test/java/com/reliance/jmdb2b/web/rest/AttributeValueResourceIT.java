package com.reliance.jmdb2b.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.reliance.jmdb2b.IntegrationTest;
import com.reliance.jmdb2b.domain.AttributeValue;
import com.reliance.jmdb2b.repository.AttributeValueRepository;
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
 * Integration tests for the {@link AttributeValueResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AttributeValueResourceIT {

    private static final String DEFAULT_PROPERTY_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_PROPERTY_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_PROPERTY_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_PROPERTY_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_PLP_DISPLAY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PLP_DISPLAY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PDP_DISPLAY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PDP_DISPLAY_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_DISPLAY_ORDER = 1L;
    private static final Long UPDATED_DISPLAY_ORDER = 2L;

    private static final String ENTITY_API_URL = "/api/attribute-values";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AttributeValueRepository attributeValueRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAttributeValueMockMvc;

    private AttributeValue attributeValue;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AttributeValue createEntity(EntityManager em) {
        AttributeValue attributeValue = new AttributeValue()
            .propertyValue(DEFAULT_PROPERTY_VALUE)
            .propertyDescription(DEFAULT_PROPERTY_DESCRIPTION)
            .plpDisplayName(DEFAULT_PLP_DISPLAY_NAME)
            .pdpDisplayName(DEFAULT_PDP_DISPLAY_NAME)
            .displayOrder(DEFAULT_DISPLAY_ORDER);
        return attributeValue;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AttributeValue createUpdatedEntity(EntityManager em) {
        AttributeValue attributeValue = new AttributeValue()
            .propertyValue(UPDATED_PROPERTY_VALUE)
            .propertyDescription(UPDATED_PROPERTY_DESCRIPTION)
            .plpDisplayName(UPDATED_PLP_DISPLAY_NAME)
            .pdpDisplayName(UPDATED_PDP_DISPLAY_NAME)
            .displayOrder(UPDATED_DISPLAY_ORDER);
        return attributeValue;
    }

    @BeforeEach
    public void initTest() {
        attributeValue = createEntity(em);
    }

    @Test
    @Transactional
    void createAttributeValue() throws Exception {
        int databaseSizeBeforeCreate = attributeValueRepository.findAll().size();
        // Create the AttributeValue
        restAttributeValueMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(attributeValue))
            )
            .andExpect(status().isCreated());

        // Validate the AttributeValue in the database
        List<AttributeValue> attributeValueList = attributeValueRepository.findAll();
        assertThat(attributeValueList).hasSize(databaseSizeBeforeCreate + 1);
        AttributeValue testAttributeValue = attributeValueList.get(attributeValueList.size() - 1);
        assertThat(testAttributeValue.getPropertyValue()).isEqualTo(DEFAULT_PROPERTY_VALUE);
        assertThat(testAttributeValue.getPropertyDescription()).isEqualTo(DEFAULT_PROPERTY_DESCRIPTION);
        assertThat(testAttributeValue.getPlpDisplayName()).isEqualTo(DEFAULT_PLP_DISPLAY_NAME);
        assertThat(testAttributeValue.getPdpDisplayName()).isEqualTo(DEFAULT_PDP_DISPLAY_NAME);
        assertThat(testAttributeValue.getDisplayOrder()).isEqualTo(DEFAULT_DISPLAY_ORDER);
    }

    @Test
    @Transactional
    void createAttributeValueWithExistingId() throws Exception {
        // Create the AttributeValue with an existing ID
        attributeValue.setId(1L);

        int databaseSizeBeforeCreate = attributeValueRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAttributeValueMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(attributeValue))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttributeValue in the database
        List<AttributeValue> attributeValueList = attributeValueRepository.findAll();
        assertThat(attributeValueList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAttributeValues() throws Exception {
        // Initialize the database
        attributeValueRepository.saveAndFlush(attributeValue);

        // Get all the attributeValueList
        restAttributeValueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attributeValue.getId().intValue())))
            .andExpect(jsonPath("$.[*].propertyValue").value(hasItem(DEFAULT_PROPERTY_VALUE)))
            .andExpect(jsonPath("$.[*].propertyDescription").value(hasItem(DEFAULT_PROPERTY_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].plpDisplayName").value(hasItem(DEFAULT_PLP_DISPLAY_NAME)))
            .andExpect(jsonPath("$.[*].pdpDisplayName").value(hasItem(DEFAULT_PDP_DISPLAY_NAME)))
            .andExpect(jsonPath("$.[*].displayOrder").value(hasItem(DEFAULT_DISPLAY_ORDER.intValue())));
    }

    @Test
    @Transactional
    void getAttributeValue() throws Exception {
        // Initialize the database
        attributeValueRepository.saveAndFlush(attributeValue);

        // Get the attributeValue
        restAttributeValueMockMvc
            .perform(get(ENTITY_API_URL_ID, attributeValue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(attributeValue.getId().intValue()))
            .andExpect(jsonPath("$.propertyValue").value(DEFAULT_PROPERTY_VALUE))
            .andExpect(jsonPath("$.propertyDescription").value(DEFAULT_PROPERTY_DESCRIPTION))
            .andExpect(jsonPath("$.plpDisplayName").value(DEFAULT_PLP_DISPLAY_NAME))
            .andExpect(jsonPath("$.pdpDisplayName").value(DEFAULT_PDP_DISPLAY_NAME))
            .andExpect(jsonPath("$.displayOrder").value(DEFAULT_DISPLAY_ORDER.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingAttributeValue() throws Exception {
        // Get the attributeValue
        restAttributeValueMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAttributeValue() throws Exception {
        // Initialize the database
        attributeValueRepository.saveAndFlush(attributeValue);

        int databaseSizeBeforeUpdate = attributeValueRepository.findAll().size();

        // Update the attributeValue
        AttributeValue updatedAttributeValue = attributeValueRepository.findById(attributeValue.getId()).get();
        // Disconnect from session so that the updates on updatedAttributeValue are not directly saved in db
        em.detach(updatedAttributeValue);
        updatedAttributeValue
            .propertyValue(UPDATED_PROPERTY_VALUE)
            .propertyDescription(UPDATED_PROPERTY_DESCRIPTION)
            .plpDisplayName(UPDATED_PLP_DISPLAY_NAME)
            .pdpDisplayName(UPDATED_PDP_DISPLAY_NAME)
            .displayOrder(UPDATED_DISPLAY_ORDER);

        restAttributeValueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAttributeValue.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAttributeValue))
            )
            .andExpect(status().isOk());

        // Validate the AttributeValue in the database
        List<AttributeValue> attributeValueList = attributeValueRepository.findAll();
        assertThat(attributeValueList).hasSize(databaseSizeBeforeUpdate);
        AttributeValue testAttributeValue = attributeValueList.get(attributeValueList.size() - 1);
        assertThat(testAttributeValue.getPropertyValue()).isEqualTo(UPDATED_PROPERTY_VALUE);
        assertThat(testAttributeValue.getPropertyDescription()).isEqualTo(UPDATED_PROPERTY_DESCRIPTION);
        assertThat(testAttributeValue.getPlpDisplayName()).isEqualTo(UPDATED_PLP_DISPLAY_NAME);
        assertThat(testAttributeValue.getPdpDisplayName()).isEqualTo(UPDATED_PDP_DISPLAY_NAME);
        assertThat(testAttributeValue.getDisplayOrder()).isEqualTo(UPDATED_DISPLAY_ORDER);
    }

    @Test
    @Transactional
    void putNonExistingAttributeValue() throws Exception {
        int databaseSizeBeforeUpdate = attributeValueRepository.findAll().size();
        attributeValue.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttributeValueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, attributeValue.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attributeValue))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttributeValue in the database
        List<AttributeValue> attributeValueList = attributeValueRepository.findAll();
        assertThat(attributeValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAttributeValue() throws Exception {
        int databaseSizeBeforeUpdate = attributeValueRepository.findAll().size();
        attributeValue.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttributeValueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attributeValue))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttributeValue in the database
        List<AttributeValue> attributeValueList = attributeValueRepository.findAll();
        assertThat(attributeValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAttributeValue() throws Exception {
        int databaseSizeBeforeUpdate = attributeValueRepository.findAll().size();
        attributeValue.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttributeValueMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(attributeValue)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AttributeValue in the database
        List<AttributeValue> attributeValueList = attributeValueRepository.findAll();
        assertThat(attributeValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAttributeValueWithPatch() throws Exception {
        // Initialize the database
        attributeValueRepository.saveAndFlush(attributeValue);

        int databaseSizeBeforeUpdate = attributeValueRepository.findAll().size();

        // Update the attributeValue using partial update
        AttributeValue partialUpdatedAttributeValue = new AttributeValue();
        partialUpdatedAttributeValue.setId(attributeValue.getId());

        partialUpdatedAttributeValue.propertyDescription(UPDATED_PROPERTY_DESCRIPTION).pdpDisplayName(UPDATED_PDP_DISPLAY_NAME);

        restAttributeValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAttributeValue.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAttributeValue))
            )
            .andExpect(status().isOk());

        // Validate the AttributeValue in the database
        List<AttributeValue> attributeValueList = attributeValueRepository.findAll();
        assertThat(attributeValueList).hasSize(databaseSizeBeforeUpdate);
        AttributeValue testAttributeValue = attributeValueList.get(attributeValueList.size() - 1);
        assertThat(testAttributeValue.getPropertyValue()).isEqualTo(DEFAULT_PROPERTY_VALUE);
        assertThat(testAttributeValue.getPropertyDescription()).isEqualTo(UPDATED_PROPERTY_DESCRIPTION);
        assertThat(testAttributeValue.getPlpDisplayName()).isEqualTo(DEFAULT_PLP_DISPLAY_NAME);
        assertThat(testAttributeValue.getPdpDisplayName()).isEqualTo(UPDATED_PDP_DISPLAY_NAME);
        assertThat(testAttributeValue.getDisplayOrder()).isEqualTo(DEFAULT_DISPLAY_ORDER);
    }

    @Test
    @Transactional
    void fullUpdateAttributeValueWithPatch() throws Exception {
        // Initialize the database
        attributeValueRepository.saveAndFlush(attributeValue);

        int databaseSizeBeforeUpdate = attributeValueRepository.findAll().size();

        // Update the attributeValue using partial update
        AttributeValue partialUpdatedAttributeValue = new AttributeValue();
        partialUpdatedAttributeValue.setId(attributeValue.getId());

        partialUpdatedAttributeValue
            .propertyValue(UPDATED_PROPERTY_VALUE)
            .propertyDescription(UPDATED_PROPERTY_DESCRIPTION)
            .plpDisplayName(UPDATED_PLP_DISPLAY_NAME)
            .pdpDisplayName(UPDATED_PDP_DISPLAY_NAME)
            .displayOrder(UPDATED_DISPLAY_ORDER);

        restAttributeValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAttributeValue.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAttributeValue))
            )
            .andExpect(status().isOk());

        // Validate the AttributeValue in the database
        List<AttributeValue> attributeValueList = attributeValueRepository.findAll();
        assertThat(attributeValueList).hasSize(databaseSizeBeforeUpdate);
        AttributeValue testAttributeValue = attributeValueList.get(attributeValueList.size() - 1);
        assertThat(testAttributeValue.getPropertyValue()).isEqualTo(UPDATED_PROPERTY_VALUE);
        assertThat(testAttributeValue.getPropertyDescription()).isEqualTo(UPDATED_PROPERTY_DESCRIPTION);
        assertThat(testAttributeValue.getPlpDisplayName()).isEqualTo(UPDATED_PLP_DISPLAY_NAME);
        assertThat(testAttributeValue.getPdpDisplayName()).isEqualTo(UPDATED_PDP_DISPLAY_NAME);
        assertThat(testAttributeValue.getDisplayOrder()).isEqualTo(UPDATED_DISPLAY_ORDER);
    }

    @Test
    @Transactional
    void patchNonExistingAttributeValue() throws Exception {
        int databaseSizeBeforeUpdate = attributeValueRepository.findAll().size();
        attributeValue.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttributeValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, attributeValue.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(attributeValue))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttributeValue in the database
        List<AttributeValue> attributeValueList = attributeValueRepository.findAll();
        assertThat(attributeValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAttributeValue() throws Exception {
        int databaseSizeBeforeUpdate = attributeValueRepository.findAll().size();
        attributeValue.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttributeValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(attributeValue))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttributeValue in the database
        List<AttributeValue> attributeValueList = attributeValueRepository.findAll();
        assertThat(attributeValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAttributeValue() throws Exception {
        int databaseSizeBeforeUpdate = attributeValueRepository.findAll().size();
        attributeValue.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttributeValueMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(attributeValue))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AttributeValue in the database
        List<AttributeValue> attributeValueList = attributeValueRepository.findAll();
        assertThat(attributeValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAttributeValue() throws Exception {
        // Initialize the database
        attributeValueRepository.saveAndFlush(attributeValue);

        int databaseSizeBeforeDelete = attributeValueRepository.findAll().size();

        // Delete the attributeValue
        restAttributeValueMockMvc
            .perform(delete(ENTITY_API_URL_ID, attributeValue.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AttributeValue> attributeValueList = attributeValueRepository.findAll();
        assertThat(attributeValueList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
