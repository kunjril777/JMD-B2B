package com.reliance.jmdb2b.repository;

import com.reliance.jmdb2b.domain.LedgerTransaction;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LedgerTransaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LedgerTransactionRepository extends JpaRepository<LedgerTransaction, Long> {}
