package com.talentactor.service.impl;

import com.talentactor.service.VoiceService;
import com.talentactor.domain.Voice;
import com.talentactor.repository.VoiceRepository;
import com.talentactor.service.dto.VoiceDTO;
import com.talentactor.service.mapper.VoiceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing Voice.
 */
@Service
@Transactional
public class VoiceServiceImpl implements VoiceService {

    private final Logger log = LoggerFactory.getLogger(VoiceServiceImpl.class);

    private final VoiceRepository voiceRepository;

    private final VoiceMapper voiceMapper;

    public VoiceServiceImpl(VoiceRepository voiceRepository, VoiceMapper voiceMapper) {
        this.voiceRepository = voiceRepository;
        this.voiceMapper = voiceMapper;
    }

    /**
     * Save a voice.
     *
     * @param voiceDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public VoiceDTO save(VoiceDTO voiceDTO) {
        log.debug("Request to save Voice : {}", voiceDTO);
        Voice voice = voiceMapper.toEntity(voiceDTO);
        voice = voiceRepository.save(voice);
        return voiceMapper.toDto(voice);
    }

    /**
     * Get all the voices.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<VoiceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Voices");
        return voiceRepository.findAll(pageable)
            .map(voiceMapper::toDto);
    }


    /**
     * Get one voice by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<VoiceDTO> findOne(Long id) {
        log.debug("Request to get Voice : {}", id);
        return voiceRepository.findById(id)
            .map(voiceMapper::toDto);
    }

    /**
     * Delete the voice by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Voice : {}", id);
        voiceRepository.deleteById(id);
    }
}
