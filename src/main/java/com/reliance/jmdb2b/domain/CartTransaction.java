package com.reliance.jmdb2b.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A CartTransaction.
 */
@Entity
@Table(name = "cart_transaction")
public class CartTransaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "cart_total_quantity")
    private Long cartTotalQuantity;

    @Column(name = "cart_total_price")
    private Long cartTotalPrice;

    @Column(name = "billing_address_id")
    private Long billingAddressId;

    @Column(name = "shipping_address_id")
    private Long shippingAddressId;

    @Column(name = "delivery_charge")
    private Double deliveryCharge;

    @Column(name = "coupon_code")
    private String couponCode;

    @Column(name = "cart_final_total")
    private Long cartFinalTotal;

    @Column(name = "created_time")
    private ZonedDateTime createdTime;

    @Column(name = "updated_time")
    private ZonedDateTime updatedTime;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @OneToMany(mappedBy = "cartTransaction")
    @JsonIgnoreProperties(value = { "cartTransaction" }, allowSetters = true)
    private Set<CartProduct> cartTransactioncartProducts = new HashSet<>();

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

    public CartTransaction id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCartTotalQuantity() {
        return this.cartTotalQuantity;
    }

    public CartTransaction cartTotalQuantity(Long cartTotalQuantity) {
        this.setCartTotalQuantity(cartTotalQuantity);
        return this;
    }

    public void setCartTotalQuantity(Long cartTotalQuantity) {
        this.cartTotalQuantity = cartTotalQuantity;
    }

    public Long getCartTotalPrice() {
        return this.cartTotalPrice;
    }

    public CartTransaction cartTotalPrice(Long cartTotalPrice) {
        this.setCartTotalPrice(cartTotalPrice);
        return this;
    }

    public void setCartTotalPrice(Long cartTotalPrice) {
        this.cartTotalPrice = cartTotalPrice;
    }

    public Long getBillingAddressId() {
        return this.billingAddressId;
    }

    public CartTransaction billingAddressId(Long billingAddressId) {
        this.setBillingAddressId(billingAddressId);
        return this;
    }

    public void setBillingAddressId(Long billingAddressId) {
        this.billingAddressId = billingAddressId;
    }

    public Long getShippingAddressId() {
        return this.shippingAddressId;
    }

    public CartTransaction shippingAddressId(Long shippingAddressId) {
        this.setShippingAddressId(shippingAddressId);
        return this;
    }

    public void setShippingAddressId(Long shippingAddressId) {
        this.shippingAddressId = shippingAddressId;
    }

    public Double getDeliveryCharge() {
        return this.deliveryCharge;
    }

    public CartTransaction deliveryCharge(Double deliveryCharge) {
        this.setDeliveryCharge(deliveryCharge);
        return this;
    }

    public void setDeliveryCharge(Double deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public String getCouponCode() {
        return this.couponCode;
    }

    public CartTransaction couponCode(String couponCode) {
        this.setCouponCode(couponCode);
        return this;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public Long getCartFinalTotal() {
        return this.cartFinalTotal;
    }

    public CartTransaction cartFinalTotal(Long cartFinalTotal) {
        this.setCartFinalTotal(cartFinalTotal);
        return this;
    }

    public void setCartFinalTotal(Long cartFinalTotal) {
        this.cartFinalTotal = cartFinalTotal;
    }

    public ZonedDateTime getCreatedTime() {
        return this.createdTime;
    }

    public CartTransaction createdTime(ZonedDateTime createdTime) {
        this.setCreatedTime(createdTime);
        return this;
    }

    public void setCreatedTime(ZonedDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public ZonedDateTime getUpdatedTime() {
        return this.updatedTime;
    }

    public CartTransaction updatedTime(ZonedDateTime updatedTime) {
        this.setUpdatedTime(updatedTime);
        return this;
    }

    public void setUpdatedTime(ZonedDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public CartTransaction createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public CartTransaction updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Set<CartProduct> getCartTransactioncartProducts() {
        return this.cartTransactioncartProducts;
    }

    public void setCartTransactioncartProducts(Set<CartProduct> cartProducts) {
        if (this.cartTransactioncartProducts != null) {
            this.cartTransactioncartProducts.forEach(i -> i.setCartTransaction(null));
        }
        if (cartProducts != null) {
            cartProducts.forEach(i -> i.setCartTransaction(this));
        }
        this.cartTransactioncartProducts = cartProducts;
    }

    public CartTransaction cartTransactioncartProducts(Set<CartProduct> cartProducts) {
        this.setCartTransactioncartProducts(cartProducts);
        return this;
    }

    public CartTransaction addCartTransactioncartProduct(CartProduct cartProduct) {
        this.cartTransactioncartProducts.add(cartProduct);
        cartProduct.setCartTransaction(this);
        return this;
    }

    public CartTransaction removeCartTransactioncartProduct(CartProduct cartProduct) {
        this.cartTransactioncartProducts.remove(cartProduct);
        cartProduct.setCartTransaction(null);
        return this;
    }

    public UserB2B getUserB2B() {
        return this.userB2B;
    }

    public void setUserB2B(UserB2B userB2B) {
        this.userB2B = userB2B;
    }

    public CartTransaction userB2B(UserB2B userB2B) {
        this.setUserB2B(userB2B);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CartTransaction)) {
            return false;
        }
        return id != null && id.equals(((CartTransaction) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CartTransaction{" +
            "id=" + getId() +
            ", cartTotalQuantity=" + getCartTotalQuantity() +
            ", cartTotalPrice=" + getCartTotalPrice() +
            ", billingAddressId=" + getBillingAddressId() +
            ", shippingAddressId=" + getShippingAddressId() +
            ", deliveryCharge=" + getDeliveryCharge() +
            ", couponCode='" + getCouponCode() + "'" +
            ", cartFinalTotal=" + getCartFinalTotal() +
            ", createdTime='" + getCreatedTime() + "'" +
            ", updatedTime='" + getUpdatedTime() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            "}";
    }
}
