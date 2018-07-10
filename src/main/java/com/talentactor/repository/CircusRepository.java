package com.talentactor.repository;

import com.talentactor.domain.Circus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Circus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CircusRepository extends JpaRepository<Circus, Long>, JpaSpecificationExecutor<Circus> {

}
