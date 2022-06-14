package com.reliance.jmdb2b.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.reliance.jmdb2b.domain.enumeration.CLIENTTRANSACTIONTYPE;
import com.reliance.jmdb2b.domain.enumeration.PAYMENTAGGREGATOR;
import com.reliance.jmdb2b.domain.enumeration.PAYMENTGATEWAY;
import com.reliance.jmdb2b.domain.enumeration.PAYMENTMETHOD;
import com.reliance.jmdb2b.domain.enumeration.PAYMENTSTATUS;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;

/**
 * A Payment.
 */
@Entity
@Table(name = "payment")
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "pg_transaction_id")
    private String pgTransactionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "client_transaction_type")
    private CLIENTTRANSACTIONTYPE clientTransactionType;

    @Column(name = "client_transaction_id")
    private Long clientTransactionId;

    @Column(name = "pg_status")
    private String pgStatus;

    @Column(name = "transactioninit_date")
    private ZonedDateTime transactioninitDate;

    @Column(name = "status_update_date")
    private ZonedDateTime statusUpdateDate;

    @Column(name = "amount")
    private Float amount;

    @Column(name = "redirection_url")
    private String redirectionUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_aggregator")
    private PAYMENTAGGREGATOR paymentAggregator;

    @Enumerated(EnumType.STRING)
    @Column(name = "paymentgateway")
    private PAYMENTGATEWAY paymentgateway;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PAYMENTSTATUS paymentStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PAYMENTMETHOD paymentMethod;

    @Column(name = "created_time")
    private ZonedDateTime createdTime;

    @Column(name = "updated_time")
    private ZonedDateTime updatedTime;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @JsonIgnoreProperties(value = { "orderNorderTrackingLogs" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Order paymentOrder;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Payment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPgTransactionId() {
        return this.pgTransactionId;
    }

    public Payment pgTransactionId(String pgTransactionId) {
        this.setPgTransactionId(pgTransactionId);
        return this;
    }

    public void setPgTransactionId(String pgTransactionId) {
        this.pgTransactionId = pgTransactionId;
    }

    public CLIENTTRANSACTIONTYPE getClientTransactionType() {
        return this.clientTransactionType;
    }

    public Payment clientTransactionType(CLIENTTRANSACTIONTYPE clientTransactionType) {
        this.setClientTransactionType(clientTransactionType);
        return this;
    }

    public void setClientTransactionType(CLIENTTRANSACTIONTYPE clientTransactionType) {
        this.clientTransactionType = clientTransactionType;
    }

    public Long getClientTransactionId() {
        return this.clientTransactionId;
    }

    public Payment clientTransactionId(Long clientTransactionId) {
        this.setClientTransactionId(clientTransactionId);
        return this;
    }

    public void setClientTransactionId(Long clientTransactionId) {
        this.clientTransactionId = clientTransactionId;
    }

    public String getPgStatus() {
        return this.pgStatus;
    }

    public Payment pgStatus(String pgStatus) {
        this.setPgStatus(pgStatus);
        return this;
    }

    public void setPgStatus(String pgStatus) {
        this.pgStatus = pgStatus;
    }

    public ZonedDateTime getTransactioninitDate() {
        return this.transactioninitDate;
    }

    public Payment transactioninitDate(ZonedDateTime transactioninitDate) {
        this.setTransactioninitDate(transactioninitDate);
        return this;
    }

    public void setTransactioninitDate(ZonedDateTime transactioninitDate) {
        this.transactioninitDate = transactioninitDate;
    }

    public ZonedDateTime getStatusUpdateDate() {
        return this.statusUpdateDate;
    }

    public Payment statusUpdateDate(ZonedDateTime statusUpdateDate) {
        this.setStatusUpdateDate(statusUpdateDate);
        return this;
    }

    public void setStatusUpdateDate(ZonedDateTime statusUpdateDate) {
        this.statusUpdateDate = statusUpdateDate;
    }

    public Float getAmount() {
        return this.amount;
    }

    public Payment amount(Float amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public String getRedirectionUrl() {
        return this.redirectionUrl;
    }

    public Payment redirectionUrl(String redirectionUrl) {
        this.setRedirectionUrl(redirectionUrl);
        return this;
    }

    public void setRedirectionUrl(String redirectionUrl) {
        this.redirectionUrl = redirectionUrl;
    }

    public PAYMENTAGGREGATOR getPaymentAggregator() {
        return this.paymentAggregator;
    }

    public Payment paymentAggregator(PAYMENTAGGREGATOR paymentAggregator) {
        this.setPaymentAggregator(paymentAggregator);
        return this;
    }

    public void setPaymentAggregator(PAYMENTAGGREGATOR paymentAggregator) {
        this.paymentAggregator = paymentAggregator;
    }

    public PAYMENTGATEWAY getPaymentgateway() {
        return this.paymentgateway;
    }

    public Payment paymentgateway(PAYMENTGATEWAY paymentgateway) {
        this.setPaymentgateway(paymentgateway);
        return this;
    }

    public void setPaymentgateway(PAYMENTGATEWAY paymentgateway) {
        this.paymentgateway = paymentgateway;
    }

    public PAYMENTSTATUS getPaymentStatus() {
        return this.paymentStatus;
    }

    public Payment paymentStatus(PAYMENTSTATUS paymentStatus) {
        this.setPaymentStatus(paymentStatus);
        return this;
    }

    public void setPaymentStatus(PAYMENTSTATUS paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public PAYMENTMETHOD getPaymentMethod() {
        return this.paymentMethod;
    }

    public Payment paymentMethod(PAYMENTMETHOD paymentMethod) {
        this.setPaymentMethod(paymentMethod);
        return this;
    }

    public void setPaymentMethod(PAYMENTMETHOD paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public ZonedDateTime getCreatedTime() {
        return this.createdTime;
    }

    public Payment createdTime(ZonedDateTime createdTime) {
        this.setCreatedTime(createdTime);
        return this;
    }

    public void setCreatedTime(ZonedDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public ZonedDateTime getUpdatedTime() {
        return this.updatedTime;
    }

    public Payment updatedTime(ZonedDateTime updatedTime) {
        this.setUpdatedTime(updatedTime);
        return this;
    }

    public void setUpdatedTime(ZonedDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Payment createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public Payment updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Order getPaymentOrder() {
        return this.paymentOrder;
    }

    public void setPaymentOrder(Order order) {
        this.paymentOrder = order;
    }

    public Payment paymentOrder(Order order) {
        this.setPaymentOrder(order);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Payment)) {
            return false;
        }
        return id != null && id.equals(((Payment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Payment{" +
            "id=" + getId() +
            ", pgTransactionId='" + getPgTransactionId() + "'" +
            ", clientTransactionType='" + getClientTransactionType() + "'" +
            ", clientTransactionId=" + getClientTransactionId() +
            ", pgStatus='" + getPgStatus() + "'" +
            ", transactioninitDate='" + getTransactioninitDate() + "'" +
            ", statusUpdateDate='" + getStatusUpdateDate() + "'" +
            ", amount=" + getAmount() +
            ", redirectionUrl='" + getRedirectionUrl() + "'" +
            ", paymentAggregator='" + getPaymentAggregator() + "'" +
            ", paymentgateway='" + getPaymentgateway() + "'" +
            ", paymentStatus='" + getPaymentStatus() + "'" +
            ", paymentMethod='" + getPaymentMethod() + "'" +
            ", createdTime='" + getCreatedTime() + "'" +
            ", updatedTime='" + getUpdatedTime() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            "}";
    }
}
