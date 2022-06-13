package com.reliance.jmdb2b.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;

/**
 * A DealsOfTheDay.
 */
@Entity
@Table(name = "deals_of_the_day")
public class DealsOfTheDay implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "deal_start_time")
    private ZonedDateTime dealStartTime;

    @Column(name = "deal_end_time")
    private ZonedDateTime dealEndTime;

    @Column(name = "priority")
    private Integer priority;

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

    public DealsOfTheDay id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return this.productId;
    }

    public DealsOfTheDay productId(Long productId) {
        this.setProductId(productId);
        return this;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getCategoryId() {
        return this.categoryId;
    }

    public DealsOfTheDay categoryId(Long categoryId) {
        this.setCategoryId(categoryId);
        return this;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public ZonedDateTime getDealStartTime() {
        return this.dealStartTime;
    }

    public DealsOfTheDay dealStartTime(ZonedDateTime dealStartTime) {
        this.setDealStartTime(dealStartTime);
        return this;
    }

    public void setDealStartTime(ZonedDateTime dealStartTime) {
        this.dealStartTime = dealStartTime;
    }

    public ZonedDateTime getDealEndTime() {
        return this.dealEndTime;
    }

    public DealsOfTheDay dealEndTime(ZonedDateTime dealEndTime) {
        this.setDealEndTime(dealEndTime);
        return this;
    }

    public void setDealEndTime(ZonedDateTime dealEndTime) {
        this.dealEndTime = dealEndTime;
    }

    public Integer getPriority() {
        return this.priority;
    }

    public DealsOfTheDay priority(Integer priority) {
        this.setPriority(priority);
        return this;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public ZonedDateTime getCreatedTime() {
        return this.createdTime;
    }

    public DealsOfTheDay createdTime(ZonedDateTime createdTime) {
        this.setCreatedTime(createdTime);
        return this;
    }

    public void setCreatedTime(ZonedDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public ZonedDateTime getUpdatedTime() {
        return this.updatedTime;
    }

    public DealsOfTheDay updatedTime(ZonedDateTime updatedTime) {
        this.setUpdatedTime(updatedTime);
        return this;
    }

    public void setUpdatedTime(ZonedDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public DealsOfTheDay createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public DealsOfTheDay updatedBy(String updatedBy) {
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
        if (!(o instanceof DealsOfTheDay)) {
            return false;
        }
        return id != null && id.equals(((DealsOfTheDay) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DealsOfTheDay{" +
            "id=" + getId() +
            ", productId=" + getProductId() +
            ", categoryId=" + getCategoryId() +
            ", dealStartTime='" + getDealStartTime() + "'" +
            ", dealEndTime='" + getDealEndTime() + "'" +
            ", priority=" + getPriority() +
            ", createdTime='" + getCreatedTime() + "'" +
            ", updatedTime='" + getUpdatedTime() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            "}";
    }
}
