package com.reliance.jmdb2b.repository;

import com.reliance.jmdb2b.domain.AttributeName;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AttributeName entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AttributeNameRepository extends JpaRepository<AttributeName, Long> {}
