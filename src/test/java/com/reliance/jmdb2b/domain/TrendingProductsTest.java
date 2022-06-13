package com.reliance.jmdb2b.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.reliance.jmdb2b.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TrendingProductsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TrendingProducts.class);
        TrendingProducts trendingProducts1 = new TrendingProducts();
        trendingProducts1.setId(1L);
        TrendingProducts trendingProducts2 = new TrendingProducts();
        trendingProducts2.setId(trendingProducts1.getId());
        assertThat(trendingProducts1).isEqualTo(trendingProducts2);
        trendingProducts2.setId(2L);
        assertThat(trendingProducts1).isNotEqualTo(trendingProducts2);
        trendingProducts1.setId(null);
        assertThat(trendingProducts1).isNotEqualTo(trendingProducts2);
    }
}
