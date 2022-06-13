package com.reliance.jmdb2b.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.reliance.jmdb2b.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TopselectionsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Topselections.class);
        Topselections topselections1 = new Topselections();
        topselections1.setId(1L);
        Topselections topselections2 = new Topselections();
        topselections2.setId(topselections1.getId());
        assertThat(topselections1).isEqualTo(topselections2);
        topselections2.setId(2L);
        assertThat(topselections1).isNotEqualTo(topselections2);
        topselections1.setId(null);
        assertThat(topselections1).isNotEqualTo(topselections2);
    }
}
