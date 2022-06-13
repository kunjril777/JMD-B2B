package com.reliance.jmdb2b.repository;

import com.reliance.jmdb2b.domain.CartTransaction;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CartTransaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CartTransactionRepository extends JpaRepository<CartTransaction, Long> {}
