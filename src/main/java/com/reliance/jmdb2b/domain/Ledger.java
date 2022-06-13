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

    @OneToMany(mappedBy = "ledger")
    @JsonIgnoreProperties(value = { "ledger" }, allowSetters = true)
    private Set<LedgerLog> ledgers = new HashSet<>();

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

    public Set<LedgerLog> getLedgers() {
        return this.ledgers;
    }

    public void setLedgers(Set<LedgerLog> ledgerLogs) {
        if (this.ledgers != null) {
            this.ledgers.forEach(i -> i.setLedger(null));
        }
        if (ledgerLogs != null) {
            ledgerLogs.forEach(i -> i.setLedger(this));
        }
        this.ledgers = ledgerLogs;
    }

    public Ledger ledgers(Set<LedgerLog> ledgerLogs) {
        this.setLedgers(ledgerLogs);
        return this;
    }

    public Ledger addLedger(LedgerLog ledgerLog) {
        this.ledgers.add(ledgerLog);
        ledgerLog.setLedger(this);
        return this;
    }

    public Ledger removeLedger(LedgerLog ledgerLog) {
        this.ledgers.remove(ledgerLog);
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
            "}";
    }
}
