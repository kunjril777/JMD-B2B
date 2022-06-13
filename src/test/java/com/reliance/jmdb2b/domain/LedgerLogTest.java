package com.reliance.jmdb2b.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.reliance.jmdb2b.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LedgerLogTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LedgerLog.class);
        LedgerLog ledgerLog1 = new LedgerLog();
        ledgerLog1.setId(1L);
        LedgerLog ledgerLog2 = new LedgerLog();
        ledgerLog2.setId(ledgerLog1.getId());
        assertThat(ledgerLog1).isEqualTo(ledgerLog2);
        ledgerLog2.setId(2L);
        assertThat(ledgerLog1).isNotEqualTo(ledgerLog2);
        ledgerLog1.setId(null);
        assertThat(ledgerLog1).isNotEqualTo(ledgerLog2);
    }
}
