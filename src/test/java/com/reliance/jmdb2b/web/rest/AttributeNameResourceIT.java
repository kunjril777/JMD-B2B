package com.reliance.jmdb2b.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.reliance.jmdb2b.IntegrationTest;
import com.reliance.jmdb2b.domain.AttributeName;
import com.reliance.jmdb2b.domain.enumeration.PROPERTYTYPE;
import com.reliance.jmdb2b.repository.AttributeNameRepository;
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
 * Integration tests for the {@link AttributeNameResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AttributeNameResourceIT {

    private static final String DEFAULT_PROPERTY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PROPERTY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PROPERTY_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_PROPERTY_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_MANDETORY = false;
    private static final Boolean UPDATED_MANDETORY = true;

    private static final PROPERTYTYPE DEFAULT_PROPERTY_TYPE = PROPERTYTYPE.PRODUCTVARIANT;
    private static final PROPERTYTYPE UPDATED_PROPERTY_TYPE = PROPERTYTYPE.CATERGORY;

    private static final String DEFAULT_PLP_DISPLAY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PLP_DISPLAY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PDP_DISPLAY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PDP_DISPLAY_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_PARENT_ATTRIBUTE_ID = 1L;
    private static final Long UPDATED_PARENT_ATTRIBUTE_ID = 2L;

    private static final Long DEFAULT_DISPLAY_ORDER = 1L;
    private static final Long UPDATED_DISPLAY_ORDER = 2L;

    private static final String ENTITY_API_URL = "/api/attribute-names";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AttributeNameRepository attributeNameRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAttributeNameMockMvc;

    private AttributeName attributeName;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AttributeName createEntity(EntityManager em) {
        AttributeName attributeName = new AttributeName()
            .propertyName(DEFAULT_PROPERTY_NAME)
            .propertyDescription(DEFAULT_PROPERTY_DESCRIPTION)
            .mandetory(DEFAULT_MANDETORY)
            .propertyType(DEFAULT_PROPERTY_TYPE)
            .plpDisplayName(DEFAULT_PLP_DISPLAY_NAME)
            .pdpDisplayName(DEFAULT_PDP_DISPLAY_NAME)
            .parentAttributeId(DEFAULT_PARENT_ATTRIBUTE_ID)
            .displayOrder(DEFAULT_DISPLAY_ORDER);
        return attributeName;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AttributeName createUpdatedEntity(EntityManager em) {
        AttributeName attributeName = new AttributeName()
            .propertyName(UPDATED_PROPERTY_NAME)
            .propertyDescription(UPDATED_PROPERTY_DESCRIPTION)
            .mandetory(UPDATED_MANDETORY)
            .propertyType(UPDATED_PROPERTY_TYPE)
            .plpDisplayName(UPDATED_PLP_DISPLAY_NAME)
            .pdpDisplayName(UPDATED_PDP_DISPLAY_NAME)
            .parentAttributeId(UPDATED_PARENT_ATTRIBUTE_ID)
            .displayOrder(UPDATED_DISPLAY_ORDER);
        return attributeName;
    }

    @BeforeEach
    public void initTest() {
        attributeName = createEntity(em);
    }

    @Test
    @Transactional
    void createAttributeName() throws Exception {
        int databaseSizeBeforeCreate = attributeNameRepository.findAll().size();
        // Create the AttributeName
        restAttributeNameMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(attributeName)))
            .andExpect(status().isCreated());

        // Validate the AttributeName in the database
        List<AttributeName> attributeNameList = attributeNameRepository.findAll();
        assertThat(attributeNameList).hasSize(databaseSizeBeforeCreate + 1);
        AttributeName testAttributeName = attributeNameList.get(attributeNameList.size() - 1);
        assertThat(testAttributeName.getPropertyName()).isEqualTo(DEFAULT_PROPERTY_NAME);
        assertThat(testAttributeName.getPropertyDescription()).isEqualTo(DEFAULT_PROPERTY_DESCRIPTION);
        assertThat(testAttributeName.getMandetory()).isEqualTo(DEFAULT_MANDETORY);
        assertThat(testAttributeName.getPropertyType()).isEqualTo(DEFAULT_PROPERTY_TYPE);
        assertThat(testAttributeName.getPlpDisplayName()).isEqualTo(DEFAULT_PLP_DISPLAY_NAME);
        assertThat(testAttributeName.getPdpDisplayName()).isEqualTo(DEFAULT_PDP_DISPLAY_NAME);
        assertThat(testAttributeName.getParentAttributeId()).isEqualTo(DEFAULT_PARENT_ATTRIBUTE_ID);
        assertThat(testAttributeName.getDisplayOrder()).isEqualTo(DEFAULT_DISPLAY_ORDER);
    }

    @Test
    @Transactional
    void createAttributeNameWithExistingId() throws Exception {
        // Create the AttributeName with an existing ID
        attributeName.setId(1L);

        int databaseSizeBeforeCreate = attributeNameRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAttributeNameMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(attributeName)))
            .andExpect(status().isBadRequest());

        // Validate the AttributeName in the database
        List<AttributeName> attributeNameList = attributeNameRepository.findAll();
        assertThat(attributeNameList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAttributeNames() throws Exception {
        // Initialize the database
        attributeNameRepository.saveAndFlush(attributeName);

        // Get all the attributeNameList
        restAttributeNameMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attributeName.getId().intValue())))
            .andExpect(jsonPath("$.[*].propertyName").value(hasItem(DEFAULT_PROPERTY_NAME)))
            .andExpect(jsonPath("$.[*].propertyDescription").value(hasItem(DEFAULT_PROPERTY_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].mandetory").value(hasItem(DEFAULT_MANDETORY.booleanValue())))
            .andExpect(jsonPath("$.[*].propertyType").value(hasItem(DEFAULT_PROPERTY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].plpDisplayName").value(hasItem(DEFAULT_PLP_DISPLAY_NAME)))
            .andExpect(jsonPath("$.[*].pdpDisplayName").value(hasItem(DEFAULT_PDP_DISPLAY_NAME)))
            .andExpect(jsonPath("$.[*].parentAttributeId").value(hasItem(DEFAULT_PARENT_ATTRIBUTE_ID.intValue())))
            .andExpect(jsonPath("$.[*].displayOrder").value(hasItem(DEFAULT_DISPLAY_ORDER.intValue())));
    }

    @Test
    @Transactional
    void getAttributeName() throws Exception {
        // Initialize the database
        attributeNameRepository.saveAndFlush(attributeName);

        // Get the attributeName
        restAttributeNameMockMvc
            .perform(get(ENTITY_API_URL_ID, attributeName.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(attributeName.getId().intValue()))
            .andExpect(jsonPath("$.propertyName").value(DEFAULT_PROPERTY_NAME))
            .andExpect(jsonPath("$.propertyDescription").value(DEFAULT_PROPERTY_DESCRIPTION))
            .andExpect(jsonPath("$.mandetory").value(DEFAULT_MANDETORY.booleanValue()))
            .andExpect(jsonPath("$.propertyType").value(DEFAULT_PROPERTY_TYPE.toString()))
            .andExpect(jsonPath("$.plpDisplayName").value(DEFAULT_PLP_DISPLAY_NAME))
            .andExpect(jsonPath("$.pdpDisplayName").value(DEFAULT_PDP_DISPLAY_NAME))
            .andExpect(jsonPath("$.parentAttributeId").value(DEFAULT_PARENT_ATTRIBUTE_ID.intValue()))
            .andExpect(jsonPath("$.displayOrder").value(DEFAULT_DISPLAY_ORDER.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingAttributeName() throws Exception {
        // Get the attributeName
        restAttributeNameMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAttributeName() throws Exception {
        // Initialize the database
        attributeNameRepository.saveAndFlush(attributeName);

        int databaseSizeBeforeUpdate = attributeNameRepository.findAll().size();

        // Update the attributeName
        AttributeName updatedAttributeName = attributeNameRepository.findById(attributeName.getId()).get();
        // Disconnect from session so that the updates on updatedAttributeName are not directly saved in db
        em.detach(updatedAttributeName);
        updatedAttributeName
            .propertyName(UPDATED_PROPERTY_NAME)
            .propertyDescription(UPDATED_PROPERTY_DESCRIPTION)
            .mandetory(UPDATED_MANDETORY)
            .propertyType(UPDATED_PROPERTY_TYPE)
            .plpDisplayName(UPDATED_PLP_DISPLAY_NAME)
            .pdpDisplayName(UPDATED_PDP_DISPLAY_NAME)
            .parentAttributeId(UPDATED_PARENT_ATTRIBUTE_ID)
            .displayOrder(UPDATED_DISPLAY_ORDER);

        restAttributeNameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAttributeName.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAttributeName))
            )
            .andExpect(status().isOk());

        // Validate the AttributeName in the database
        List<AttributeName> attributeNameList = attributeNameRepository.findAll();
        assertThat(attributeNameList).hasSize(databaseSizeBeforeUpdate);
        AttributeName testAttributeName = attributeNameList.get(attributeNameList.size() - 1);
        assertThat(testAttributeName.getPropertyName()).isEqualTo(UPDATED_PROPERTY_NAME);
        assertThat(testAttributeName.getPropertyDescription()).isEqualTo(UPDATED_PROPERTY_DESCRIPTION);
        assertThat(testAttributeName.getMandetory()).isEqualTo(UPDATED_MANDETORY);
        assertThat(testAttributeName.getPropertyType()).isEqualTo(UPDATED_PROPERTY_TYPE);
        assertThat(testAttributeName.getPlpDisplayName()).isEqualTo(UPDATED_PLP_DISPLAY_NAME);
        assertThat(testAttributeName.getPdpDisplayName()).isEqualTo(UPDATED_PDP_DISPLAY_NAME);
        assertThat(testAttributeName.getParentAttributeId()).isEqualTo(UPDATED_PARENT_ATTRIBUTE_ID);
        assertThat(testAttributeName.getDisplayOrder()).isEqualTo(UPDATED_DISPLAY_ORDER);
    }

    @Test
    @Transactional
    void putNonExistingAttributeName() throws Exception {
        int databaseSizeBeforeUpdate = attributeNameRepository.findAll().size();
        attributeName.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttributeNameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, attributeName.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attributeName))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttributeName in the database
        List<AttributeName> attributeNameList = attributeNameRepository.findAll();
        assertThat(attributeNameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAttributeName() throws Exception {
        int databaseSizeBeforeUpdate = attributeNameRepository.findAll().size();
        attributeName.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttributeNameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attributeName))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttributeName in the database
        List<AttributeName> attributeNameList = attributeNameRepository.findAll();
        assertThat(attributeNameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAttributeName() throws Exception {
        int databaseSizeBeforeUpdate = attributeNameRepository.findAll().size();
        attributeName.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttributeNameMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(attributeName)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AttributeName in the database
        List<AttributeName> attributeNameList = attributeNameRepository.findAll();
        assertThat(attributeNameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAttributeNameWithPatch() throws Exception {
        // Initialize the database
        attributeNameRepository.saveAndFlush(attributeName);

        int databaseSizeBeforeUpdate = attributeNameRepository.findAll().size();

        // Update the attributeName using partial update
        AttributeName partialUpdatedAttributeName = new AttributeName();
        partialUpdatedAttributeName.setId(attributeName.getId());

        partialUpdatedAttributeName
            .propertyDescription(UPDATED_PROPERTY_DESCRIPTION)
            .mandetory(UPDATED_MANDETORY)
            .propertyType(UPDATED_PROPERTY_TYPE)
            .pdpDisplayName(UPDATED_PDP_DISPLAY_NAME);

        restAttributeNameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAttributeName.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAttributeName))
            )
            .andExpect(status().isOk());

        // Validate the AttributeName in the database
        List<AttributeName> attributeNameList = attributeNameRepository.findAll();
        assertThat(attributeNameList).hasSize(databaseSizeBeforeUpdate);
        AttributeName testAttributeName = attributeNameList.get(attributeNameList.size() - 1);
        assertThat(testAttributeName.getPropertyName()).isEqualTo(DEFAULT_PROPERTY_NAME);
        assertThat(testAttributeName.getPropertyDescription()).isEqualTo(UPDATED_PROPERTY_DESCRIPTION);
        assertThat(testAttributeName.getMandetory()).isEqualTo(UPDATED_MANDETORY);
        assertThat(testAttributeName.getPropertyType()).isEqualTo(UPDATED_PROPERTY_TYPE);
        assertThat(testAttributeName.getPlpDisplayName()).isEqualTo(DEFAULT_PLP_DISPLAY_NAME);
        assertThat(testAttributeName.getPdpDisplayName()).isEqualTo(UPDATED_PDP_DISPLAY_NAME);
        assertThat(testAttributeName.getParentAttributeId()).isEqualTo(DEFAULT_PARENT_ATTRIBUTE_ID);
        assertThat(testAttributeName.getDisplayOrder()).isEqualTo(DEFAULT_DISPLAY_ORDER);
    }

    @Test
    @Transactional
    void fullUpdateAttributeNameWithPatch() throws Exception {
        // Initialize the database
        attributeNameRepository.saveAndFlush(attributeName);

        int databaseSizeBeforeUpdate = attributeNameRepository.findAll().size();

        // Update the attributeName using partial update
        AttributeName partialUpdatedAttributeName = new AttributeName();
        partialUpdatedAttributeName.setId(attributeName.getId());

        partialUpdatedAttributeName
            .propertyName(UPDATED_PROPERTY_NAME)
            .propertyDescription(UPDATED_PROPERTY_DESCRIPTION)
            .mandetory(UPDATED_MANDETORY)
            .propertyType(UPDATED_PROPERTY_TYPE)
            .plpDisplayName(UPDATED_PLP_DISPLAY_NAME)
            .pdpDisplayName(UPDATED_PDP_DISPLAY_NAME)
            .parentAttributeId(UPDATED_PARENT_ATTRIBUTE_ID)
            .displayOrder(UPDATED_DISPLAY_ORDER);

        restAttributeNameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAttributeName.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAttributeName))
            )
            .andExpect(status().isOk());

        // Validate the AttributeName in the database
        List<AttributeName> attributeNameList = attributeNameRepository.findAll();
        assertThat(attributeNameList).hasSize(databaseSizeBeforeUpdate);
        AttributeName testAttributeName = attributeNameList.get(attributeNameList.size() - 1);
        assertThat(testAttributeName.getPropertyName()).isEqualTo(UPDATED_PROPERTY_NAME);
        assertThat(testAttributeName.getPropertyDescription()).isEqualTo(UPDATED_PROPERTY_DESCRIPTION);
        assertThat(testAttributeName.getMandetory()).isEqualTo(UPDATED_MANDETORY);
        assertThat(testAttributeName.getPropertyType()).isEqualTo(UPDATED_PROPERTY_TYPE);
        assertThat(testAttributeName.getPlpDisplayName()).isEqualTo(UPDATED_PLP_DISPLAY_NAME);
        assertThat(testAttributeName.getPdpDisplayName()).isEqualTo(UPDATED_PDP_DISPLAY_NAME);
        assertThat(testAttributeName.getParentAttributeId()).isEqualTo(UPDATED_PARENT_ATTRIBUTE_ID);
        assertThat(testAttributeName.getDisplayOrder()).isEqualTo(UPDATED_DISPLAY_ORDER);
    }

    @Test
    @Transactional
    void patchNonExistingAttributeName() throws Exception {
        int databaseSizeBeforeUpdate = attributeNameRepository.findAll().size();
        attributeName.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttributeNameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, attributeName.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(attributeName))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttributeName in the database
        List<AttributeName> attributeNameList = attributeNameRepository.findAll();
        assertThat(attributeNameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAttributeName() throws Exception {
        int databaseSizeBeforeUpdate = attributeNameRepository.findAll().size();
        attributeName.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttributeNameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(attributeName))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttributeName in the database
        List<AttributeName> attributeNameList = attributeNameRepository.findAll();
        assertThat(attributeNameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAttributeName() throws Exception {
        int databaseSizeBeforeUpdate = attributeNameRepository.findAll().size();
        attributeName.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttributeNameMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(attributeName))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AttributeName in the database
        List<AttributeName> attributeNameList = attributeNameRepository.findAll();
        assertThat(attributeNameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAttributeName() throws Exception {
        // Initialize the database
        attributeNameRepository.saveAndFlush(attributeName);

        int databaseSizeBeforeDelete = attributeNameRepository.findAll().size();

        // Delete the attributeName
        restAttributeNameMockMvc
            .perform(delete(ENTITY_API_URL_ID, attributeName.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AttributeName> attributeNameList = attributeNameRepository.findAll();
        assertThat(attributeNameList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
