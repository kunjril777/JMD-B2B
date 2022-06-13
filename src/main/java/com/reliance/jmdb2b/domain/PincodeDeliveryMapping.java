package com.reliance.jmdb2b.domain;

import com.reliance.jmdb2b.domain.enumeration.WEIGHT;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;

/**
 * A PincodeDeliveryMapping.
 */
@Entity
@Table(name = "pincode_delivery_mapping")
public class PincodeDeliveryMapping implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "origin_location")
    private Long originLocation;

    @Column(name = "delivery_pincode")
    private Long deliveryPincode;

    @Enumerated(EnumType.STRING)
    @Column(name = "weight")
    private WEIGHT weight;

    @Column(name = "deliverable")
    private Boolean deliverable;

    @Column(name = "num_of_days_to_delivery")
    private ZonedDateTime numOfDaysToDelivery;

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

    public PincodeDeliveryMapping id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOriginLocation() {
        return this.originLocation;
    }

    public PincodeDeliveryMapping originLocation(Long originLocation) {
        this.setOriginLocation(originLocation);
        return this;
    }

    public void setOriginLocation(Long originLocation) {
        this.originLocation = originLocation;
    }

    public Long getDeliveryPincode() {
        return this.deliveryPincode;
    }

    public PincodeDeliveryMapping deliveryPincode(Long deliveryPincode) {
        this.setDeliveryPincode(deliveryPincode);
        return this;
    }

    public void setDeliveryPincode(Long deliveryPincode) {
        this.deliveryPincode = deliveryPincode;
    }

    public WEIGHT getWeight() {
        return this.weight;
    }

    public PincodeDeliveryMapping weight(WEIGHT weight) {
        this.setWeight(weight);
        return this;
    }

    public void setWeight(WEIGHT weight) {
        this.weight = weight;
    }

    public Boolean getDeliverable() {
        return this.deliverable;
    }

    public PincodeDeliveryMapping deliverable(Boolean deliverable) {
        this.setDeliverable(deliverable);
        return this;
    }

    public void setDeliverable(Boolean deliverable) {
        this.deliverable = deliverable;
    }

    public ZonedDateTime getNumOfDaysToDelivery() {
        return this.numOfDaysToDelivery;
    }

    public PincodeDeliveryMapping numOfDaysToDelivery(ZonedDateTime numOfDaysToDelivery) {
        this.setNumOfDaysToDelivery(numOfDaysToDelivery);
        return this;
    }

    public void setNumOfDaysToDelivery(ZonedDateTime numOfDaysToDelivery) {
        this.numOfDaysToDelivery = numOfDaysToDelivery;
    }

    public ZonedDateTime getCreatedTime() {
        return this.createdTime;
    }

    public PincodeDeliveryMapping createdTime(ZonedDateTime createdTime) {
        this.setCreatedTime(createdTime);
        return this;
    }

    public void setCreatedTime(ZonedDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public ZonedDateTime getUpdatedTime() {
        return this.updatedTime;
    }

    public PincodeDeliveryMapping updatedTime(ZonedDateTime updatedTime) {
        this.setUpdatedTime(updatedTime);
        return this;
    }

    public void setUpdatedTime(ZonedDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public PincodeDeliveryMapping createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public PincodeDeliveryMapping updatedBy(String updatedBy) {
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
        if (!(o instanceof PincodeDeliveryMapping)) {
            return false;
        }
        return id != null && id.equals(((PincodeDeliveryMapping) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PincodeDeliveryMapping{" +
            "id=" + getId() +
            ", originLocation=" + getOriginLocation() +
            ", deliveryPincode=" + getDeliveryPincode() +
            ", weight='" + getWeight() + "'" +
            ", deliverable='" + getDeliverable() + "'" +
            ", numOfDaysToDelivery='" + getNumOfDaysToDelivery() + "'" +
            ", createdTime='" + getCreatedTime() + "'" +
            ", updatedTime='" + getUpdatedTime() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            "}";
    }
}
