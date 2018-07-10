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

import com.talentactor.domain.Swimming;
import com.talentactor.domain.*; // for static metamodels
import com.talentactor.repository.SwimmingRepository;
import com.talentactor.service.dto.SwimmingCriteria;

import com.talentactor.service.dto.SwimmingDTO;
import com.talentactor.service.mapper.SwimmingMapper;

/**
 * Service for executing complex queries for Swimming entities in the database.
 * The main input is a {@link SwimmingCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SwimmingDTO} or a {@link Page} of {@link SwimmingDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SwimmingQueryService extends QueryService<Swimming> {

    private final Logger log = LoggerFactory.getLogger(SwimmingQueryService.class);

    private final SwimmingRepository swimmingRepository;

    private final SwimmingMapper swimmingMapper;

    public SwimmingQueryService(SwimmingRepository swimmingRepository, SwimmingMapper swimmingMapper) {
        this.swimmingRepository = swimmingRepository;
        this.swimmingMapper = swimmingMapper;
    }

    /**
     * Return a {@link List} of {@link SwimmingDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SwimmingDTO> findByCriteria(SwimmingCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Swimming> specification = createSpecification(criteria);
        return swimmingMapper.toDto(swimmingRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SwimmingDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SwimmingDTO> findByCriteria(SwimmingCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Swimming> specification = createSpecification(criteria);
        return swimmingRepository.findAll(specification, page)
            .map(swimmingMapper::toDto);
    }

    /**
     * Function to convert SwimmingCriteria to a {@link Specification}
     */
    private Specification<Swimming> createSpecification(SwimmingCriteria criteria) {
        Specification<Swimming> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Swimming_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Swimming_.title));
            }
            if (criteria.getProfileId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getProfileId(), Swimming_.profiles, Profile_.id));
            }
        }
        return specification;
    }

}
