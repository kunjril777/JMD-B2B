package com.reliance.jmdb2b.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.reliance.jmdb2b.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PincodeDeliveryMappingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PincodeDeliveryMapping.class);
        PincodeDeliveryMapping pincodeDeliveryMapping1 = new PincodeDeliveryMapping();
        pincodeDeliveryMapping1.setId(1L);
        PincodeDeliveryMapping pincodeDeliveryMapping2 = new PincodeDeliveryMapping();
        pincodeDeliveryMapping2.setId(pincodeDeliveryMapping1.getId());
        assertThat(pincodeDeliveryMapping1).isEqualTo(pincodeDeliveryMapping2);
        pincodeDeliveryMapping2.setId(2L);
        assertThat(pincodeDeliveryMapping1).isNotEqualTo(pincodeDeliveryMapping2);
        pincodeDeliveryMapping1.setId(null);
        assertThat(pincodeDeliveryMapping1).isNotEqualTo(pincodeDeliveryMapping2);
    }
}
