package com.reliance.jmdb2b.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.reliance.jmdb2b.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CartProductTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CartProduct.class);
        CartProduct cartProduct1 = new CartProduct();
        cartProduct1.setId(1L);
        CartProduct cartProduct2 = new CartProduct();
        cartProduct2.setId(cartProduct1.getId());
        assertThat(cartProduct1).isEqualTo(cartProduct2);
        cartProduct2.setId(2L);
        assertThat(cartProduct1).isNotEqualTo(cartProduct2);
        cartProduct1.setId(null);
        assertThat(cartProduct1).isNotEqualTo(cartProduct2);
    }
}
