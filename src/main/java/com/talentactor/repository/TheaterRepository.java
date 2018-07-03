package com.talentactor.repository;

import com.talentactor.domain.Theater;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Theater entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TheaterRepository extends JpaRepository<Theater, Long> {

}
