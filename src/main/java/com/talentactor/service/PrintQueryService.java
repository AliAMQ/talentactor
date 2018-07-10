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

import com.talentactor.domain.Print;
import com.talentactor.domain.*; // for static metamodels
import com.talentactor.repository.PrintRepository;
import com.talentactor.service.dto.PrintCriteria;

import com.talentactor.service.dto.PrintDTO;
import com.talentactor.service.mapper.PrintMapper;

/**
 * Service for executing complex queries for Print entities in the database.
 * The main input is a {@link PrintCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PrintDTO} or a {@link Page} of {@link PrintDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PrintQueryService extends QueryService<Print> {

    private final Logger log = LoggerFactory.getLogger(PrintQueryService.class);

    private final PrintRepository printRepository;

    private final PrintMapper printMapper;

    public PrintQueryService(PrintRepository printRepository, PrintMapper printMapper) {
        this.printRepository = printRepository;
        this.printMapper = printMapper;
    }

    /**
     * Return a {@link List} of {@link PrintDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PrintDTO> findByCriteria(PrintCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Print> specification = createSpecification(criteria);
        return printMapper.toDto(printRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PrintDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PrintDTO> findByCriteria(PrintCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Print> specification = createSpecification(criteria);
        return printRepository.findAll(specification, page)
            .map(printMapper::toDto);
    }

    /**
     * Function to convert PrintCriteria to a {@link Specification}
     */
    private Specification<Print> createSpecification(PrintCriteria criteria) {
        Specification<Print> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Print_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Print_.title));
            }
            if (criteria.getPhotographer() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhotographer(), Print_.photographer));
            }
            if (criteria.getHair() != null) {
                specification = specification.and(buildStringSpecification(criteria.getHair(), Print_.hair));
            }
            if (criteria.getLink() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLink(), Print_.link));
            }
            if (criteria.getImagepath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImagepath(), Print_.imagepath));
            }
            if (criteria.getProfileId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getProfileId(), Print_.profile, Profile_.id));
            }
        }
        return specification;
    }

}
