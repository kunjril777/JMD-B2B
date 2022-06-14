package com.reliance.jmdb2b.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Address.
 */
@Entity
@Table(name = "address")
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "address_line_1", nullable = false)
    private String addressLine1;

    @Column(name = "address_line_2")
    private String addressLine2;

    @NotNull
    @Column(name = "pincode", nullable = false)
    private Long pincode;

    @NotNull
    @Column(name = "city", nullable = false)
    private String city;

    @NotNull
    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "gst")
    private String gst;

    @NotNull
    @Column(name = "landmark", nullable = false)
    private String landmark;

    @Column(name = "address_status")
    private Boolean addressStatus;

    @NotNull
    @Column(name = "created_time", nullable = false)
    private ZonedDateTime createdTime;

    @NotNull
    @Column(name = "updated_time", nullable = false)
    private ZonedDateTime updatedTime;

    @NotNull
    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @NotNull
    @Column(name = "updated_by", nullable = false)
    private String updatedBy;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "userB2BLedger", "userB2BcarTransactions", "userB2Baddresses", "userB2BwishLists" },
        allowSetters = true
    )
    private UserB2B userB2B;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Address id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Address title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddressLine1() {
        return this.addressLine1;
    }

    public Address addressLine1(String addressLine1) {
        this.setAddressLine1(addressLine1);
        return this;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return this.addressLine2;
    }

    public Address addressLine2(String addressLine2) {
        this.setAddressLine2(addressLine2);
        return this;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public Long getPincode() {
        return this.pincode;
    }

    public Address pincode(Long pincode) {
        this.setPincode(pincode);
        return this;
    }

    public void setPincode(Long pincode) {
        this.pincode = pincode;
    }

    public String getCity() {
        return this.city;
    }

    public Address city(String city) {
        this.setCity(city);
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return this.state;
    }

    public Address state(String state) {
        this.setState(state);
        return this;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getGst() {
        return this.gst;
    }

    public Address gst(String gst) {
        this.setGst(gst);
        return this;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public String getLandmark() {
        return this.landmark;
    }

    public Address landmark(String landmark) {
        this.setLandmark(landmark);
        return this;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public Boolean getAddressStatus() {
        return this.addressStatus;
    }

    public Address addressStatus(Boolean addressStatus) {
        this.setAddressStatus(addressStatus);
        return this;
    }

    public void setAddressStatus(Boolean addressStatus) {
        this.addressStatus = addressStatus;
    }

    public ZonedDateTime getCreatedTime() {
        return this.createdTime;
    }

    public Address createdTime(ZonedDateTime createdTime) {
        this.setCreatedTime(createdTime);
        return this;
    }

    public void setCreatedTime(ZonedDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public ZonedDateTime getUpdatedTime() {
        return this.updatedTime;
    }

    public Address updatedTime(ZonedDateTime updatedTime) {
        this.setUpdatedTime(updatedTime);
        return this;
    }

    public void setUpdatedTime(ZonedDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Address createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public Address updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public UserB2B getUserB2B() {
        return this.userB2B;
    }

    public void setUserB2B(UserB2B userB2B) {
        this.userB2B = userB2B;
    }

    public Address userB2B(UserB2B userB2B) {
        this.setUserB2B(userB2B);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Address)) {
            return false;
        }
        return id != null && id.equals(((Address) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Address{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", addressLine1='" + getAddressLine1() + "'" +
            ", addressLine2='" + getAddressLine2() + "'" +
            ", pincode=" + getPincode() +
            ", city='" + getCity() + "'" +
            ", state='" + getState() + "'" +
            ", gst='" + getGst() + "'" +
            ", landmark='" + getLandmark() + "'" +
            ", addressStatus='" + getAddressStatus() + "'" +
            ", createdTime='" + getCreatedTime() + "'" +
            ", updatedTime='" + getUpdatedTime() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            "}";
    }
}
