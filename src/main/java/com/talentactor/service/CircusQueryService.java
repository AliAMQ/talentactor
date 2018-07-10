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

import com.talentactor.domain.Circus;
import com.talentactor.domain.*; // for static metamodels
import com.talentactor.repository.CircusRepository;
import com.talentactor.service.dto.CircusCriteria;

import com.talentactor.service.dto.CircusDTO;
import com.talentactor.service.mapper.CircusMapper;

/**
 * Service for executing complex queries for Circus entities in the database.
 * The main input is a {@link CircusCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CircusDTO} or a {@link Page} of {@link CircusDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CircusQueryService extends QueryService<Circus> {

    private final Logger log = LoggerFactory.getLogger(CircusQueryService.class);

    private final CircusRepository circusRepository;

    private final CircusMapper circusMapper;

    public CircusQueryService(CircusRepository circusRepository, CircusMapper circusMapper) {
        this.circusRepository = circusRepository;
        this.circusMapper = circusMapper;
    }

    /**
     * Return a {@link List} of {@link CircusDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CircusDTO> findByCriteria(CircusCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Circus> specification = createSpecification(criteria);
        return circusMapper.toDto(circusRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CircusDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CircusDTO> findByCriteria(CircusCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Circus> specification = createSpecification(criteria);
        return circusRepository.findAll(specification, page)
            .map(circusMapper::toDto);
    }

    /**
     * Function to convert CircusCriteria to a {@link Specification}
     */
    private Specification<Circus> createSpecification(CircusCriteria criteria) {
        Specification<Circus> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Circus_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Circus_.title));
            }
            if (criteria.getProfileId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getProfileId(), Circus_.profiles, Profile_.id));
            }
        }
        return specification;
    }

}
