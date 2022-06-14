package com.reliance.jmdb2b.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A AttributeValue.
 */
@Entity
@Table(name = "attribute_value")
public class AttributeValue implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "property_value")
    private String propertyValue;

    @Column(name = "property_description")
    private String propertyDescription;

    @Column(name = "plp_display_name")
    private String plpDisplayName;

    @Column(name = "pdp_display_name")
    private String pdpDisplayName;

    @Column(name = "display_order")
    private Long displayOrder;

    @ManyToOne
    @JsonIgnoreProperties(value = { "attributeNames" }, allowSetters = true)
    private AttributeName attributeName;

    @ManyToOne
    @JsonIgnoreProperties(value = { "productAttributes" }, allowSetters = true)
    private ProductAttribute productAttribute;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AttributeValue id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPropertyValue() {
        return this.propertyValue;
    }

    public AttributeValue propertyValue(String propertyValue) {
        this.setPropertyValue(propertyValue);
        return this;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    public String getPropertyDescription() {
        return this.propertyDescription;
    }

    public AttributeValue propertyDescription(String propertyDescription) {
        this.setPropertyDescription(propertyDescription);
        return this;
    }

    public void setPropertyDescription(String propertyDescription) {
        this.propertyDescription = propertyDescription;
    }

    public String getPlpDisplayName() {
        return this.plpDisplayName;
    }

    public AttributeValue plpDisplayName(String plpDisplayName) {
        this.setPlpDisplayName(plpDisplayName);
        return this;
    }

    public void setPlpDisplayName(String plpDisplayName) {
        this.plpDisplayName = plpDisplayName;
    }

    public String getPdpDisplayName() {
        return this.pdpDisplayName;
    }

    public AttributeValue pdpDisplayName(String pdpDisplayName) {
        this.setPdpDisplayName(pdpDisplayName);
        return this;
    }

    public void setPdpDisplayName(String pdpDisplayName) {
        this.pdpDisplayName = pdpDisplayName;
    }

    public Long getDisplayOrder() {
        return this.displayOrder;
    }

    public AttributeValue displayOrder(Long displayOrder) {
        this.setDisplayOrder(displayOrder);
        return this;
    }

    public void setDisplayOrder(Long displayOrder) {
        this.displayOrder = displayOrder;
    }

    public AttributeName getAttributeName() {
        return this.attributeName;
    }

    public void setAttributeName(AttributeName attributeName) {
        this.attributeName = attributeName;
    }

    public AttributeValue attributeName(AttributeName attributeName) {
        this.setAttributeName(attributeName);
        return this;
    }

    public ProductAttribute getProductAttribute() {
        return this.productAttribute;
    }

    public void setProductAttribute(ProductAttribute productAttribute) {
        this.productAttribute = productAttribute;
    }

    public AttributeValue productAttribute(ProductAttribute productAttribute) {
        this.setProductAttribute(productAttribute);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AttributeValue)) {
            return false;
        }
        return id != null && id.equals(((AttributeValue) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AttributeValue{" +
            "id=" + getId() +
            ", propertyValue='" + getPropertyValue() + "'" +
            ", propertyDescription='" + getPropertyDescription() + "'" +
            ", plpDisplayName='" + getPlpDisplayName() + "'" +
            ", pdpDisplayName='" + getPdpDisplayName() + "'" +
            ", displayOrder=" + getDisplayOrder() +
            "}";
    }
}
