package com.reliance.jmdb2b.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.reliance.jmdb2b.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RecentSearchesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RecentSearches.class);
        RecentSearches recentSearches1 = new RecentSearches();
        recentSearches1.setId(1L);
        RecentSearches recentSearches2 = new RecentSearches();
        recentSearches2.setId(recentSearches1.getId());
        assertThat(recentSearches1).isEqualTo(recentSearches2);
        recentSearches2.setId(2L);
        assertThat(recentSearches1).isNotEqualTo(recentSearches2);
        recentSearches1.setId(null);
        assertThat(recentSearches1).isNotEqualTo(recentSearches2);
    }
}
