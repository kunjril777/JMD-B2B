package com.reliance.jmdb2b.repository;

import com.reliance.jmdb2b.domain.ItemsBackInStock;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ItemsBackInStock entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemsBackInStockRepository extends JpaRepository<ItemsBackInStock, Long> {}
