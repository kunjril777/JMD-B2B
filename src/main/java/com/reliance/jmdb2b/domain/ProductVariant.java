package com.reliance.jmdb2b.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A ProductVariant.
 */
@Entity
@Table(name = "product_variant")
public class ProductVariant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "product_variant_id")
    private Long productVariantId;

    @NotNull
    @Column(name = "product_price", nullable = false)
    private Float productPrice;

    @Column(name = "deal_price")
    private Float dealPrice;

    @Column(name = "mrp")
    private Float mrp;

    @Column(name = "description")
    private String description;

    @Column(name = "title")
    private String title;

    @Column(name = "images")
    private String images;

    @Column(name = "created_time")
    private ZonedDateTime createdTime;

    @Column(name = "updated_time")
    private ZonedDateTime updatedTime;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @OneToMany(mappedBy = "productVariant")
    @JsonIgnoreProperties(value = { "productVariant" }, allowSetters = true)
    private Set<ProductInventory> productVariantNproductInventories = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "productNproductReviews", "productNproductVariants", "category" }, allowSetters = true)
    private Product product;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProductVariant id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductVariantId() {
        return this.productVariantId;
    }

    public ProductVariant productVariantId(Long productVariantId) {
        this.setProductVariantId(productVariantId);
        return this;
    }

    public void setProductVariantId(Long productVariantId) {
        this.productVariantId = productVariantId;
    }

    public Float getProductPrice() {
        return this.productPrice;
    }

    public ProductVariant productPrice(Float productPrice) {
        this.setProductPrice(productPrice);
        return this;
    }

    public void setProductPrice(Float productPrice) {
        this.productPrice = productPrice;
    }

    public Float getDealPrice() {
        return this.dealPrice;
    }

    public ProductVariant dealPrice(Float dealPrice) {
        this.setDealPrice(dealPrice);
        return this;
    }

    public void setDealPrice(Float dealPrice) {
        this.dealPrice = dealPrice;
    }

    public Float getMrp() {
        return this.mrp;
    }

    public ProductVariant mrp(Float mrp) {
        this.setMrp(mrp);
        return this;
    }

    public void setMrp(Float mrp) {
        this.mrp = mrp;
    }

    public String getDescription() {
        return this.description;
    }

    public ProductVariant description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return this.title;
    }

    public ProductVariant title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImages() {
        return this.images;
    }

    public ProductVariant images(String images) {
        this.setImages(images);
        return this;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public ZonedDateTime getCreatedTime() {
        return this.createdTime;
    }

    public ProductVariant createdTime(ZonedDateTime createdTime) {
        this.setCreatedTime(createdTime);
        return this;
    }

    public void setCreatedTime(ZonedDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public ZonedDateTime getUpdatedTime() {
        return this.updatedTime;
    }

    public ProductVariant updatedTime(ZonedDateTime updatedTime) {
        this.setUpdatedTime(updatedTime);
        return this;
    }

    public void setUpdatedTime(ZonedDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public ProductVariant createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public ProductVariant updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Set<ProductInventory> getProductVariantNproductInventories() {
        return this.productVariantNproductInventories;
    }

    public void setProductVariantNproductInventories(Set<ProductInventory> productInventories) {
        if (this.productVariantNproductInventories != null) {
            this.productVariantNproductInventories.forEach(i -> i.setProductVariant(null));
        }
        if (productInventories != null) {
            productInventories.forEach(i -> i.setProductVariant(this));
        }
        this.productVariantNproductInventories = productInventories;
    }

    public ProductVariant productVariantNproductInventories(Set<ProductInventory> productInventories) {
        this.setProductVariantNproductInventories(productInventories);
        return this;
    }

    public ProductVariant addProductVariantNproductInventory(ProductInventory productInventory) {
        this.productVariantNproductInventories.add(productInventory);
        productInventory.setProductVariant(this);
        return this;
    }

    public ProductVariant removeProductVariantNproductInventory(ProductInventory productInventory) {
        this.productVariantNproductInventories.remove(productInventory);
        productInventory.setProductVariant(null);
        return this;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ProductVariant product(Product product) {
        this.setProduct(product);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductVariant)) {
            return false;
        }
        return id != null && id.equals(((ProductVariant) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductVariant{" +
            "id=" + getId() +
            ", productVariantId=" + getProductVariantId() +
            ", productPrice=" + getProductPrice() +
            ", dealPrice=" + getDealPrice() +
            ", mrp=" + getMrp() +
            ", description='" + getDescription() + "'" +
            ", title='" + getTitle() + "'" +
            ", images='" + getImages() + "'" +
            ", createdTime='" + getCreatedTime() + "'" +
            ", updatedTime='" + getUpdatedTime() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            "}";
    }
}
