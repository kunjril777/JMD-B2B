package com.reliance.jmdb2b.repository;

import com.reliance.jmdb2b.domain.Topselections;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Topselections entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TopselectionsRepository extends JpaRepository<Topselections, Long> {}
