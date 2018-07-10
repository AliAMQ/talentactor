package com.talentactor.repository;

import com.talentactor.domain.Horse;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Horse entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HorseRepository extends JpaRepository<Horse, Long>, JpaSpecificationExecutor<Horse> {

}
