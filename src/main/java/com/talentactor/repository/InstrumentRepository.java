package com.talentactor.repository;

import com.talentactor.domain.Instrument;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Instrument entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InstrumentRepository extends JpaRepository<Instrument, Long>, JpaSpecificationExecutor<Instrument> {

}
