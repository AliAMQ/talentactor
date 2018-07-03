package com.talentactor.service.impl;

import com.talentactor.service.SwimmingService;
import com.talentactor.domain.Swimming;
import com.talentactor.repository.SwimmingRepository;
import com.talentactor.service.dto.SwimmingDTO;
import com.talentactor.service.mapper.SwimmingMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing Swimming.
 */
@Service
@Transactional
public class SwimmingServiceImpl implements SwimmingService {

    private final Logger log = LoggerFactory.getLogger(SwimmingServiceImpl.class);

    private final SwimmingRepository swimmingRepository;

    private final SwimmingMapper swimmingMapper;

    public SwimmingServiceImpl(SwimmingRepository swimmingRepository, SwimmingMapper swimmingMapper) {
        this.swimmingRepository = swimmingRepository;
        this.swimmingMapper = swimmingMapper;
    }

    /**
     * Save a swimming.
     *
     * @param swimmingDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public SwimmingDTO save(SwimmingDTO swimmingDTO) {
        log.debug("Request to save Swimming : {}", swimmingDTO);
        Swimming swimming = swimmingMapper.toEntity(swimmingDTO);
        swimming = swimmingRepository.save(swimming);
        return swimmingMapper.toDto(swimming);
    }

    /**
     * Get all the swimmings.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SwimmingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Swimmings");
        return swimmingRepository.findAll(pageable)
            .map(swimmingMapper::toDto);
    }


    /**
     * Get one swimming by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SwimmingDTO> findOne(Long id) {
        log.debug("Request to get Swimming : {}", id);
        return swimmingRepository.findById(id)
            .map(swimmingMapper::toDto);
    }

    /**
     * Delete the swimming by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Swimming : {}", id);
        swimmingRepository.deleteById(id);
    }
}
