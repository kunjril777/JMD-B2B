package com.reliance.jmdb2b.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Category.
 */
@Entity
@Table(name = "category")
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "parentcategory")
    private Long parentcategory;

    @Column(name = "attributes")
    private String attributes;

    @Column(name = "level")
    private String level;

    @Column(name = "description")
    private String description;

    @Column(name = "category_status")
    private Boolean categoryStatus;

    @JsonIgnoreProperties(value = { "category", "categories" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    @OneToMany(mappedBy = "category")
    private Category category;

    @JsonIgnoreProperties(value = { "category", "categories" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    @OneToMany(mappedBy = "category")
    @JsonIgnoreProperties(value = { "products", "products", "category" }, allowSetters = true)
    private Set<Product> categories = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Category id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Category title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getParentcategory() {
        return this.parentcategory;
    }

    public Category parentcategory(Long parentcategory) {
        this.setParentcategory(parentcategory);
        return this;
    }

    public void setParentcategory(Long parentcategory) {
        this.parentcategory = parentcategory;
    }

    public String getAttributes() {
        return this.attributes;
    }

    public Category attributes(String attributes) {
        this.setAttributes(attributes);
        return this;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public String getLevel() {
        return this.level;
    }

    public Category level(String level) {
        this.setLevel(level);
        return this;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDescription() {
        return this.description;
    }

    public Category description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getCategoryStatus() {
        return this.categoryStatus;
    }

    public Category categoryStatus(Boolean categoryStatus) {
        this.setCategoryStatus(categoryStatus);
        return this;
    }

    public void setCategoryStatus(Boolean categoryStatus) {
        this.categoryStatus = categoryStatus;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Category category(Category category) {
        this.setCategory(category);
        return this;
    }

    public Set<Product> getCategories() {
        return this.categories;
    }

    public void setCategories(Set<Product> products) {
        if (this.categories != null) {
            this.categories.forEach(i -> i.setCategory(null));
        }
        if (products != null) {
            products.forEach(i -> i.setCategory(this));
        }
        this.categories = products;
    }

    public Category categories(Set<Product> products) {
        this.setCategories(products);
        return this;
    }

    public Category addCategory(Product product) {
        this.categories.add(product);
        product.setCategory(this);
        return this;
    }

    public Category removeCategory(Product product) {
        this.categories.remove(product);
        product.setCategory(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Category)) {
            return false;
        }
        return id != null && id.equals(((Category) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Category{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", parentcategory=" + getParentcategory() +
            ", attributes='" + getAttributes() + "'" +
            ", level='" + getLevel() + "'" +
            ", description='" + getDescription() + "'" +
            ", categoryStatus='" + getCategoryStatus() + "'" +
            "}";
    }
}
