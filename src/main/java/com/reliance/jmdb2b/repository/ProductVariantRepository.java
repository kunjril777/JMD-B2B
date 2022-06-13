package com.reliance.jmdb2b.repository;

import com.reliance.jmdb2b.domain.ProductVariant;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ProductVariant entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {}
