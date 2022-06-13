package com.reliance.jmdb2b.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.reliance.jmdb2b.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserB2BTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserB2B.class);
        UserB2B userB2B1 = new UserB2B();
        userB2B1.setId(1L);
        UserB2B userB2B2 = new UserB2B();
        userB2B2.setId(userB2B1.getId());
        assertThat(userB2B1).isEqualTo(userB2B2);
        userB2B2.setId(2L);
        assertThat(userB2B1).isNotEqualTo(userB2B2);
        userB2B1.setId(null);
        assertThat(userB2B1).isNotEqualTo(userB2B2);
    }
}
