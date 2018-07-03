package com.talentactor.service.impl;

import com.talentactor.service.SportService;
import com.talentactor.domain.Sport;
import com.talentactor.repository.SportRepository;
import com.talentactor.service.dto.SportDTO;
import com.talentactor.service.mapper.SportMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing Sport.
 */
@Service
@Transactional
public class SportServiceImpl implements SportService {

    private final Logger log = LoggerFactory.getLogger(SportServiceImpl.class);

    private final SportRepository sportRepository;

    private final SportMapper sportMapper;

    public SportServiceImpl(SportRepository sportRepository, SportMapper sportMapper) {
        this.sportRepository = sportRepository;
        this.sportMapper = sportMapper;
    }

    /**
     * Save a sport.
     *
     * @param sportDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public SportDTO save(SportDTO sportDTO) {
        log.debug("Request to save Sport : {}", sportDTO);
        Sport sport = sportMapper.toEntity(sportDTO);
        sport = sportRepository.save(sport);
        return sportMapper.toDto(sport);
    }

    /**
     * Get all the sports.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SportDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Sports");
        return sportRepository.findAll(pageable)
            .map(sportMapper::toDto);
    }


    /**
     * Get one sport by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SportDTO> findOne(Long id) {
        log.debug("Request to get Sport : {}", id);
        return sportRepository.findById(id)
            .map(sportMapper::toDto);
    }

    /**
     * Delete the sport by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Sport : {}", id);
        sportRepository.deleteById(id);
    }
}
