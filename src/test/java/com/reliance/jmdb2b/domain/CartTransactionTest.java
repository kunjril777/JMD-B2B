package com.reliance.jmdb2b.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.reliance.jmdb2b.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CartTransactionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CartTransaction.class);
        CartTransaction cartTransaction1 = new CartTransaction();
        cartTransaction1.setId(1L);
        CartTransaction cartTransaction2 = new CartTransaction();
        cartTransaction2.setId(cartTransaction1.getId());
        assertThat(cartTransaction1).isEqualTo(cartTransaction2);
        cartTransaction2.setId(2L);
        assertThat(cartTransaction1).isNotEqualTo(cartTransaction2);
        cartTransaction1.setId(null);
        assertThat(cartTransaction1).isNotEqualTo(cartTransaction2);
    }
}
