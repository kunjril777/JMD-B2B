package com.reliance.jmdb2b.repository;

import com.reliance.jmdb2b.domain.WishListProduct;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the WishListProduct entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WishListProductRepository extends JpaRepository<WishListProduct, Long> {}
