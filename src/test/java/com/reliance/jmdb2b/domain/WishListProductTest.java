package com.reliance.jmdb2b.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.reliance.jmdb2b.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WishListProductTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WishListProduct.class);
        WishListProduct wishListProduct1 = new WishListProduct();
        wishListProduct1.setId(1L);
        WishListProduct wishListProduct2 = new WishListProduct();
        wishListProduct2.setId(wishListProduct1.getId());
        assertThat(wishListProduct1).isEqualTo(wishListProduct2);
        wishListProduct2.setId(2L);
        assertThat(wishListProduct1).isNotEqualTo(wishListProduct2);
        wishListProduct1.setId(null);
        assertThat(wishListProduct1).isNotEqualTo(wishListProduct2);
    }
}
