package com.reliance.jmdb2b.repository;

import com.reliance.jmdb2b.domain.RecommmendedItems;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the RecommmendedItems entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RecommmendedItemsRepository extends JpaRepository<RecommmendedItems, Long> {}
