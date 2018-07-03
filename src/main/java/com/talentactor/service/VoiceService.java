package com.talentactor.service;

import com.talentactor.service.dto.VoiceDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Voice.
 */
public interface VoiceService {

    /**
     * Save a voice.
     *
     * @param voiceDTO the entity to save
     * @return the persisted entity
     */
    VoiceDTO save(VoiceDTO voiceDTO);

    /**
     * Get all the voices.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<VoiceDTO> findAll(Pageable pageable);


    /**
     * Get the "id" voice.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<VoiceDTO> findOne(Long id);

    /**
     * Delete the "id" voice.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
