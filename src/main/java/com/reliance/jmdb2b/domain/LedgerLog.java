package com.reliance.jmdb2b.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.reliance.jmdb2b.domain.enumeration.CLIENTNAME;
import com.reliance.jmdb2b.domain.enumeration.TRANSACTIONTYPE;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;

/**
 * A LedgerLog.
 */
@Entity
@Table(name = "ledger_log")
public class LedgerLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "total_balance")
    private Double totalBalance;

    @Column(name = "transaction_date")
    private ZonedDateTime transactionDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private TRANSACTIONTYPE transactionType;

    @Column(name = "client_transaction_id")
    private Long clientTransactionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "client_name")
    private CLIENTNAME clientName;

    @ManyToOne
    @JsonIgnoreProperties(value = { "ledgerNledgerLogs" }, allowSetters = true)
    private Ledger ledger;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LedgerLog id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return this.amount;
    }

    public LedgerLog amount(Double amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getTotalBalance() {
        return this.totalBalance;
    }

    public LedgerLog totalBalance(Double totalBalance) {
        this.setTotalBalance(totalBalance);
        return this;
    }

    public void setTotalBalance(Double totalBalance) {
        this.totalBalance = totalBalance;
    }

    public ZonedDateTime getTransactionDate() {
        return this.transactionDate;
    }

    public LedgerLog transactionDate(ZonedDateTime transactionDate) {
        this.setTransactionDate(transactionDate);
        return this;
    }

    public void setTransactionDate(ZonedDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public TRANSACTIONTYPE getTransactionType() {
        return this.transactionType;
    }

    public LedgerLog transactionType(TRANSACTIONTYPE transactionType) {
        this.setTransactionType(transactionType);
        return this;
    }

    public void setTransactionType(TRANSACTIONTYPE transactionType) {
        this.transactionType = transactionType;
    }

    public Long getClientTransactionId() {
        return this.clientTransactionId;
    }

    public LedgerLog clientTransactionId(Long clientTransactionId) {
        this.setClientTransactionId(clientTransactionId);
        return this;
    }

    public void setClientTransactionId(Long clientTransactionId) {
        this.clientTransactionId = clientTransactionId;
    }

    public CLIENTNAME getClientName() {
        return this.clientName;
    }

    public LedgerLog clientName(CLIENTNAME clientName) {
        this.setClientName(clientName);
        return this;
    }

    public void setClientName(CLIENTNAME clientName) {
        this.clientName = clientName;
    }

    public Ledger getLedger() {
        return this.ledger;
    }

    public void setLedger(Ledger ledger) {
        this.ledger = ledger;
    }

    public LedgerLog ledger(Ledger ledger) {
        this.setLedger(ledger);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LedgerLog)) {
            return false;
        }
        return id != null && id.equals(((LedgerLog) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LedgerLog{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", totalBalance=" + getTotalBalance() +
            ", transactionDate='" + getTransactionDate() + "'" +
            ", transactionType='" + getTransactionType() + "'" +
            ", clientTransactionId=" + getClientTransactionId() +
            ", clientName='" + getClientName() + "'" +
            "}";
    }
}
