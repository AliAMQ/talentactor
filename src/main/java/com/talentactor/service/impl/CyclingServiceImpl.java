package com.talentactor.service.impl;

import com.talentactor.service.CyclingService;
import com.talentactor.domain.Cycling;
import com.talentactor.repository.CyclingRepository;
import com.talentactor.service.dto.CyclingDTO;
import com.talentactor.service.mapper.CyclingMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing Cycling.
 */
@Service
@Transactional
public class CyclingServiceImpl implements CyclingService {

    private final Logger log = LoggerFactory.getLogger(CyclingServiceImpl.class);

    private final CyclingRepository cyclingRepository;

    private final CyclingMapper cyclingMapper;

    public CyclingServiceImpl(CyclingRepository cyclingRepository, CyclingMapper cyclingMapper) {
        this.cyclingRepository = cyclingRepository;
        this.cyclingMapper = cyclingMapper;
    }

    /**
     * Save a cycling.
     *
     * @param cyclingDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CyclingDTO save(CyclingDTO cyclingDTO) {
        log.debug("Request to save Cycling : {}", cyclingDTO);
        Cycling cycling = cyclingMapper.toEntity(cyclingDTO);
        cycling = cyclingRepository.save(cycling);
        return cyclingMapper.toDto(cycling);
    }

    /**
     * Get all the cyclings.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CyclingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Cyclings");
        return cyclingRepository.findAll(pageable)
            .map(cyclingMapper::toDto);
    }


    /**
     * Get one cycling by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CyclingDTO> findOne(Long id) {
        log.debug("Request to get Cycling : {}", id);
        return cyclingRepository.findById(id)
            .map(cyclingMapper::toDto);
    }

    /**
     * Delete the cycling by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Cycling : {}", id);
        cyclingRepository.deleteById(id);
    }
}
