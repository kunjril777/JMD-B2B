package com.reliance.jmdb2b.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;

/**
 * A TrendingProducts.
 */
@Entity
@Table(name = "trending_products")
public class TrendingProducts implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "sold_quantity")
    private Long soldQuantity;

    @Column(name = "view_count")
    private Long viewCount;

    @Column(name = "sold_date")
    private ZonedDateTime soldDate;

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

    public TrendingProducts id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return this.productId;
    }

    public TrendingProducts productId(Long productId) {
        this.setProductId(productId);
        return this;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getCategoryId() {
        return this.categoryId;
    }

    public TrendingProducts categoryId(Long categoryId) {
        this.setCategoryId(categoryId);
        return this;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getSoldQuantity() {
        return this.soldQuantity;
    }

    public TrendingProducts soldQuantity(Long soldQuantity) {
        this.setSoldQuantity(soldQuantity);
        return this;
    }

    public void setSoldQuantity(Long soldQuantity) {
        this.soldQuantity = soldQuantity;
    }

    public Long getViewCount() {
        return this.viewCount;
    }

    public TrendingProducts viewCount(Long viewCount) {
        this.setViewCount(viewCount);
        return this;
    }

    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }

    public ZonedDateTime getSoldDate() {
        return this.soldDate;
    }

    public TrendingProducts soldDate(ZonedDateTime soldDate) {
        this.setSoldDate(soldDate);
        return this;
    }

    public void setSoldDate(ZonedDateTime soldDate) {
        this.soldDate = soldDate;
    }

    public ZonedDateTime getCreatedTime() {
        return this.createdTime;
    }

    public TrendingProducts createdTime(ZonedDateTime createdTime) {
        this.setCreatedTime(createdTime);
        return this;
    }

    public void setCreatedTime(ZonedDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public ZonedDateTime getUpdatedTime() {
        return this.updatedTime;
    }

    public TrendingProducts updatedTime(ZonedDateTime updatedTime) {
        this.setUpdatedTime(updatedTime);
        return this;
    }

    public void setUpdatedTime(ZonedDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public TrendingProducts createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public TrendingProducts updatedBy(String updatedBy) {
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
        if (!(o instanceof TrendingProducts)) {
            return false;
        }
        return id != null && id.equals(((TrendingProducts) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TrendingProducts{" +
            "id=" + getId() +
            ", productId=" + getProductId() +
            ", categoryId=" + getCategoryId() +
            ", soldQuantity=" + getSoldQuantity() +
            ", viewCount=" + getViewCount() +
            ", soldDate='" + getSoldDate() + "'" +
            ", createdTime='" + getCreatedTime() + "'" +
            ", updatedTime='" + getUpdatedTime() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            "}";
    }
}
