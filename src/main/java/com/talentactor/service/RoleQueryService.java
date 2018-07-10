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

import com.talentactor.domain.Role;
import com.talentactor.domain.*; // for static metamodels
import com.talentactor.repository.RoleRepository;
import com.talentactor.service.dto.RoleCriteria;

import com.talentactor.service.dto.RoleDTO;
import com.talentactor.service.mapper.RoleMapper;

/**
 * Service for executing complex queries for Role entities in the database.
 * The main input is a {@link RoleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RoleDTO} or a {@link Page} of {@link RoleDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RoleQueryService extends QueryService<Role> {

    private final Logger log = LoggerFactory.getLogger(RoleQueryService.class);

    private final RoleRepository roleRepository;

    private final RoleMapper roleMapper;

    public RoleQueryService(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    /**
     * Return a {@link List} of {@link RoleDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RoleDTO> findByCriteria(RoleCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Role> specification = createSpecification(criteria);
        return roleMapper.toDto(roleRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RoleDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RoleDTO> findByCriteria(RoleCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Role> specification = createSpecification(criteria);
        return roleRepository.findAll(specification, page)
            .map(roleMapper::toDto);
    }

    /**
     * Function to convert RoleCriteria to a {@link Specification}
     */
    private Specification<Role> createSpecification(RoleCriteria criteria) {
        Specification<Role> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Role_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Role_.title));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Role_.description));
            }
            if (criteria.getProfileId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getProfileId(), Role_.profile, Profile_.id));
            }
            if (criteria.getProjectId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getProjectId(), Role_.project, Project_.id));
            }
        }
        return specification;
    }

}
