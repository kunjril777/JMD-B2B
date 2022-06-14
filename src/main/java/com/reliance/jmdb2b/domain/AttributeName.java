package com.reliance.jmdb2b.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.reliance.jmdb2b.domain.enumeration.PROPERTYTYPE;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A AttributeName.
 */
@Entity
@Table(name = "attribute_name")
public class AttributeName implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "property_name")
    private String propertyName;

    @Column(name = "property_description")
    private String propertyDescription;

    @Column(name = "mandetory")
    private Boolean mandetory;

    @Enumerated(EnumType.STRING)
    @Column(name = "property_type")
    private PROPERTYTYPE propertyType;

    @Column(name = "plp_display_name")
    private String plpDisplayName;

    @Column(name = "pdp_display_name")
    private String pdpDisplayName;

    @Column(name = "parent_attribute_id")
    private Long parentAttributeId;

    @Column(name = "display_order")
    private Long displayOrder;

    @OneToMany(mappedBy = "attributeName")
    @JsonIgnoreProperties(value = { "attributeName", "productAttribute" }, allowSetters = true)
    private Set<AttributeValue> attributeNames = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AttributeName id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPropertyName() {
        return this.propertyName;
    }

    public AttributeName propertyName(String propertyName) {
        this.setPropertyName(propertyName);
        return this;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyDescription() {
        return this.propertyDescription;
    }

    public AttributeName propertyDescription(String propertyDescription) {
        this.setPropertyDescription(propertyDescription);
        return this;
    }

    public void setPropertyDescription(String propertyDescription) {
        this.propertyDescription = propertyDescription;
    }

    public Boolean getMandetory() {
        return this.mandetory;
    }

    public AttributeName mandetory(Boolean mandetory) {
        this.setMandetory(mandetory);
        return this;
    }

    public void setMandetory(Boolean mandetory) {
        this.mandetory = mandetory;
    }

    public PROPERTYTYPE getPropertyType() {
        return this.propertyType;
    }

    public AttributeName propertyType(PROPERTYTYPE propertyType) {
        this.setPropertyType(propertyType);
        return this;
    }

    public void setPropertyType(PROPERTYTYPE propertyType) {
        this.propertyType = propertyType;
    }

    public String getPlpDisplayName() {
        return this.plpDisplayName;
    }

    public AttributeName plpDisplayName(String plpDisplayName) {
        this.setPlpDisplayName(plpDisplayName);
        return this;
    }

    public void setPlpDisplayName(String plpDisplayName) {
        this.plpDisplayName = plpDisplayName;
    }

    public String getPdpDisplayName() {
        return this.pdpDisplayName;
    }

    public AttributeName pdpDisplayName(String pdpDisplayName) {
        this.setPdpDisplayName(pdpDisplayName);
        return this;
    }

    public void setPdpDisplayName(String pdpDisplayName) {
        this.pdpDisplayName = pdpDisplayName;
    }

    public Long getParentAttributeId() {
        return this.parentAttributeId;
    }

    public AttributeName parentAttributeId(Long parentAttributeId) {
        this.setParentAttributeId(parentAttributeId);
        return this;
    }

    public void setParentAttributeId(Long parentAttributeId) {
        this.parentAttributeId = parentAttributeId;
    }

    public Long getDisplayOrder() {
        return this.displayOrder;
    }

    public AttributeName displayOrder(Long displayOrder) {
        this.setDisplayOrder(displayOrder);
        return this;
    }

    public void setDisplayOrder(Long displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Set<AttributeValue> getAttributeNames() {
        return this.attributeNames;
    }

    public void setAttributeNames(Set<AttributeValue> attributeValues) {
        if (this.attributeNames != null) {
            this.attributeNames.forEach(i -> i.setAttributeName(null));
        }
        if (attributeValues != null) {
            attributeValues.forEach(i -> i.setAttributeName(this));
        }
        this.attributeNames = attributeValues;
    }

    public AttributeName attributeNames(Set<AttributeValue> attributeValues) {
        this.setAttributeNames(attributeValues);
        return this;
    }

    public AttributeName addAttributeName(AttributeValue attributeValue) {
        this.attributeNames.add(attributeValue);
        attributeValue.setAttributeName(this);
        return this;
    }

    public AttributeName removeAttributeName(AttributeValue attributeValue) {
        this.attributeNames.remove(attributeValue);
        attributeValue.setAttributeName(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AttributeName)) {
            return false;
        }
        return id != null && id.equals(((AttributeName) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AttributeName{" +
            "id=" + getId() +
            ", propertyName='" + getPropertyName() + "'" +
            ", propertyDescription='" + getPropertyDescription() + "'" +
            ", mandetory='" + getMandetory() + "'" +
            ", propertyType='" + getPropertyType() + "'" +
            ", plpDisplayName='" + getPlpDisplayName() + "'" +
            ", pdpDisplayName='" + getPdpDisplayName() + "'" +
            ", parentAttributeId=" + getParentAttributeId() +
            ", displayOrder=" + getDisplayOrder() +
            "}";
    }
}
