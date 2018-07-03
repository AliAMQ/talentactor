package com.talentactor.service;

import com.talentactor.service.dto.InternetDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Internet.
 */
public interface InternetService {

    /**
     * Save a internet.
     *
     * @param internetDTO the entity to save
     * @return the persisted entity
     */
    InternetDTO save(InternetDTO internetDTO);

    /**
     * Get all the internets.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<InternetDTO> findAll(Pageable pageable);


    /**
     * Get the "id" internet.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<InternetDTO> findOne(Long id);

    /**
     * Delete the "id" internet.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
