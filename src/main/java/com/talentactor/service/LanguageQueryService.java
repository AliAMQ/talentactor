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

import com.talentactor.domain.Language;
import com.talentactor.domain.*; // for static metamodels
import com.talentactor.repository.LanguageRepository;
import com.talentactor.service.dto.LanguageCriteria;

import com.talentactor.service.dto.LanguageDTO;
import com.talentactor.service.mapper.LanguageMapper;

/**
 * Service for executing complex queries for Language entities in the database.
 * The main input is a {@link LanguageCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LanguageDTO} or a {@link Page} of {@link LanguageDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LanguageQueryService extends QueryService<Language> {

    private final Logger log = LoggerFactory.getLogger(LanguageQueryService.class);

    private final LanguageRepository languageRepository;

    private final LanguageMapper languageMapper;

    public LanguageQueryService(LanguageRepository languageRepository, LanguageMapper languageMapper) {
        this.languageRepository = languageRepository;
        this.languageMapper = languageMapper;
    }

    /**
     * Return a {@link List} of {@link LanguageDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LanguageDTO> findByCriteria(LanguageCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Language> specification = createSpecification(criteria);
        return languageMapper.toDto(languageRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LanguageDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LanguageDTO> findByCriteria(LanguageCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Language> specification = createSpecification(criteria);
        return languageRepository.findAll(specification, page)
            .map(languageMapper::toDto);
    }

    /**
     * Function to convert LanguageCriteria to a {@link Specification}
     */
    private Specification<Language> createSpecification(LanguageCriteria criteria) {
        Specification<Language> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Language_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Language_.title));
            }
            if (criteria.getProfileId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getProfileId(), Language_.profiles, Profile_.id));
            }
        }
        return specification;
    }

}
