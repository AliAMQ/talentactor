package com.talentactor.service;

import com.talentactor.service.dto.ProfileDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Profile.
 */
public interface ProfileService {

    /**
     * Save a profile.
     *
     * @param profileDTO the entity to save
     * @return the persisted entity
     */
    ProfileDTO save(ProfileDTO profileDTO);

    /**
     * Get all the profiles.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ProfileDTO> findAll(Pageable pageable);

    /**
     * Get all the Profile with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    Page<ProfileDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" profile.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ProfileDTO> findOne(Long id);

    /**
     * Get the "userId" Profile.
     *
     * @param userId the id of the entity
     * @return the entity
     */
    Optional<ProfileDTO> findOneByUserId(Long userId);

    /**
     * Delete the "id" profile.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
