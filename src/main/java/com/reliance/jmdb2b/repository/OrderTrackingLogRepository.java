package com.reliance.jmdb2b.repository;

import com.reliance.jmdb2b.domain.OrderTrackingLog;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OrderTrackingLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderTrackingLogRepository extends JpaRepository<OrderTrackingLog, Long> {}
