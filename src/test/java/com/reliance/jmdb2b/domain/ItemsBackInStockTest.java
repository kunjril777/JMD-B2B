package com.reliance.jmdb2b.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.reliance.jmdb2b.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ItemsBackInStockTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemsBackInStock.class);
        ItemsBackInStock itemsBackInStock1 = new ItemsBackInStock();
        itemsBackInStock1.setId(1L);
        ItemsBackInStock itemsBackInStock2 = new ItemsBackInStock();
        itemsBackInStock2.setId(itemsBackInStock1.getId());
        assertThat(itemsBackInStock1).isEqualTo(itemsBackInStock2);
        itemsBackInStock2.setId(2L);
        assertThat(itemsBackInStock1).isNotEqualTo(itemsBackInStock2);
        itemsBackInStock1.setId(null);
        assertThat(itemsBackInStock1).isNotEqualTo(itemsBackInStock2);
    }
}
