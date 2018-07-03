package com.talentactor.service;

import com.talentactor.service.dto.TelevisionDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Television.
 */
public interface TelevisionService {

    /**
     * Save a television.
     *
     * @param televisionDTO the entity to save
     * @return the persisted entity
     */
    TelevisionDTO save(TelevisionDTO televisionDTO);

    /**
     * Get all the televisions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<TelevisionDTO> findAll(Pageable pageable);


    /**
     * Get the "id" television.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<TelevisionDTO> findOne(Long id);

    /**
     * Delete the "id" television.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
