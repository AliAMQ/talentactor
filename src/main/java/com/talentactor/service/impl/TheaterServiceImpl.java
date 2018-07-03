package com.talentactor.service.impl;

import com.talentactor.service.TheaterService;
import com.talentactor.domain.Theater;
import com.talentactor.repository.TheaterRepository;
import com.talentactor.service.dto.TheaterDTO;
import com.talentactor.service.mapper.TheaterMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing Theater.
 */
@Service
@Transactional
public class TheaterServiceImpl implements TheaterService {

    private final Logger log = LoggerFactory.getLogger(TheaterServiceImpl.class);

    private final TheaterRepository theaterRepository;

    private final TheaterMapper theaterMapper;

    public TheaterServiceImpl(TheaterRepository theaterRepository, TheaterMapper theaterMapper) {
        this.theaterRepository = theaterRepository;
        this.theaterMapper = theaterMapper;
    }

    /**
     * Save a theater.
     *
     * @param theaterDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public TheaterDTO save(TheaterDTO theaterDTO) {
        log.debug("Request to save Theater : {}", theaterDTO);
        Theater theater = theaterMapper.toEntity(theaterDTO);
        theater = theaterRepository.save(theater);
        return theaterMapper.toDto(theater);
    }

    /**
     * Get all the theaters.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TheaterDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Theaters");
        return theaterRepository.findAll(pageable)
            .map(theaterMapper::toDto);
    }


    /**
     * Get one theater by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TheaterDTO> findOne(Long id) {
        log.debug("Request to get Theater : {}", id);
        return theaterRepository.findById(id)
            .map(theaterMapper::toDto);
    }

    /**
     * Delete the theater by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Theater : {}", id);
        theaterRepository.deleteById(id);
    }
}
