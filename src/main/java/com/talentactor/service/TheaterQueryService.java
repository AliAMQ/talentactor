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

import com.talentactor.domain.Theater;
import com.talentactor.domain.*; // for static metamodels
import com.talentactor.repository.TheaterRepository;
import com.talentactor.service.dto.TheaterCriteria;

import com.talentactor.service.dto.TheaterDTO;
import com.talentactor.service.mapper.TheaterMapper;

/**
 * Service for executing complex queries for Theater entities in the database.
 * The main input is a {@link TheaterCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TheaterDTO} or a {@link Page} of {@link TheaterDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TheaterQueryService extends QueryService<Theater> {

    private final Logger log = LoggerFactory.getLogger(TheaterQueryService.class);

    private final TheaterRepository theaterRepository;

    private final TheaterMapper theaterMapper;

    public TheaterQueryService(TheaterRepository theaterRepository, TheaterMapper theaterMapper) {
        this.theaterRepository = theaterRepository;
        this.theaterMapper = theaterMapper;
    }

    /**
     * Return a {@link List} of {@link TheaterDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TheaterDTO> findByCriteria(TheaterCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Theater> specification = createSpecification(criteria);
        return theaterMapper.toDto(theaterRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TheaterDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TheaterDTO> findByCriteria(TheaterCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Theater> specification = createSpecification(criteria);
        return theaterRepository.findAll(specification, page)
            .map(theaterMapper::toDto);
    }

    /**
     * Function to convert TheaterCriteria to a {@link Specification}
     */
    private Specification<Theater> createSpecification(TheaterCriteria criteria) {
        Specification<Theater> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Theater_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Theater_.title));
            }
            if (criteria.getDirector() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDirector(), Theater_.director));
            }
            if (criteria.getLink() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLink(), Theater_.link));
            }
            if (criteria.getImagepath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImagepath(), Theater_.imagepath));
            }
            if (criteria.getVideopath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVideopath(), Theater_.videopath));
            }
            if (criteria.getProfileId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getProfileId(), Theater_.profile, Profile_.id));
            }
        }
        return specification;
    }

}
