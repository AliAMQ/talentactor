package com.talentactor.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.talentactor.domain.Instrument;
import com.talentactor.domain.*; // for static metamodels
import com.talentactor.repository.InstrumentRepository;
import com.talentactor.service.dto.InstrumentCriteria;

import com.talentactor.service.dto.InstrumentDTO;
import com.talentactor.service.mapper.InstrumentMapper;

/**
 * Service for executing complex queries for Instrument entities in the database.
 * The main input is a {@link InstrumentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link InstrumentDTO} or a {@link Page} of {@link InstrumentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InstrumentQueryService extends QueryService<Instrument> {

    private final Logger log = LoggerFactory.getLogger(InstrumentQueryService.class);

    private final InstrumentRepository instrumentRepository;

    private final InstrumentMapper instrumentMapper;

    public InstrumentQueryService(InstrumentRepository instrumentRepository, InstrumentMapper instrumentMapper) {
        this.instrumentRepository = instrumentRepository;
        this.instrumentMapper = instrumentMapper;
    }

    /**
     * Return a {@link List} of {@link InstrumentDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<InstrumentDTO> findByCriteria(InstrumentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Instrument> specification = createSpecification(criteria);
        return instrumentMapper.toDto(instrumentRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link InstrumentDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InstrumentDTO> findByCriteria(InstrumentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Instrument> specification = createSpecification(criteria);
        return instrumentRepository.findAll(specification, page)
            .map(instrumentMapper::toDto);
    }

    /**
     * Function to convert InstrumentCriteria to a {@link Specification}
     */
    private Specification<Instrument> createSpecification(InstrumentCriteria criteria) {
        Specification<Instrument> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Instrument_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Instrument_.title));
            }
            if (criteria.getProfileId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getProfileId(), Instrument_.profiles, Profile_.id));
            }
        }
        return specification;
    }

}
