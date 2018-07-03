package com.talentactor.service;

import com.talentactor.service.dto.SwimmingDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Swimming.
 */
public interface SwimmingService {

    /**
     * Save a swimming.
     *
     * @param swimmingDTO the entity to save
     * @return the persisted entity
     */
    SwimmingDTO save(SwimmingDTO swimmingDTO);

    /**
     * Get all the swimmings.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SwimmingDTO> findAll(Pageable pageable);


    /**
     * Get the "id" swimming.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<SwimmingDTO> findOne(Long id);

    /**
     * Delete the "id" swimming.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
