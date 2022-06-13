package com.reliance.jmdb2b.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.reliance.jmdb2b.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RecommmendedItemsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RecommmendedItems.class);
        RecommmendedItems recommmendedItems1 = new RecommmendedItems();
        recommmendedItems1.setId(1L);
        RecommmendedItems recommmendedItems2 = new RecommmendedItems();
        recommmendedItems2.setId(recommmendedItems1.getId());
        assertThat(recommmendedItems1).isEqualTo(recommmendedItems2);
        recommmendedItems2.setId(2L);
        assertThat(recommmendedItems1).isNotEqualTo(recommmendedItems2);
        recommmendedItems1.setId(null);
        assertThat(recommmendedItems1).isNotEqualTo(recommmendedItems2);
    }
}
