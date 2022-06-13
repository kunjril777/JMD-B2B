package com.reliance.jmdb2b.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A UserB2B.
 */
@Entity
@Table(name = "user_b_2_b")
public class UserB2B implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "agent_id", nullable = false)
    private String agentId;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

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

    @JsonIgnoreProperties(value = { "ledgers" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    @OneToMany(mappedBy = "userB2B")
    @OneToMany(mappedBy = "userB2B")
    @OneToMany(mappedBy = "userB2B")
    private Ledger userB2B;

    @JsonIgnoreProperties(value = { "ledgers" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    @OneToMany(mappedBy = "userB2B")
    @OneToMany(mappedBy = "userB2B")
    @OneToMany(mappedBy = "userB2B")
    @JsonIgnoreProperties(value = { "cartTransactions", "userB2B" }, allowSetters = true)
    private Set<CartTransaction> userB2BS = new HashSet<>();

    @JsonIgnoreProperties(value = { "ledgers" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    @OneToMany(mappedBy = "userB2B")
    @OneToMany(mappedBy = "userB2B")
    @OneToMany(mappedBy = "userB2B")
    @JsonIgnoreProperties(value = { "userB2B" }, allowSetters = true)
    private Set<Address> userB2BS = new HashSet<>();

    @JsonIgnoreProperties(value = { "ledgers" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    @OneToMany(mappedBy = "userB2B")
    @OneToMany(mappedBy = "userB2B")
    @OneToMany(mappedBy = "userB2B")
    @JsonIgnoreProperties(value = { "wishlists", "userB2B" }, allowSetters = true)
    private Set<WishList> userB2BS = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserB2B id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAgentId() {
        return this.agentId;
    }

    public UserB2B agentId(String agentId) {
        this.setAgentId(agentId);
        return this;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getEmail() {
        return this.email;
    }

    public UserB2B email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public UserB2B password(String password) {
        this.setPassword(password);
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ZonedDateTime getCreatedTime() {
        return this.createdTime;
    }

    public UserB2B createdTime(ZonedDateTime createdTime) {
        this.setCreatedTime(createdTime);
        return this;
    }

    public void setCreatedTime(ZonedDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public ZonedDateTime getUpdatedTime() {
        return this.updatedTime;
    }

    public UserB2B updatedTime(ZonedDateTime updatedTime) {
        this.setUpdatedTime(updatedTime);
        return this;
    }

    public void setUpdatedTime(ZonedDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public UserB2B createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public UserB2B updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Ledger getUserB2B() {
        return this.userB2B;
    }

    public void setUserB2B(Ledger ledger) {
        this.userB2B = ledger;
    }

    public UserB2B userB2B(Ledger ledger) {
        this.setUserB2B(ledger);
        return this;
    }

    public Set<CartTransaction> getUserB2BS() {
        return this.userB2BS;
    }

    public void setUserB2BS(Set<CartTransaction> cartTransactions) {
        if (this.userB2BS != null) {
            this.userB2BS.forEach(i -> i.setUserB2B(null));
        }
        if (cartTransactions != null) {
            cartTransactions.forEach(i -> i.setUserB2B(this));
        }
        this.userB2BS = cartTransactions;
    }

    public UserB2B userB2BS(Set<CartTransaction> cartTransactions) {
        this.setUserB2BS(cartTransactions);
        return this;
    }

    public UserB2B addUserB2B(CartTransaction cartTransaction) {
        this.userB2BS.add(cartTransaction);
        cartTransaction.setUserB2B(this);
        return this;
    }

    public UserB2B removeUserB2B(CartTransaction cartTransaction) {
        this.userB2BS.remove(cartTransaction);
        cartTransaction.setUserB2B(null);
        return this;
    }

    public Set<Address> getUserB2BS() {
        return this.userB2BS;
    }

    public void setUserB2BS(Set<Address> addresses) {
        if (this.userB2BS != null) {
            this.userB2BS.forEach(i -> i.setUserB2B(null));
        }
        if (addresses != null) {
            addresses.forEach(i -> i.setUserB2B(this));
        }
        this.userB2BS = addresses;
    }

    public UserB2B userB2BS(Set<Address> addresses) {
        this.setUserB2BS(addresses);
        return this;
    }

    public UserB2B addUserB2B(Address address) {
        this.userB2BS.add(address);
        address.setUserB2B(this);
        return this;
    }

    public UserB2B removeUserB2B(Address address) {
        this.userB2BS.remove(address);
        address.setUserB2B(null);
        return this;
    }

    public Set<WishList> getUserB2BS() {
        return this.userB2BS;
    }

    public void setUserB2BS(Set<WishList> wishLists) {
        if (this.userB2BS != null) {
            this.userB2BS.forEach(i -> i.setUserB2B(null));
        }
        if (wishLists != null) {
            wishLists.forEach(i -> i.setUserB2B(this));
        }
        this.userB2BS = wishLists;
    }

    public UserB2B userB2BS(Set<WishList> wishLists) {
        this.setUserB2BS(wishLists);
        return this;
    }

    public UserB2B addUserB2B(WishList wishList) {
        this.userB2BS.add(wishList);
        wishList.setUserB2B(this);
        return this;
    }

    public UserB2B removeUserB2B(WishList wishList) {
        this.userB2BS.remove(wishList);
        wishList.setUserB2B(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserB2B)) {
            return false;
        }
        return id != null && id.equals(((UserB2B) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserB2B{" +
            "id=" + getId() +
            ", agentId='" + getAgentId() + "'" +
            ", email='" + getEmail() + "'" +
            ", password='" + getPassword() + "'" +
            ", createdTime='" + getCreatedTime() + "'" +
            ", updatedTime='" + getUpdatedTime() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            "}";
    }
}
