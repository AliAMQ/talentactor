package com.talentactor.service;

import com.talentactor.service.dto.PrintDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Print.
 */
public interface PrintService {

    /**
     * Save a print.
     *
     * @param printDTO the entity to save
     * @return the persisted entity
     */
    PrintDTO save(PrintDTO printDTO);

    /**
     * Get all the prints.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<PrintDTO> findAll(Pageable pageable);


    /**
     * Get the "id" print.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<PrintDTO> findOne(Long id);

    /**
     * Delete the "id" print.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
