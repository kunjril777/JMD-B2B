package com.reliance.jmdb2b.domain;

import com.reliance.jmdb2b.domain.enumeration.PROPERTYTYPE;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A Attribute.
 */
@Entity
@Table(name = "attribute")
public class Attribute implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "propety_type_id")
    private Long propetyTypeId;

    @Enumerated(EnumType.STRING)
    @Column(name = "property_type")
    private PROPERTYTYPE propertyType;

    @Column(name = "property_name")
    private String propertyName;

    @Column(name = "property_value")
    private String propertyValue;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Attribute id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPropetyTypeId() {
        return this.propetyTypeId;
    }

    public Attribute propetyTypeId(Long propetyTypeId) {
        this.setPropetyTypeId(propetyTypeId);
        return this;
    }

    public void setPropetyTypeId(Long propetyTypeId) {
        this.propetyTypeId = propetyTypeId;
    }

    public PROPERTYTYPE getPropertyType() {
        return this.propertyType;
    }

    public Attribute propertyType(PROPERTYTYPE propertyType) {
        this.setPropertyType(propertyType);
        return this;
    }

    public void setPropertyType(PROPERTYTYPE propertyType) {
        this.propertyType = propertyType;
    }

    public String getPropertyName() {
        return this.propertyName;
    }

    public Attribute propertyName(String propertyName) {
        this.setPropertyName(propertyName);
        return this;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyValue() {
        return this.propertyValue;
    }

    public Attribute propertyValue(String propertyValue) {
        this.setPropertyValue(propertyValue);
        return this;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Attribute)) {
            return false;
        }
        return id != null && id.equals(((Attribute) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Attribute{" +
            "id=" + getId() +
            ", propetyTypeId=" + getPropetyTypeId() +
            ", propertyType='" + getPropertyType() + "'" +
            ", propertyName='" + getPropertyName() + "'" +
            ", propertyValue='" + getPropertyValue() + "'" +
            "}";
    }
}
