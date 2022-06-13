package com.reliance.jmdb2b.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.reliance.jmdb2b.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderTrackingLogTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderTrackingLog.class);
        OrderTrackingLog orderTrackingLog1 = new OrderTrackingLog();
        orderTrackingLog1.setId(1L);
        OrderTrackingLog orderTrackingLog2 = new OrderTrackingLog();
        orderTrackingLog2.setId(orderTrackingLog1.getId());
        assertThat(orderTrackingLog1).isEqualTo(orderTrackingLog2);
        orderTrackingLog2.setId(2L);
        assertThat(orderTrackingLog1).isNotEqualTo(orderTrackingLog2);
        orderTrackingLog1.setId(null);
        assertThat(orderTrackingLog1).isNotEqualTo(orderTrackingLog2);
    }
}
