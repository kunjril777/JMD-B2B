package com.reliance.jmdb2b.repository;

import com.reliance.jmdb2b.domain.CartProduct;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CartProduct entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CartProductRepository extends JpaRepository<CartProduct, Long> {}
