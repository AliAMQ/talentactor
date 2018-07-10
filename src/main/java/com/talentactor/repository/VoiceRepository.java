package com.talentactor.repository;

import com.talentactor.domain.Voice;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Voice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VoiceRepository extends JpaRepository<Voice, Long>, JpaSpecificationExecutor<Voice> {

}
