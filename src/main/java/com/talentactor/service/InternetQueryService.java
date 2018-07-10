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

import com.talentactor.domain.Internet;
import com.talentactor.domain.*; // for static metamodels
import com.talentactor.repository.InternetRepository;
import com.talentactor.service.dto.InternetCriteria;

import com.talentactor.service.dto.InternetDTO;
import com.talentactor.service.mapper.InternetMapper;

/**
 * Service for executing complex queries for Internet entities in the database.
 * The main input is a {@link InternetCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link InternetDTO} or a {@link Page} of {@link InternetDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InternetQueryService extends QueryService<Internet> {

    private final Logger log = LoggerFactory.getLogger(InternetQueryService.class);

    private final InternetRepository internetRepository;

    private final InternetMapper internetMapper;

    public InternetQueryService(InternetRepository internetRepository, InternetMapper internetMapper) {
        this.internetRepository = internetRepository;
        this.internetMapper = internetMapper;
    }

    /**
     * Return a {@link List} of {@link InternetDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<InternetDTO> findByCriteria(InternetCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Internet> specification = createSpecification(criteria);
        return internetMapper.toDto(internetRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link InternetDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InternetDTO> findByCriteria(InternetCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Internet> specification = createSpecification(criteria);
        return internetRepository.findAll(specification, page)
            .map(internetMapper::toDto);
    }

    /**
     * Function to convert InternetCriteria to a {@link Specification}
     */
    private Specification<Internet> createSpecification(InternetCriteria criteria) {
        Specification<Internet> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Internet_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Internet_.title));
            }
            if (criteria.getDirector() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDirector(), Internet_.director));
            }
            if (criteria.getCameraman() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCameraman(), Internet_.cameraman));
            }
            if (criteria.getLink() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLink(), Internet_.link));
            }
            if (criteria.getImagepath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImagepath(), Internet_.imagepath));
            }
            if (criteria.getVideopath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVideopath(), Internet_.videopath));
            }
            if (criteria.getProfileId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getProfileId(), Internet_.profile, Profile_.id));
            }
        }
        return specification;
    }

}
