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

    @JsonIgnoreProperties(value = { "ledgerNledgerLogs" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Ledger userB2BLedger;

    @OneToMany(mappedBy = "userB2B")
    @JsonIgnoreProperties(value = { "cartTransactioncartProducts", "userB2B" }, allowSetters = true)
    private Set<CartTransaction> userB2BcarTransactions = new HashSet<>();

    @OneToMany(mappedBy = "userB2B")
    @JsonIgnoreProperties(value = { "userB2B" }, allowSetters = true)
    private Set<Address> userB2Baddresses = new HashSet<>();

    @OneToMany(mappedBy = "userB2B")
    @JsonIgnoreProperties(value = { "wishlistNwishListProducts", "userB2B" }, allowSetters = true)
    private Set<WishList> userB2BwishLists = new HashSet<>();

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

    public Ledger getUserB2BLedger() {
        return this.userB2BLedger;
    }

    public void setUserB2BLedger(Ledger ledger) {
        this.userB2BLedger = ledger;
    }

    public UserB2B userB2BLedger(Ledger ledger) {
        this.setUserB2BLedger(ledger);
        return this;
    }

    public Set<CartTransaction> getUserB2BcarTransactions() {
        return this.userB2BcarTransactions;
    }

    public void setUserB2BcarTransactions(Set<CartTransaction> cartTransactions) {
        if (this.userB2BcarTransactions != null) {
            this.userB2BcarTransactions.forEach(i -> i.setUserB2B(null));
        }
        if (cartTransactions != null) {
            cartTransactions.forEach(i -> i.setUserB2B(this));
        }
        this.userB2BcarTransactions = cartTransactions;
    }

    public UserB2B userB2BcarTransactions(Set<CartTransaction> cartTransactions) {
        this.setUserB2BcarTransactions(cartTransactions);
        return this;
    }

    public UserB2B addUserB2BcarTransaction(CartTransaction cartTransaction) {
        this.userB2BcarTransactions.add(cartTransaction);
        cartTransaction.setUserB2B(this);
        return this;
    }

    public UserB2B removeUserB2BcarTransaction(CartTransaction cartTransaction) {
        this.userB2BcarTransactions.remove(cartTransaction);
        cartTransaction.setUserB2B(null);
        return this;
    }

    public Set<Address> getUserB2Baddresses() {
        return this.userB2Baddresses;
    }

    public void setUserB2Baddresses(Set<Address> addresses) {
        if (this.userB2Baddresses != null) {
            this.userB2Baddresses.forEach(i -> i.setUserB2B(null));
        }
        if (addresses != null) {
            addresses.forEach(i -> i.setUserB2B(this));
        }
        this.userB2Baddresses = addresses;
    }

    public UserB2B userB2Baddresses(Set<Address> addresses) {
        this.setUserB2Baddresses(addresses);
        return this;
    }

    public UserB2B addUserB2Baddress(Address address) {
        this.userB2Baddresses.add(address);
        address.setUserB2B(this);
        return this;
    }

    public UserB2B removeUserB2Baddress(Address address) {
        this.userB2Baddresses.remove(address);
        address.setUserB2B(null);
        return this;
    }

    public Set<WishList> getUserB2BwishLists() {
        return this.userB2BwishLists;
    }

    public void setUserB2BwishLists(Set<WishList> wishLists) {
        if (this.userB2BwishLists != null) {
            this.userB2BwishLists.forEach(i -> i.setUserB2B(null));
        }
        if (wishLists != null) {
            wishLists.forEach(i -> i.setUserB2B(this));
        }
        this.userB2BwishLists = wishLists;
    }

    public UserB2B userB2BwishLists(Set<WishList> wishLists) {
        this.setUserB2BwishLists(wishLists);
        return this;
    }

    public UserB2B addUserB2BwishList(WishList wishList) {
        this.userB2BwishLists.add(wishList);
        wishList.setUserB2B(this);
        return this;
    }

    public UserB2B removeUserB2BwishList(WishList wishList) {
        this.userB2BwishLists.remove(wishList);
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
