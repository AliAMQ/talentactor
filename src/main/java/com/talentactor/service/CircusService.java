package com.talentactor.service;

import com.talentactor.service.dto.CircusDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Circus.
 */
public interface CircusService {

    /**
     * Save a circus.
     *
     * @param circusDTO the entity to save
     * @return the persisted entity
     */
    CircusDTO save(CircusDTO circusDTO);

    /**
     * Get all the circuses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CircusDTO> findAll(Pageable pageable);


    /**
     * Get the "id" circus.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CircusDTO> findOne(Long id);

    /**
     * Delete the "id" circus.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
