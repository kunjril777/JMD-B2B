package com.reliance.jmdb2b.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Ledger.
 */
@Entity
@Table(name = "ledger")
public class Ledger implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "jio_credits")
    private Double jioCredits;

    @Column(name = "total_credit")
    private Double totalCredit;

    @Column(name = "credit_balance")
    private Double creditBalance;

    @OneToMany(mappedBy = "ledger")
    @JsonIgnoreProperties(value = { "ledger" }, allowSetters = true)
    private Set<LedgerLog> ledgerNledgerLogs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Ledger id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getJioCredits() {
        return this.jioCredits;
    }

    public Ledger jioCredits(Double jioCredits) {
        this.setJioCredits(jioCredits);
        return this;
    }

    public void setJioCredits(Double jioCredits) {
        this.jioCredits = jioCredits;
    }

    public Double getTotalCredit() {
        return this.totalCredit;
    }

    public Ledger totalCredit(Double totalCredit) {
        this.setTotalCredit(totalCredit);
        return this;
    }

    public void setTotalCredit(Double totalCredit) {
        this.totalCredit = totalCredit;
    }

    public Double getCreditBalance() {
        return this.creditBalance;
    }

    public Ledger creditBalance(Double creditBalance) {
        this.setCreditBalance(creditBalance);
        return this;
    }

    public void setCreditBalance(Double creditBalance) {
        this.creditBalance = creditBalance;
    }

    public Set<LedgerLog> getLedgerNledgerLogs() {
        return this.ledgerNledgerLogs;
    }

    public void setLedgerNledgerLogs(Set<LedgerLog> ledgerLogs) {
        if (this.ledgerNledgerLogs != null) {
            this.ledgerNledgerLogs.forEach(i -> i.setLedger(null));
        }
        if (ledgerLogs != null) {
            ledgerLogs.forEach(i -> i.setLedger(this));
        }
        this.ledgerNledgerLogs = ledgerLogs;
    }

    public Ledger ledgerNledgerLogs(Set<LedgerLog> ledgerLogs) {
        this.setLedgerNledgerLogs(ledgerLogs);
        return this;
    }

    public Ledger addLedgerNledgerLog(LedgerLog ledgerLog) {
        this.ledgerNledgerLogs.add(ledgerLog);
        ledgerLog.setLedger(this);
        return this;
    }

    public Ledger removeLedgerNledgerLog(LedgerLog ledgerLog) {
        this.ledgerNledgerLogs.remove(ledgerLog);
        ledgerLog.setLedger(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ledger)) {
            return false;
        }
        return id != null && id.equals(((Ledger) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ledger{" +
            "id=" + getId() +
            ", jioCredits=" + getJioCredits() +
            ", totalCredit=" + getTotalCredit() +
            ", creditBalance=" + getCreditBalance() +
            "}";
    }
}
