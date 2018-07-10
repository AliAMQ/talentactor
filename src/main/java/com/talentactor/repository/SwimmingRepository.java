package com.talentactor.repository;

import com.talentactor.domain.Swimming;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Swimming entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SwimmingRepository extends JpaRepository<Swimming, Long>, JpaSpecificationExecutor<Swimming> {

}
