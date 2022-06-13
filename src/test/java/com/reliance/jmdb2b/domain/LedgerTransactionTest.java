package com.reliance.jmdb2b.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.reliance.jmdb2b.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LedgerTransactionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LedgerTransaction.class);
        LedgerTransaction ledgerTransaction1 = new LedgerTransaction();
        ledgerTransaction1.setId(1L);
        LedgerTransaction ledgerTransaction2 = new LedgerTransaction();
        ledgerTransaction2.setId(ledgerTransaction1.getId());
        assertThat(ledgerTransaction1).isEqualTo(ledgerTransaction2);
        ledgerTransaction2.setId(2L);
        assertThat(ledgerTransaction1).isNotEqualTo(ledgerTransaction2);
        ledgerTransaction1.setId(null);
        assertThat(ledgerTransaction1).isNotEqualTo(ledgerTransaction2);
    }
}
