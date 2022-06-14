package com.reliance.jmdb2b.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.reliance.jmdb2b.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AttributeNameTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AttributeName.class);
        AttributeName attributeName1 = new AttributeName();
        attributeName1.setId(1L);
        AttributeName attributeName2 = new AttributeName();
        attributeName2.setId(attributeName1.getId());
        assertThat(attributeName1).isEqualTo(attributeName2);
        attributeName2.setId(2L);
        assertThat(attributeName1).isNotEqualTo(attributeName2);
        attributeName1.setId(null);
        assertThat(attributeName1).isNotEqualTo(attributeName2);
    }
}
