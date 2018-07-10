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

import com.talentactor.domain.Horse;
import com.talentactor.domain.*; // for static metamodels
import com.talentactor.repository.HorseRepository;
import com.talentactor.service.dto.HorseCriteria;

import com.talentactor.service.dto.HorseDTO;
import com.talentactor.service.mapper.HorseMapper;

/**
 * Service for executing complex queries for Horse entities in the database.
 * The main input is a {@link HorseCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link HorseDTO} or a {@link Page} of {@link HorseDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HorseQueryService extends QueryService<Horse> {

    private final Logger log = LoggerFactory.getLogger(HorseQueryService.class);

    private final HorseRepository horseRepository;

    private final HorseMapper horseMapper;

    public HorseQueryService(HorseRepository horseRepository, HorseMapper horseMapper) {
        this.horseRepository = horseRepository;
        this.horseMapper = horseMapper;
    }

    /**
     * Return a {@link List} of {@link HorseDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<HorseDTO> findByCriteria(HorseCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Horse> specification = createSpecification(criteria);
        return horseMapper.toDto(horseRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link HorseDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HorseDTO> findByCriteria(HorseCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Horse> specification = createSpecification(criteria);
        return horseRepository.findAll(specification, page)
            .map(horseMapper::toDto);
    }

    /**
     * Function to convert HorseCriteria to a {@link Specification}
     */
    private Specification<Horse> createSpecification(HorseCriteria criteria) {
        Specification<Horse> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Horse_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Horse_.title));
            }
            if (criteria.getProfileId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getProfileId(), Horse_.profiles, Profile_.id));
            }
        }
        return specification;
    }

}
