package com.reliance.jmdb2b.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;

/**
 * A ItemsBackInStock.
 */
@Entity
@Table(name = "items_back_in_stock")
public class ItemsBackInStock implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "stock_status")
    private Boolean stockStatus;

    @Column(name = "status_update_time")
    private ZonedDateTime statusUpdateTime;

    @Column(name = "created_time")
    private ZonedDateTime createdTime;

    @Column(name = "updated_time")
    private ZonedDateTime updatedTime;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ItemsBackInStock id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return this.userId;
    }

    public ItemsBackInStock userId(Long userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return this.productId;
    }

    public ItemsBackInStock productId(Long productId) {
        this.setProductId(productId);
        return this;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Boolean getStockStatus() {
        return this.stockStatus;
    }

    public ItemsBackInStock stockStatus(Boolean stockStatus) {
        this.setStockStatus(stockStatus);
        return this;
    }

    public void setStockStatus(Boolean stockStatus) {
        this.stockStatus = stockStatus;
    }

    public ZonedDateTime getStatusUpdateTime() {
        return this.statusUpdateTime;
    }

    public ItemsBackInStock statusUpdateTime(ZonedDateTime statusUpdateTime) {
        this.setStatusUpdateTime(statusUpdateTime);
        return this;
    }

    public void setStatusUpdateTime(ZonedDateTime statusUpdateTime) {
        this.statusUpdateTime = statusUpdateTime;
    }

    public ZonedDateTime getCreatedTime() {
        return this.createdTime;
    }

    public ItemsBackInStock createdTime(ZonedDateTime createdTime) {
        this.setCreatedTime(createdTime);
        return this;
    }

    public void setCreatedTime(ZonedDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public ZonedDateTime getUpdatedTime() {
        return this.updatedTime;
    }

    public ItemsBackInStock updatedTime(ZonedDateTime updatedTime) {
        this.setUpdatedTime(updatedTime);
        return this;
    }

    public void setUpdatedTime(ZonedDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public ItemsBackInStock createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public ItemsBackInStock updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemsBackInStock)) {
            return false;
        }
        return id != null && id.equals(((ItemsBackInStock) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ItemsBackInStock{" +
            "id=" + getId() +
            ", userId=" + getUserId() +
            ", productId=" + getProductId() +
            ", stockStatus='" + getStockStatus() + "'" +
            ", statusUpdateTime='" + getStatusUpdateTime() + "'" +
            ", createdTime='" + getCreatedTime() + "'" +
            ", updatedTime='" + getUpdatedTime() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            "}";
    }
}
