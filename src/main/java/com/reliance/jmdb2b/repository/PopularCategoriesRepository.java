package com.reliance.jmdb2b.repository;

import com.reliance.jmdb2b.domain.PopularCategories;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PopularCategories entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PopularCategoriesRepository extends JpaRepository<PopularCategories, Long> {}
