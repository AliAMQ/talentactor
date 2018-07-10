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

import com.talentactor.domain.Weapon;
import com.talentactor.domain.*; // for static metamodels
import com.talentactor.repository.WeaponRepository;
import com.talentactor.service.dto.WeaponCriteria;

import com.talentactor.service.dto.WeaponDTO;
import com.talentactor.service.mapper.WeaponMapper;

/**
 * Service for executing complex queries for Weapon entities in the database.
 * The main input is a {@link WeaponCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link WeaponDTO} or a {@link Page} of {@link WeaponDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WeaponQueryService extends QueryService<Weapon> {

    private final Logger log = LoggerFactory.getLogger(WeaponQueryService.class);

    private final WeaponRepository weaponRepository;

    private final WeaponMapper weaponMapper;

    public WeaponQueryService(WeaponRepository weaponRepository, WeaponMapper weaponMapper) {
        this.weaponRepository = weaponRepository;
        this.weaponMapper = weaponMapper;
    }

    /**
     * Return a {@link List} of {@link WeaponDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<WeaponDTO> findByCriteria(WeaponCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Weapon> specification = createSpecification(criteria);
        return weaponMapper.toDto(weaponRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link WeaponDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WeaponDTO> findByCriteria(WeaponCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Weapon> specification = createSpecification(criteria);
        return weaponRepository.findAll(specification, page)
            .map(weaponMapper::toDto);
    }

    /**
     * Function to convert WeaponCriteria to a {@link Specification}
     */
    private Specification<Weapon> createSpecification(WeaponCriteria criteria) {
        Specification<Weapon> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Weapon_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Weapon_.title));
            }
            if (criteria.getProfileId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getProfileId(), Weapon_.profiles, Profile_.id));
            }
        }
        return specification;
    }

}
