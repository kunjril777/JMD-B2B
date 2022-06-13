package com.reliance.jmdb2b.repository;

import com.reliance.jmdb2b.domain.TrendingProducts;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TrendingProducts entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TrendingProductsRepository extends JpaRepository<TrendingProducts, Long> {}
