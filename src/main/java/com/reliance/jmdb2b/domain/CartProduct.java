package com.reliance.jmdb2b.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;

/**
 * A CartProduct.
 */
@Entity
@Table(name = "cart_product")
public class CartProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "product_variant_id")
    private Long productVariantId;

    @Column(name = "price")
    private Long price;

    @Column(name = "created_time")
    private ZonedDateTime createdTime;

    @Column(name = "updated_time")
    private ZonedDateTime updatedTime;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @ManyToOne
    @JsonIgnoreProperties(value = { "cartTransactioncartProducts", "userB2B" }, allowSetters = true)
    private CartTransaction cartTransaction;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CartProduct id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuantity() {
        return this.quantity;
    }

    public CartProduct quantity(Long quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getProductVariantId() {
        return this.productVariantId;
    }

    public CartProduct productVariantId(Long productVariantId) {
        this.setProductVariantId(productVariantId);
        return this;
    }

    public void setProductVariantId(Long productVariantId) {
        this.productVariantId = productVariantId;
    }

    public Long getPrice() {
        return this.price;
    }

    public CartProduct price(Long price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public ZonedDateTime getCreatedTime() {
        return this.createdTime;
    }

    public CartProduct createdTime(ZonedDateTime createdTime) {
        this.setCreatedTime(createdTime);
        return this;
    }

    public void setCreatedTime(ZonedDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public ZonedDateTime getUpdatedTime() {
        return this.updatedTime;
    }

    public CartProduct updatedTime(ZonedDateTime updatedTime) {
        this.setUpdatedTime(updatedTime);
        return this;
    }

    public void setUpdatedTime(ZonedDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public CartProduct createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public CartProduct updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public CartTransaction getCartTransaction() {
        return this.cartTransaction;
    }

    public void setCartTransaction(CartTransaction cartTransaction) {
        this.cartTransaction = cartTransaction;
    }

    public CartProduct cartTransaction(CartTransaction cartTransaction) {
        this.setCartTransaction(cartTransaction);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CartProduct)) {
            return false;
        }
        return id != null && id.equals(((CartProduct) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CartProduct{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", productVariantId=" + getProductVariantId() +
            ", price=" + getPrice() +
            ", createdTime='" + getCreatedTime() + "'" +
            ", updatedTime='" + getUpdatedTime() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            "}";
    }
}
