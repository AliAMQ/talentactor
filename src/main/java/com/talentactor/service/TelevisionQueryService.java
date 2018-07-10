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

import com.talentactor.domain.Television;
import com.talentactor.domain.*; // for static metamodels
import com.talentactor.repository.TelevisionRepository;
import com.talentactor.service.dto.TelevisionCriteria;

import com.talentactor.service.dto.TelevisionDTO;
import com.talentactor.service.mapper.TelevisionMapper;

/**
 * Service for executing complex queries for Television entities in the database.
 * The main input is a {@link TelevisionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TelevisionDTO} or a {@link Page} of {@link TelevisionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TelevisionQueryService extends QueryService<Television> {

    private final Logger log = LoggerFactory.getLogger(TelevisionQueryService.class);

    private final TelevisionRepository televisionRepository;

    private final TelevisionMapper televisionMapper;

    public TelevisionQueryService(TelevisionRepository televisionRepository, TelevisionMapper televisionMapper) {
        this.televisionRepository = televisionRepository;
        this.televisionMapper = televisionMapper;
    }

    /**
     * Return a {@link List} of {@link TelevisionDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TelevisionDTO> findByCriteria(TelevisionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Television> specification = createSpecification(criteria);
        return televisionMapper.toDto(televisionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TelevisionDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TelevisionDTO> findByCriteria(TelevisionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Television> specification = createSpecification(criteria);
        return televisionRepository.findAll(specification, page)
            .map(televisionMapper::toDto);
    }

    /**
     * Function to convert TelevisionCriteria to a {@link Specification}
     */
    private Specification<Television> createSpecification(TelevisionCriteria criteria) {
        Specification<Television> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Television_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Television_.title));
            }
            if (criteria.getDirector() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDirector(), Television_.director));
            }
            if (criteria.getCameraman() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCameraman(), Television_.cameraman));
            }
            if (criteria.getLink() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLink(), Television_.link));
            }
            if (criteria.getImagepath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImagepath(), Television_.imagepath));
            }
            if (criteria.getVideopath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVideopath(), Television_.videopath));
            }
            if (criteria.getProfileId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getProfileId(), Television_.profile, Profile_.id));
            }
        }
        return specification;
    }

}
