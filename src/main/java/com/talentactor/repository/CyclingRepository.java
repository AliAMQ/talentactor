package com.talentactor.repository;

import com.talentactor.domain.Cycling;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Cycling entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CyclingRepository extends JpaRepository<Cycling, Long>, JpaSpecificationExecutor<Cycling> {

}
