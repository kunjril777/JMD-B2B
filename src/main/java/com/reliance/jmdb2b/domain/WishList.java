package com.reliance.jmdb2b.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A WishList.
 */
@Entity
@Table(name = "wish_list")
public class WishList implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "wish_list_name")
    private String wishListName;

    @Column(name = "created_time")
    private ZonedDateTime createdTime;

    @Column(name = "updated_time")
    private ZonedDateTime updatedTime;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @OneToMany(mappedBy = "wishList")
    @JsonIgnoreProperties(value = { "wishList" }, allowSetters = true)
    private Set<WishListProduct> wishlistNwishListProducts = new HashSet<>();

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

    public WishList id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWishListName() {
        return this.wishListName;
    }

    public WishList wishListName(String wishListName) {
        this.setWishListName(wishListName);
        return this;
    }

    public void setWishListName(String wishListName) {
        this.wishListName = wishListName;
    }

    public ZonedDateTime getCreatedTime() {
        return this.createdTime;
    }

    public WishList createdTime(ZonedDateTime createdTime) {
        this.setCreatedTime(createdTime);
        return this;
    }

    public void setCreatedTime(ZonedDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public ZonedDateTime getUpdatedTime() {
        return this.updatedTime;
    }

    public WishList updatedTime(ZonedDateTime updatedTime) {
        this.setUpdatedTime(updatedTime);
        return this;
    }

    public void setUpdatedTime(ZonedDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public WishList createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public WishList updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Set<WishListProduct> getWishlistNwishListProducts() {
        return this.wishlistNwishListProducts;
    }

    public void setWishlistNwishListProducts(Set<WishListProduct> wishListProducts) {
        if (this.wishlistNwishListProducts != null) {
            this.wishlistNwishListProducts.forEach(i -> i.setWishList(null));
        }
        if (wishListProducts != null) {
            wishListProducts.forEach(i -> i.setWishList(this));
        }
        this.wishlistNwishListProducts = wishListProducts;
    }

    public WishList wishlistNwishListProducts(Set<WishListProduct> wishListProducts) {
        this.setWishlistNwishListProducts(wishListProducts);
        return this;
    }

    public WishList addWishlistNwishListProduct(WishListProduct wishListProduct) {
        this.wishlistNwishListProducts.add(wishListProduct);
        wishListProduct.setWishList(this);
        return this;
    }

    public WishList removeWishlistNwishListProduct(WishListProduct wishListProduct) {
        this.wishlistNwishListProducts.remove(wishListProduct);
        wishListProduct.setWishList(null);
        return this;
    }

    public UserB2B getUserB2B() {
        return this.userB2B;
    }

    public void setUserB2B(UserB2B userB2B) {
        this.userB2B = userB2B;
    }

    public WishList userB2B(UserB2B userB2B) {
        this.setUserB2B(userB2B);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WishList)) {
            return false;
        }
        return id != null && id.equals(((WishList) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WishList{" +
            "id=" + getId() +
            ", wishListName='" + getWishListName() + "'" +
            ", createdTime='" + getCreatedTime() + "'" +
            ", updatedTime='" + getUpdatedTime() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            "}";
    }
}
