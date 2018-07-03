package com.talentactor.service.impl;

import com.talentactor.service.InternetService;
import com.talentactor.domain.Internet;
import com.talentactor.repository.InternetRepository;
import com.talentactor.service.dto.InternetDTO;
import com.talentactor.service.mapper.InternetMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing Internet.
 */
@Service
@Transactional
public class InternetServiceImpl implements InternetService {

    private final Logger log = LoggerFactory.getLogger(InternetServiceImpl.class);

    private final InternetRepository internetRepository;

    private final InternetMapper internetMapper;

    public InternetServiceImpl(InternetRepository internetRepository, InternetMapper internetMapper) {
        this.internetRepository = internetRepository;
        this.internetMapper = internetMapper;
    }

    /**
     * Save a internet.
     *
     * @param internetDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public InternetDTO save(InternetDTO internetDTO) {
        log.debug("Request to save Internet : {}", internetDTO);
        Internet internet = internetMapper.toEntity(internetDTO);
        internet = internetRepository.save(internet);
        return internetMapper.toDto(internet);
    }

    /**
     * Get all the internets.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<InternetDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Internets");
        return internetRepository.findAll(pageable)
            .map(internetMapper::toDto);
    }


    /**
     * Get one internet by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<InternetDTO> findOne(Long id) {
        log.debug("Request to get Internet : {}", id);
        return internetRepository.findById(id)
            .map(internetMapper::toDto);
    }

    /**
     * Delete the internet by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Internet : {}", id);
        internetRepository.deleteById(id);
    }
}
