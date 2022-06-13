package com.reliance.jmdb2b.repository;

import com.reliance.jmdb2b.domain.ProductInventory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ProductInventory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductInventoryRepository extends JpaRepository<ProductInventory, Long> {}
