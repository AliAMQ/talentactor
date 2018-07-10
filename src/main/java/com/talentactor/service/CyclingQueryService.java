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

import com.talentactor.domain.Cycling;
import com.talentactor.domain.*; // for static metamodels
import com.talentactor.repository.CyclingRepository;
import com.talentactor.service.dto.CyclingCriteria;

import com.talentactor.service.dto.CyclingDTO;
import com.talentactor.service.mapper.CyclingMapper;

/**
 * Service for executing complex queries for Cycling entities in the database.
 * The main input is a {@link CyclingCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CyclingDTO} or a {@link Page} of {@link CyclingDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CyclingQueryService extends QueryService<Cycling> {

    private final Logger log = LoggerFactory.getLogger(CyclingQueryService.class);

    private final CyclingRepository cyclingRepository;

    private final CyclingMapper cyclingMapper;

    public CyclingQueryService(CyclingRepository cyclingRepository, CyclingMapper cyclingMapper) {
        this.cyclingRepository = cyclingRepository;
        this.cyclingMapper = cyclingMapper;
    }

    /**
     * Return a {@link List} of {@link CyclingDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CyclingDTO> findByCriteria(CyclingCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Cycling> specification = createSpecification(criteria);
        return cyclingMapper.toDto(cyclingRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CyclingDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CyclingDTO> findByCriteria(CyclingCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Cycling> specification = createSpecification(criteria);
        return cyclingRepository.findAll(specification, page)
            .map(cyclingMapper::toDto);
    }

    /**
     * Function to convert CyclingCriteria to a {@link Specification}
     */
    private Specification<Cycling> createSpecification(CyclingCriteria criteria) {
        Specification<Cycling> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Cycling_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Cycling_.title));
            }
            if (criteria.getProfileId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getProfileId(), Cycling_.profiles, Profile_.id));
            }
        }
        return specification;
    }

}
