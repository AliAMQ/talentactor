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

import com.talentactor.domain.Sport;
import com.talentactor.domain.*; // for static metamodels
import com.talentactor.repository.SportRepository;
import com.talentactor.service.dto.SportCriteria;

import com.talentactor.service.dto.SportDTO;
import com.talentactor.service.mapper.SportMapper;

/**
 * Service for executing complex queries for Sport entities in the database.
 * The main input is a {@link SportCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SportDTO} or a {@link Page} of {@link SportDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SportQueryService extends QueryService<Sport> {

    private final Logger log = LoggerFactory.getLogger(SportQueryService.class);

    private final SportRepository sportRepository;

    private final SportMapper sportMapper;

    public SportQueryService(SportRepository sportRepository, SportMapper sportMapper) {
        this.sportRepository = sportRepository;
        this.sportMapper = sportMapper;
    }

    /**
     * Return a {@link List} of {@link SportDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SportDTO> findByCriteria(SportCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Sport> specification = createSpecification(criteria);
        return sportMapper.toDto(sportRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SportDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SportDTO> findByCriteria(SportCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Sport> specification = createSpecification(criteria);
        return sportRepository.findAll(specification, page)
            .map(sportMapper::toDto);
    }

    /**
     * Function to convert SportCriteria to a {@link Specification}
     */
    private Specification<Sport> createSpecification(SportCriteria criteria) {
        Specification<Sport> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Sport_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Sport_.title));
            }
            if (criteria.getProfileId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getProfileId(), Sport_.profiles, Profile_.id));
            }
        }
        return specification;
    }

}
