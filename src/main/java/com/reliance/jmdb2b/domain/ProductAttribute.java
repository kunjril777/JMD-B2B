package com.reliance.jmdb2b.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A ProductAttribute.
 */
@Entity
@Table(name = "product_attribute")
public class ProductAttribute implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "product_variantid")
    private Long productVariantid;

    @OneToMany(mappedBy = "productAttribute")
    @JsonIgnoreProperties(value = { "attributeName", "productAttribute" }, allowSetters = true)
    private Set<AttributeValue> productAttributes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProductAttribute id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductVariantid() {
        return this.productVariantid;
    }

    public ProductAttribute productVariantid(Long productVariantid) {
        this.setProductVariantid(productVariantid);
        return this;
    }

    public void setProductVariantid(Long productVariantid) {
        this.productVariantid = productVariantid;
    }

    public Set<AttributeValue> getProductAttributes() {
        return this.productAttributes;
    }

    public void setProductAttributes(Set<AttributeValue> attributeValues) {
        if (this.productAttributes != null) {
            this.productAttributes.forEach(i -> i.setProductAttribute(null));
        }
        if (attributeValues != null) {
            attributeValues.forEach(i -> i.setProductAttribute(this));
        }
        this.productAttributes = attributeValues;
    }

    public ProductAttribute productAttributes(Set<AttributeValue> attributeValues) {
        this.setProductAttributes(attributeValues);
        return this;
    }

    public ProductAttribute addProductAttribute(AttributeValue attributeValue) {
        this.productAttributes.add(attributeValue);
        attributeValue.setProductAttribute(this);
        return this;
    }

    public ProductAttribute removeProductAttribute(AttributeValue attributeValue) {
        this.productAttributes.remove(attributeValue);
        attributeValue.setProductAttribute(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductAttribute)) {
            return false;
        }
        return id != null && id.equals(((ProductAttribute) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductAttribute{" +
            "id=" + getId() +
            ", productVariantid=" + getProductVariantid() +
            "}";
    }
}
