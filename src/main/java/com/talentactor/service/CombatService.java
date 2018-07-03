package com.talentactor.service;

import com.talentactor.service.dto.CombatDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Combat.
 */
public interface CombatService {

    /**
     * Save a combat.
     *
     * @param combatDTO the entity to save
     * @return the persisted entity
     */
    CombatDTO save(CombatDTO combatDTO);

    /**
     * Get all the combats.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CombatDTO> findAll(Pageable pageable);


    /**
     * Get the "id" combat.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CombatDTO> findOne(Long id);

    /**
     * Delete the "id" combat.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
