package com.talentactor.service.impl;

import com.talentactor.service.InstrumentService;
import com.talentactor.domain.Instrument;
import com.talentactor.repository.InstrumentRepository;
import com.talentactor.service.dto.InstrumentDTO;
import com.talentactor.service.mapper.InstrumentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing Instrument.
 */
@Service
@Transactional
public class InstrumentServiceImpl implements InstrumentService {

    private final Logger log = LoggerFactory.getLogger(InstrumentServiceImpl.class);

    private final InstrumentRepository instrumentRepository;

    private final InstrumentMapper instrumentMapper;

    public InstrumentServiceImpl(InstrumentRepository instrumentRepository, InstrumentMapper instrumentMapper) {
        this.instrumentRepository = instrumentRepository;
        this.instrumentMapper = instrumentMapper;
    }

    /**
     * Save a instrument.
     *
     * @param instrumentDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public InstrumentDTO save(InstrumentDTO instrumentDTO) {
        log.debug("Request to save Instrument : {}", instrumentDTO);
        Instrument instrument = instrumentMapper.toEntity(instrumentDTO);
        instrument = instrumentRepository.save(instrument);
        return instrumentMapper.toDto(instrument);
    }

    /**
     * Get all the instruments.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<InstrumentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Instruments");
        return instrumentRepository.findAll(pageable)
            .map(instrumentMapper::toDto);
    }


    /**
     * Get one instrument by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<InstrumentDTO> findOne(Long id) {
        log.debug("Request to get Instrument : {}", id);
        return instrumentRepository.findById(id)
            .map(instrumentMapper::toDto);
    }

    /**
     * Delete the instrument by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Instrument : {}", id);
        instrumentRepository.deleteById(id);
    }
}
