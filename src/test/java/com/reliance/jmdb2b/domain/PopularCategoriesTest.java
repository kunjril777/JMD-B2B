package com.reliance.jmdb2b.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.reliance.jmdb2b.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PopularCategoriesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PopularCategories.class);
        PopularCategories popularCategories1 = new PopularCategories();
        popularCategories1.setId(1L);
        PopularCategories popularCategories2 = new PopularCategories();
        popularCategories2.setId(popularCategories1.getId());
        assertThat(popularCategories1).isEqualTo(popularCategories2);
        popularCategories2.setId(2L);
        assertThat(popularCategories1).isNotEqualTo(popularCategories2);
        popularCategories1.setId(null);
        assertThat(popularCategories1).isNotEqualTo(popularCategories2);
    }
}
