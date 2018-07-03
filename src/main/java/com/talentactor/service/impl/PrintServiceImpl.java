package com.talentactor.service.impl;

import com.talentactor.service.PrintService;
import com.talentactor.domain.Print;
import com.talentactor.repository.PrintRepository;
import com.talentactor.service.dto.PrintDTO;
import com.talentactor.service.mapper.PrintMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing Print.
 */
@Service
@Transactional
public class PrintServiceImpl implements PrintService {

    private final Logger log = LoggerFactory.getLogger(PrintServiceImpl.class);

    private final PrintRepository printRepository;

    private final PrintMapper printMapper;

    public PrintServiceImpl(PrintRepository printRepository, PrintMapper printMapper) {
        this.printRepository = printRepository;
        this.printMapper = printMapper;
    }

    /**
     * Save a print.
     *
     * @param printDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public PrintDTO save(PrintDTO printDTO) {
        log.debug("Request to save Print : {}", printDTO);
        Print print = printMapper.toEntity(printDTO);
        print = printRepository.save(print);
        return printMapper.toDto(print);
    }

    /**
     * Get all the prints.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PrintDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Prints");
        return printRepository.findAll(pageable)
            .map(printMapper::toDto);
    }


    /**
     * Get one print by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PrintDTO> findOne(Long id) {
        log.debug("Request to get Print : {}", id);
        return printRepository.findById(id)
            .map(printMapper::toDto);
    }

    /**
     * Delete the print by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Print : {}", id);
        printRepository.deleteById(id);
    }
}
