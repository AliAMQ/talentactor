package com.talentactor.repository;

import com.talentactor.domain.Commercial;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Commercial entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommercialRepository extends JpaRepository<Commercial, Long>, JpaSpecificationExecutor<Commercial> {

}
