package com.talentactor.service;

import com.talentactor.service.dto.TheaterDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Theater.
 */
public interface TheaterService {

    /**
     * Save a theater.
     *
     * @param theaterDTO the entity to save
     * @return the persisted entity
     */
    TheaterDTO save(TheaterDTO theaterDTO);

    /**
     * Get all the theaters.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<TheaterDTO> findAll(Pageable pageable);


    /**
     * Get the "id" theater.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<TheaterDTO> findOne(Long id);

    /**
     * Delete the "id" theater.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
