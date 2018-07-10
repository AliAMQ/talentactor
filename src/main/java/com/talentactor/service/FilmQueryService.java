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

import com.talentactor.domain.Film;
import com.talentactor.domain.*; // for static metamodels
import com.talentactor.repository.FilmRepository;
import com.talentactor.service.dto.FilmCriteria;

import com.talentactor.service.dto.FilmDTO;
import com.talentactor.service.mapper.FilmMapper;

/**
 * Service for executing complex queries for Film entities in the database.
 * The main input is a {@link FilmCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FilmDTO} or a {@link Page} of {@link FilmDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FilmQueryService extends QueryService<Film> {

    private final Logger log = LoggerFactory.getLogger(FilmQueryService.class);

    private final FilmRepository filmRepository;

    private final FilmMapper filmMapper;

    public FilmQueryService(FilmRepository filmRepository, FilmMapper filmMapper) {
        this.filmRepository = filmRepository;
        this.filmMapper = filmMapper;
    }

    /**
     * Return a {@link List} of {@link FilmDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FilmDTO> findByCriteria(FilmCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Film> specification = createSpecification(criteria);
        return filmMapper.toDto(filmRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FilmDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FilmDTO> findByCriteria(FilmCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Film> specification = createSpecification(criteria);
        return filmRepository.findAll(specification, page)
            .map(filmMapper::toDto);
    }

    /**
     * Function to convert FilmCriteria to a {@link Specification}
     */
    private Specification<Film> createSpecification(FilmCriteria criteria) {
        Specification<Film> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Film_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Film_.title));
            }
            if (criteria.getDirector() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDirector(), Film_.director));
            }
            if (criteria.getCameraman() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCameraman(), Film_.cameraman));
            }
            if (criteria.getLink() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLink(), Film_.link));
            }
            if (criteria.getImagepath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImagepath(), Film_.imagepath));
            }
            if (criteria.getVideopath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVideopath(), Film_.videopath));
            }
            if (criteria.getProfileId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getProfileId(), Film_.profile, Profile_.id));
            }
        }
        return specification;
    }

}
