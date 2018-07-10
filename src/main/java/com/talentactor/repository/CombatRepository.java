package com.talentactor.repository;

import com.talentactor.domain.Combat;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Combat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CombatRepository extends JpaRepository<Combat, Long>, JpaSpecificationExecutor<Combat> {

}
