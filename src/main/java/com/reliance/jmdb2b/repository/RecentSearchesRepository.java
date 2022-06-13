package com.reliance.jmdb2b.repository;

import com.reliance.jmdb2b.domain.RecentSearches;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the RecentSearches entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RecentSearchesRepository extends JpaRepository<RecentSearches, Long> {}
