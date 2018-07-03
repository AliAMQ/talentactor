package com.talentactor.service.impl;

import com.talentactor.service.HorseService;
import com.talentactor.domain.Horse;
import com.talentactor.repository.HorseRepository;
import com.talentactor.service.dto.HorseDTO;
import com.talentactor.service.mapper.HorseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing Horse.
 */
@Service
@Transactional
public class HorseServiceImpl implements HorseService {

    private final Logger log = LoggerFactory.getLogger(HorseServiceImpl.class);

    private final HorseRepository horseRepository;

    private final HorseMapper horseMapper;

    public HorseServiceImpl(HorseRepository horseRepository, HorseMapper horseMapper) {
        this.horseRepository = horseRepository;
        this.horseMapper = horseMapper;
    }

    /**
     * Save a horse.
     *
     * @param horseDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public HorseDTO save(HorseDTO horseDTO) {
        log.debug("Request to save Horse : {}", horseDTO);
        Horse horse = horseMapper.toEntity(horseDTO);
        horse = horseRepository.save(horse);
        return horseMapper.toDto(horse);
    }

    /**
     * Get all the horses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<HorseDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Horses");
        return horseRepository.findAll(pageable)
            .map(horseMapper::toDto);
    }


    /**
     * Get one horse by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<HorseDTO> findOne(Long id) {
        log.debug("Request to get Horse : {}", id);
        return horseRepository.findById(id)
            .map(horseMapper::toDto);
    }

    /**
     * Delete the horse by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Horse : {}", id);
        horseRepository.deleteById(id);
    }
}
