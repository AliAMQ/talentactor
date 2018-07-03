package com.talentactor.service;

import com.talentactor.service.dto.InstrumentDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Instrument.
 */
public interface InstrumentService {

    /**
     * Save a instrument.
     *
     * @param instrumentDTO the entity to save
     * @return the persisted entity
     */
    InstrumentDTO save(InstrumentDTO instrumentDTO);

    /**
     * Get all the instruments.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<InstrumentDTO> findAll(Pageable pageable);


    /**
     * Get the "id" instrument.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<InstrumentDTO> findOne(Long id);

    /**
     * Delete the "id" instrument.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
