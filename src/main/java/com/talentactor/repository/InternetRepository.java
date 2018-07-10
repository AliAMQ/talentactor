package com.talentactor.repository;

import com.talentactor.domain.Internet;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Internet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InternetRepository extends JpaRepository<Internet, Long>, JpaSpecificationExecutor<Internet> {

}
