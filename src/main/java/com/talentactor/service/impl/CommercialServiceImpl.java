package com.talentactor.service.impl;

import com.talentactor.service.CommercialService;
import com.talentactor.domain.Commercial;
import com.talentactor.repository.CommercialRepository;
import com.talentactor.service.dto.CommercialDTO;
import com.talentactor.service.mapper.CommercialMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing Commercial.
 */
@Service
@Transactional
public class CommercialServiceImpl implements CommercialService {

    private final Logger log = LoggerFactory.getLogger(CommercialServiceImpl.class);

    private final CommercialRepository commercialRepository;

    private final CommercialMapper commercialMapper;

    public CommercialServiceImpl(CommercialRepository commercialRepository, CommercialMapper commercialMapper) {
        this.commercialRepository = commercialRepository;
        this.commercialMapper = commercialMapper;
    }

    /**
     * Save a commercial.
     *
     * @param commercialDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CommercialDTO save(CommercialDTO commercialDTO) {
        log.debug("Request to save Commercial : {}", commercialDTO);
        Commercial commercial = commercialMapper.toEntity(commercialDTO);
        commercial = commercialRepository.save(commercial);
        return commercialMapper.toDto(commercial);
    }

    /**
     * Get all the commercials.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CommercialDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Commercials");
        return commercialRepository.findAll(pageable)
            .map(commercialMapper::toDto);
    }


    /**
     * Get one commercial by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CommercialDTO> findOne(Long id) {
        log.debug("Request to get Commercial : {}", id);
        return commercialRepository.findById(id)
            .map(commercialMapper::toDto);
    }

    /**
     * Delete the commercial by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Commercial : {}", id);
        commercialRepository.deleteById(id);
    }
}
