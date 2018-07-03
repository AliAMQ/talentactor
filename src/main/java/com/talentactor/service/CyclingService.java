package com.talentactor.service;

import com.talentactor.service.dto.CyclingDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Cycling.
 */
public interface CyclingService {

    /**
     * Save a cycling.
     *
     * @param cyclingDTO the entity to save
     * @return the persisted entity
     */
    CyclingDTO save(CyclingDTO cyclingDTO);

    /**
     * Get all the cyclings.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CyclingDTO> findAll(Pageable pageable);


    /**
     * Get the "id" cycling.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CyclingDTO> findOne(Long id);

    /**
     * Delete the "id" cycling.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
