package com.reliance.jmdb2b.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.reliance.jmdb2b.domain.enumeration.CLIENTNAME;
import com.reliance.jmdb2b.domain.enumeration.LTSTATUS;
import com.reliance.jmdb2b.domain.enumeration.TRANSACTIONTYPE;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;

/**
 * A LedgerTransaction.
 */
@Entity
@Table(name = "ledger_transaction")
public class LedgerTransaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "amount")
    private Long amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private TRANSACTIONTYPE transactionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "ledger_transaction_status")
    private LTSTATUS ledgerTransactionStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "client_name")
    private CLIENTNAME clientName;

    @Column(name = "created_time")
    private ZonedDateTime createdTime;

    @Column(name = "updated_time")
    private ZonedDateTime updatedTime;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @JsonIgnoreProperties(value = { "ledger" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private LedgerLog ledgerTransaction;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LedgerTransaction id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAmount() {
        return this.amount;
    }

    public LedgerTransaction amount(Long amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public TRANSACTIONTYPE getTransactionType() {
        return this.transactionType;
    }

    public LedgerTransaction transactionType(TRANSACTIONTYPE transactionType) {
        this.setTransactionType(transactionType);
        return this;
    }

    public void setTransactionType(TRANSACTIONTYPE transactionType) {
        this.transactionType = transactionType;
    }

    public LTSTATUS getLedgerTransactionStatus() {
        return this.ledgerTransactionStatus;
    }

    public LedgerTransaction ledgerTransactionStatus(LTSTATUS ledgerTransactionStatus) {
        this.setLedgerTransactionStatus(ledgerTransactionStatus);
        return this;
    }

    public void setLedgerTransactionStatus(LTSTATUS ledgerTransactionStatus) {
        this.ledgerTransactionStatus = ledgerTransactionStatus;
    }

    public CLIENTNAME getClientName() {
        return this.clientName;
    }

    public LedgerTransaction clientName(CLIENTNAME clientName) {
        this.setClientName(clientName);
        return this;
    }

    public void setClientName(CLIENTNAME clientName) {
        this.clientName = clientName;
    }

    public ZonedDateTime getCreatedTime() {
        return this.createdTime;
    }

    public LedgerTransaction createdTime(ZonedDateTime createdTime) {
        this.setCreatedTime(createdTime);
        return this;
    }

    public void setCreatedTime(ZonedDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public ZonedDateTime getUpdatedTime() {
        return this.updatedTime;
    }

    public LedgerTransaction updatedTime(ZonedDateTime updatedTime) {
        this.setUpdatedTime(updatedTime);
        return this;
    }

    public void setUpdatedTime(ZonedDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public LedgerTransaction createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public LedgerTransaction updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LedgerLog getLedgerTransaction() {
        return this.ledgerTransaction;
    }

    public void setLedgerTransaction(LedgerLog ledgerLog) {
        this.ledgerTransaction = ledgerLog;
    }

    public LedgerTransaction ledgerTransaction(LedgerLog ledgerLog) {
        this.setLedgerTransaction(ledgerLog);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LedgerTransaction)) {
            return false;
        }
        return id != null && id.equals(((LedgerTransaction) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LedgerTransaction{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", transactionType='" + getTransactionType() + "'" +
            ", ledgerTransactionStatus='" + getLedgerTransactionStatus() + "'" +
            ", clientName='" + getClientName() + "'" +
            ", createdTime='" + getCreatedTime() + "'" +
            ", updatedTime='" + getUpdatedTime() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            "}";
    }
}
