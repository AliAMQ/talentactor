package com.talentactor.service.impl;

import com.talentactor.service.CircusService;
import com.talentactor.domain.Circus;
import com.talentactor.repository.CircusRepository;
import com.talentactor.service.dto.CircusDTO;
import com.talentactor.service.mapper.CircusMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing Circus.
 */
@Service
@Transactional
public class CircusServiceImpl implements CircusService {

    private final Logger log = LoggerFactory.getLogger(CircusServiceImpl.class);

    private final CircusRepository circusRepository;

    private final CircusMapper circusMapper;

    public CircusServiceImpl(CircusRepository circusRepository, CircusMapper circusMapper) {
        this.circusRepository = circusRepository;
        this.circusMapper = circusMapper;
    }

    /**
     * Save a circus.
     *
     * @param circusDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CircusDTO save(CircusDTO circusDTO) {
        log.debug("Request to save Circus : {}", circusDTO);
        Circus circus = circusMapper.toEntity(circusDTO);
        circus = circusRepository.save(circus);
        return circusMapper.toDto(circus);
    }

    /**
     * Get all the circuses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CircusDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Circuses");
        return circusRepository.findAll(pageable)
            .map(circusMapper::toDto);
    }


    /**
     * Get one circus by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CircusDTO> findOne(Long id) {
        log.debug("Request to get Circus : {}", id);
        return circusRepository.findById(id)
            .map(circusMapper::toDto);
    }

    /**
     * Delete the circus by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Circus : {}", id);
        circusRepository.deleteById(id);
    }
}
