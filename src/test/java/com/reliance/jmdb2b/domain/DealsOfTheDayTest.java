package com.reliance.jmdb2b.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.reliance.jmdb2b.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DealsOfTheDayTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DealsOfTheDay.class);
        DealsOfTheDay dealsOfTheDay1 = new DealsOfTheDay();
        dealsOfTheDay1.setId(1L);
        DealsOfTheDay dealsOfTheDay2 = new DealsOfTheDay();
        dealsOfTheDay2.setId(dealsOfTheDay1.getId());
        assertThat(dealsOfTheDay1).isEqualTo(dealsOfTheDay2);
        dealsOfTheDay2.setId(2L);
        assertThat(dealsOfTheDay1).isNotEqualTo(dealsOfTheDay2);
        dealsOfTheDay1.setId(null);
        assertThat(dealsOfTheDay1).isNotEqualTo(dealsOfTheDay2);
    }
}
