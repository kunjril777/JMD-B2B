package com.reliance.jmdb2b.repository;

import com.reliance.jmdb2b.domain.PincodeDeliveryMapping;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PincodeDeliveryMapping entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PincodeDeliveryMappingRepository extends JpaRepository<PincodeDeliveryMapping, Long> {}
