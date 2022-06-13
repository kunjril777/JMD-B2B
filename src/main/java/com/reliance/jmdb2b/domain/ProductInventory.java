package com.reliance.jmdb2b.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;

/**
 * A ProductInventory.
 */
@Entity
@Table(name = "product_inventory")
public class ProductInventory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "location")
    private Long location;

    @Column(name = "stock_quantity")
    private Long stockQuantity;

    @Column(name = "stock_status")
    private Boolean stockStatus;

    @Column(name = "created_time")
    private ZonedDateTime createdTime;

    @Column(name = "updated_time")
    private ZonedDateTime updatedTime;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @ManyToOne
    @JsonIgnoreProperties(value = { "productVariants", "product" }, allowSetters = true)
    private ProductVariant productVariant;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProductInventory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLocation() {
        return this.location;
    }

    public ProductInventory location(Long location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(Long location) {
        this.location = location;
    }

    public Long getStockQuantity() {
        return this.stockQuantity;
    }

    public ProductInventory stockQuantity(Long stockQuantity) {
        this.setStockQuantity(stockQuantity);
        return this;
    }

    public void setStockQuantity(Long stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Boolean getStockStatus() {
        return this.stockStatus;
    }

    public ProductInventory stockStatus(Boolean stockStatus) {
        this.setStockStatus(stockStatus);
        return this;
    }

    public void setStockStatus(Boolean stockStatus) {
        this.stockStatus = stockStatus;
    }

    public ZonedDateTime getCreatedTime() {
        return this.createdTime;
    }

    public ProductInventory createdTime(ZonedDateTime createdTime) {
        this.setCreatedTime(createdTime);
        return this;
    }

    public void setCreatedTime(ZonedDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public ZonedDateTime getUpdatedTime() {
        return this.updatedTime;
    }

    public ProductInventory updatedTime(ZonedDateTime updatedTime) {
        this.setUpdatedTime(updatedTime);
        return this;
    }

    public void setUpdatedTime(ZonedDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public ProductInventory createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public ProductInventory updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public ProductVariant getProductVariant() {
        return this.productVariant;
    }

    public void setProductVariant(ProductVariant productVariant) {
        this.productVariant = productVariant;
    }

    public ProductInventory productVariant(ProductVariant productVariant) {
        this.setProductVariant(productVariant);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductInventory)) {
            return false;
        }
        return id != null && id.equals(((ProductInventory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductInventory{" +
            "id=" + getId() +
            ", location=" + getLocation() +
            ", stockQuantity=" + getStockQuantity() +
            ", stockStatus='" + getStockStatus() + "'" +
            ", createdTime='" + getCreatedTime() + "'" +
            ", updatedTime='" + getUpdatedTime() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            "}";
    }
}
