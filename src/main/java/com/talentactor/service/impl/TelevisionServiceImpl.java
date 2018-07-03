package com.talentactor.service.impl;

import com.talentactor.service.TelevisionService;
import com.talentactor.domain.Television;
import com.talentactor.repository.TelevisionRepository;
import com.talentactor.service.dto.TelevisionDTO;
import com.talentactor.service.mapper.TelevisionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing Television.
 */
@Service
@Transactional
public class TelevisionServiceImpl implements TelevisionService {

    private final Logger log = LoggerFactory.getLogger(TelevisionServiceImpl.class);

    private final TelevisionRepository televisionRepository;

    private final TelevisionMapper televisionMapper;

    public TelevisionServiceImpl(TelevisionRepository televisionRepository, TelevisionMapper televisionMapper) {
        this.televisionRepository = televisionRepository;
        this.televisionMapper = televisionMapper;
    }

    /**
     * Save a television.
     *
     * @param televisionDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public TelevisionDTO save(TelevisionDTO televisionDTO) {
        log.debug("Request to save Television : {}", televisionDTO);
        Television television = televisionMapper.toEntity(televisionDTO);
        television = televisionRepository.save(television);
        return televisionMapper.toDto(television);
    }

    /**
     * Get all the televisions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TelevisionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Televisions");
        return televisionRepository.findAll(pageable)
            .map(televisionMapper::toDto);
    }


    /**
     * Get one television by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TelevisionDTO> findOne(Long id) {
        log.debug("Request to get Television : {}", id);
        return televisionRepository.findById(id)
            .map(televisionMapper::toDto);
    }

    /**
     * Delete the television by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Television : {}", id);
        televisionRepository.deleteById(id);
    }
}
