package com.reliance.jmdb2b.repository;

import com.reliance.jmdb2b.domain.LedgerLog;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LedgerLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LedgerLogRepository extends JpaRepository<LedgerLog, Long> {}
