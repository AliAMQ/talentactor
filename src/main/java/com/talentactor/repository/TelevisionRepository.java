package com.talentactor.repository;

import com.talentactor.domain.Television;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Television entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TelevisionRepository extends JpaRepository<Television, Long> {

}
