package com.talentactor.repository;

import com.talentactor.domain.Print;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Print entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PrintRepository extends JpaRepository<Print, Long>, JpaSpecificationExecutor<Print> {

}
