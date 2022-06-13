package com.reliance.jmdb2b.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.reliance.jmdb2b.domain.enumeration.ORDERSTATUS;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Order.
 */
@Entity
@Table(name = "jhi_order")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "cart_transaction_id")
    private Long cartTransactionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private ORDERSTATUS orderStatus;

    @Column(name = "order_date")
    private ZonedDateTime orderDate;

    @Column(name = "created_time")
    private ZonedDateTime createdTime;

    @Column(name = "updated_time")
    private ZonedDateTime updatedTime;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @OneToMany(mappedBy = "order")
    @JsonIgnoreProperties(value = { "order" }, allowSetters = true)
    private Set<OrderTrackingLog> orders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Order id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCartTransactionId() {
        return this.cartTransactionId;
    }

    public Order cartTransactionId(Long cartTransactionId) {
        this.setCartTransactionId(cartTransactionId);
        return this;
    }

    public void setCartTransactionId(Long cartTransactionId) {
        this.cartTransactionId = cartTransactionId;
    }

    public ORDERSTATUS getOrderStatus() {
        return this.orderStatus;
    }

    public Order orderStatus(ORDERSTATUS orderStatus) {
        this.setOrderStatus(orderStatus);
        return this;
    }

    public void setOrderStatus(ORDERSTATUS orderStatus) {
        this.orderStatus = orderStatus;
    }

    public ZonedDateTime getOrderDate() {
        return this.orderDate;
    }

    public Order orderDate(ZonedDateTime orderDate) {
        this.setOrderDate(orderDate);
        return this;
    }

    public void setOrderDate(ZonedDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public ZonedDateTime getCreatedTime() {
        return this.createdTime;
    }

    public Order createdTime(ZonedDateTime createdTime) {
        this.setCreatedTime(createdTime);
        return this;
    }

    public void setCreatedTime(ZonedDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public ZonedDateTime getUpdatedTime() {
        return this.updatedTime;
    }

    public Order updatedTime(ZonedDateTime updatedTime) {
        this.setUpdatedTime(updatedTime);
        return this;
    }

    public void setUpdatedTime(ZonedDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Order createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public Order updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Set<OrderTrackingLog> getOrders() {
        return this.orders;
    }

    public void setOrders(Set<OrderTrackingLog> orderTrackingLogs) {
        if (this.orders != null) {
            this.orders.forEach(i -> i.setOrder(null));
        }
        if (orderTrackingLogs != null) {
            orderTrackingLogs.forEach(i -> i.setOrder(this));
        }
        this.orders = orderTrackingLogs;
    }

    public Order orders(Set<OrderTrackingLog> orderTrackingLogs) {
        this.setOrders(orderTrackingLogs);
        return this;
    }

    public Order addOrder(OrderTrackingLog orderTrackingLog) {
        this.orders.add(orderTrackingLog);
        orderTrackingLog.setOrder(this);
        return this;
    }

    public Order removeOrder(OrderTrackingLog orderTrackingLog) {
        this.orders.remove(orderTrackingLog);
        orderTrackingLog.setOrder(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order)) {
            return false;
        }
        return id != null && id.equals(((Order) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Order{" +
            "id=" + getId() +
            ", cartTransactionId=" + getCartTransactionId() +
            ", orderStatus='" + getOrderStatus() + "'" +
            ", orderDate='" + getOrderDate() + "'" +
            ", createdTime='" + getCreatedTime() + "'" +
            ", updatedTime='" + getUpdatedTime() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            "}";
    }
}
