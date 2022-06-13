package com.reliance.jmdb2b.repository;

import com.reliance.jmdb2b.domain.UserB2B;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the UserB2B entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserB2BRepository extends JpaRepository<UserB2B, Long> {}
