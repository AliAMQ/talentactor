package com.talentactor.service;

import com.talentactor.service.dto.WeaponDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Weapon.
 */
public interface WeaponService {

    /**
     * Save a weapon.
     *
     * @param weaponDTO the entity to save
     * @return the persisted entity
     */
    WeaponDTO save(WeaponDTO weaponDTO);

    /**
     * Get all the weapons.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<WeaponDTO> findAll(Pageable pageable);


    /**
     * Get the "id" weapon.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<WeaponDTO> findOne(Long id);

    /**
     * Delete the "id" weapon.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
