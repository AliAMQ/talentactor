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

import com.talentactor.domain.Combat;
import com.talentactor.domain.*; // for static metamodels
import com.talentactor.repository.CombatRepository;
import com.talentactor.service.dto.CombatCriteria;

import com.talentactor.service.dto.CombatDTO;
import com.talentactor.service.mapper.CombatMapper;

/**
 * Service for executing complex queries for Combat entities in the database.
 * The main input is a {@link CombatCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CombatDTO} or a {@link Page} of {@link CombatDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CombatQueryService extends QueryService<Combat> {

    private final Logger log = LoggerFactory.getLogger(CombatQueryService.class);

    private final CombatRepository combatRepository;

    private final CombatMapper combatMapper;

    public CombatQueryService(CombatRepository combatRepository, CombatMapper combatMapper) {
        this.combatRepository = combatRepository;
        this.combatMapper = combatMapper;
    }

    /**
     * Return a {@link List} of {@link CombatDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CombatDTO> findByCriteria(CombatCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Combat> specification = createSpecification(criteria);
        return combatMapper.toDto(combatRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CombatDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CombatDTO> findByCriteria(CombatCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Combat> specification = createSpecification(criteria);
        return combatRepository.findAll(specification, page)
            .map(combatMapper::toDto);
    }

    /**
     * Function to convert CombatCriteria to a {@link Specification}
     */
    private Specification<Combat> createSpecification(CombatCriteria criteria) {
        Specification<Combat> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Combat_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Combat_.title));
            }
            if (criteria.getProfileId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getProfileId(), Combat_.profiles, Profile_.id));
            }
        }
        return specification;
    }

}
