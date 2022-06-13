package com.reliance.jmdb2b.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "brand_name")
    private String brandName;

    @NotNull
    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "model_number")
    private String modelNumber;

    @Column(name = "product_status")
    private Boolean productStatus;

    @Column(name = "min_order_quantity")
    private Integer minOrderQuantity;

    @Column(name = "max_order_quantity")
    private Integer maxOrderQuantity;

    @Column(name = "tags")
    private String tags;

    @Column(name = "created_time")
    private ZonedDateTime createdTime;

    @Column(name = "updated_time")
    private ZonedDateTime updatedTime;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @OneToMany(mappedBy = "product")
    @OneToMany(mappedBy = "product")
    @JsonIgnoreProperties(value = { "product" }, allowSetters = true)
    private Set<ProductReview> products = new HashSet<>();

    @OneToMany(mappedBy = "product")
    @OneToMany(mappedBy = "product")
    @JsonIgnoreProperties(value = { "productVariants", "product" }, allowSetters = true)
    private Set<ProductVariant> products = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "category", "categories" }, allowSetters = true)
    private Category category;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Product id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrandName() {
        return this.brandName;
    }

    public Product brandName(String brandName) {
        this.setBrandName(brandName);
        return this;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getProductName() {
        return this.productName;
    }

    public Product productName(String productName) {
        this.setProductName(productName);
        return this;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getModelNumber() {
        return this.modelNumber;
    }

    public Product modelNumber(String modelNumber) {
        this.setModelNumber(modelNumber);
        return this;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public Boolean getProductStatus() {
        return this.productStatus;
    }

    public Product productStatus(Boolean productStatus) {
        this.setProductStatus(productStatus);
        return this;
    }

    public void setProductStatus(Boolean productStatus) {
        this.productStatus = productStatus;
    }

    public Integer getMinOrderQuantity() {
        return this.minOrderQuantity;
    }

    public Product minOrderQuantity(Integer minOrderQuantity) {
        this.setMinOrderQuantity(minOrderQuantity);
        return this;
    }

    public void setMinOrderQuantity(Integer minOrderQuantity) {
        this.minOrderQuantity = minOrderQuantity;
    }

    public Integer getMaxOrderQuantity() {
        return this.maxOrderQuantity;
    }

    public Product maxOrderQuantity(Integer maxOrderQuantity) {
        this.setMaxOrderQuantity(maxOrderQuantity);
        return this;
    }

    public void setMaxOrderQuantity(Integer maxOrderQuantity) {
        this.maxOrderQuantity = maxOrderQuantity;
    }

    public String getTags() {
        return this.tags;
    }

    public Product tags(String tags) {
        this.setTags(tags);
        return this;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public ZonedDateTime getCreatedTime() {
        return this.createdTime;
    }

    public Product createdTime(ZonedDateTime createdTime) {
        this.setCreatedTime(createdTime);
        return this;
    }

    public void setCreatedTime(ZonedDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public ZonedDateTime getUpdatedTime() {
        return this.updatedTime;
    }

    public Product updatedTime(ZonedDateTime updatedTime) {
        this.setUpdatedTime(updatedTime);
        return this;
    }

    public void setUpdatedTime(ZonedDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Product createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public Product updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Set<ProductReview> getProducts() {
        return this.products;
    }

    public void setProducts(Set<ProductReview> productReviews) {
        if (this.products != null) {
            this.products.forEach(i -> i.setProduct(null));
        }
        if (productReviews != null) {
            productReviews.forEach(i -> i.setProduct(this));
        }
        this.products = productReviews;
    }

    public Product products(Set<ProductReview> productReviews) {
        this.setProducts(productReviews);
        return this;
    }

    public Product addProduct(ProductReview productReview) {
        this.products.add(productReview);
        productReview.setProduct(this);
        return this;
    }

    public Product removeProduct(ProductReview productReview) {
        this.products.remove(productReview);
        productReview.setProduct(null);
        return this;
    }

    public Set<ProductVariant> getProducts() {
        return this.products;
    }

    public void setProducts(Set<ProductVariant> productVariants) {
        if (this.products != null) {
            this.products.forEach(i -> i.setProduct(null));
        }
        if (productVariants != null) {
            productVariants.forEach(i -> i.setProduct(this));
        }
        this.products = productVariants;
    }

    public Product products(Set<ProductVariant> productVariants) {
        this.setProducts(productVariants);
        return this;
    }

    public Product addProduct(ProductVariant productVariant) {
        this.products.add(productVariant);
        productVariant.setProduct(this);
        return this;
    }

    public Product removeProduct(ProductVariant productVariant) {
        this.products.remove(productVariant);
        productVariant.setProduct(null);
        return this;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Product category(Category category) {
        this.setCategory(category);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return id != null && id.equals(((Product) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", brandName='" + getBrandName() + "'" +
            ", productName='" + getProductName() + "'" +
            ", modelNumber='" + getModelNumber() + "'" +
            ", productStatus='" + getProductStatus() + "'" +
            ", minOrderQuantity=" + getMinOrderQuantity() +
            ", maxOrderQuantity=" + getMaxOrderQuantity() +
            ", tags='" + getTags() + "'" +
            ", createdTime='" + getCreatedTime() + "'" +
            ", updatedTime='" + getUpdatedTime() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            "}";
    }
}
