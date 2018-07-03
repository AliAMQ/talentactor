package com.talentactor.service;

import com.talentactor.service.dto.CommercialDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Commercial.
 */
public interface CommercialService {

    /**
     * Save a commercial.
     *
     * @param commercialDTO the entity to save
     * @return the persisted entity
     */
    CommercialDTO save(CommercialDTO commercialDTO);

    /**
     * Get all the commercials.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CommercialDTO> findAll(Pageable pageable);


    /**
     * Get the "id" commercial.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CommercialDTO> findOne(Long id);

    /**
     * Delete the "id" commercial.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
