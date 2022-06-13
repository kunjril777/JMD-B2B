package com.reliance.jmdb2b.repository;

import com.reliance.jmdb2b.domain.DealsOfTheDay;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DealsOfTheDay entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DealsOfTheDayRepository extends JpaRepository<DealsOfTheDay, Long> {}
