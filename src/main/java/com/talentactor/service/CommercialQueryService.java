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

import com.talentactor.domain.Commercial;
import com.talentactor.domain.*; // for static metamodels
import com.talentactor.repository.CommercialRepository;
import com.talentactor.service.dto.CommercialCriteria;

import com.talentactor.service.dto.CommercialDTO;
import com.talentactor.service.mapper.CommercialMapper;

/**
 * Service for executing complex queries for Commercial entities in the database.
 * The main input is a {@link CommercialCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CommercialDTO} or a {@link Page} of {@link CommercialDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CommercialQueryService extends QueryService<Commercial> {

    private final Logger log = LoggerFactory.getLogger(CommercialQueryService.class);

    private final CommercialRepository commercialRepository;

    private final CommercialMapper commercialMapper;

    public CommercialQueryService(CommercialRepository commercialRepository, CommercialMapper commercialMapper) {
        this.commercialRepository = commercialRepository;
        this.commercialMapper = commercialMapper;
    }

    /**
     * Return a {@link List} of {@link CommercialDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CommercialDTO> findByCriteria(CommercialCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Commercial> specification = createSpecification(criteria);
        return commercialMapper.toDto(commercialRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CommercialDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CommercialDTO> findByCriteria(CommercialCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Commercial> specification = createSpecification(criteria);
        return commercialRepository.findAll(specification, page)
            .map(commercialMapper::toDto);
    }

    /**
     * Function to convert CommercialCriteria to a {@link Specification}
     */
    private Specification<Commercial> createSpecification(CommercialCriteria criteria) {
        Specification<Commercial> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Commercial_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Commercial_.title));
            }
            if (criteria.getDirector() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDirector(), Commercial_.director));
            }
            if (criteria.getCameraman() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCameraman(), Commercial_.cameraman));
            }
            if (criteria.getLink() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLink(), Commercial_.link));
            }
            if (criteria.getImagepath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImagepath(), Commercial_.imagepath));
            }
            if (criteria.getVideopath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVideopath(), Commercial_.videopath));
            }
            if (criteria.getProfileId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getProfileId(), Commercial_.profile, Profile_.id));
            }
        }
        return specification;
    }

}
