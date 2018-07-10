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

import com.talentactor.domain.Profile;
import com.talentactor.domain.*; // for static metamodels
import com.talentactor.repository.ProfileRepository;
import com.talentactor.service.dto.ProfileCriteria;

import com.talentactor.service.dto.ProfileDTO;
import com.talentactor.service.mapper.ProfileMapper;

/**
 * Service for executing complex queries for Profile entities in the database.
 * The main input is a {@link ProfileCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProfileDTO} or a {@link Page} of {@link ProfileDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProfileQueryService extends QueryService<Profile> {

    private final Logger log = LoggerFactory.getLogger(ProfileQueryService.class);

    private final ProfileRepository profileRepository;

    private final ProfileMapper profileMapper;

    public ProfileQueryService(ProfileRepository profileRepository, ProfileMapper profileMapper) {
        this.profileRepository = profileRepository;
        this.profileMapper = profileMapper;
    }

    /**
     * Return a {@link List} of {@link ProfileDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProfileDTO> findByCriteria(ProfileCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Profile> specification = createSpecification(criteria);
        return profileMapper.toDto(profileRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProfileDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProfileDTO> findByCriteria(ProfileCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Profile> specification = createSpecification(criteria);
        return profileRepository.findAll(specification, page)
            .map(profileMapper::toDto);
    }

    /**
     * Function to convert ProfileCriteria to a {@link Specification}
     */
    private Specification<Profile> createSpecification(ProfileCriteria criteria) {
        Specification<Profile> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Profile_.id));
            }
            if (criteria.getState() != null) {
                specification = specification.and(buildSpecification(criteria.getState(), Profile_.state));
            }
            if (criteria.getCity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCity(), Profile_.city));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), Profile_.address));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), Profile_.phone));
            }
            if (criteria.getSince() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSince(), Profile_.since));
            }
            if (criteria.getImagepath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImagepath(), Profile_.imagepath));
            }
            if (criteria.getVideopath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVideopath(), Profile_.videopath));
            }
            if (criteria.getAudiopath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAudiopath(), Profile_.audiopath));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getUserId(), Profile_.user, User_.id));
            }
            if (criteria.getRoleId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getRoleId(), Profile_.roles, Role_.id));
            }
            if (criteria.getFilmId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getFilmId(), Profile_.films, Film_.id));
            }
            if (criteria.getTelevisionId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getTelevisionId(), Profile_.televisions, Television_.id));
            }
            if (criteria.getInternetId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getInternetId(), Profile_.internets, Internet_.id));
            }
            if (criteria.getCommercialId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getCommercialId(), Profile_.commercials, Commercial_.id));
            }
            if (criteria.getPrintId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getPrintId(), Profile_.prints, Print_.id));
            }
            if (criteria.getTheaterId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getTheaterId(), Profile_.theaters, Theater_.id));
            }
            if (criteria.getVoiceId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getVoiceId(), Profile_.voices, Voice_.id));
            }
            if (criteria.getSkillId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getSkillId(), Profile_.skills, Skill_.id));
            }
            if (criteria.getSportId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getSportId(), Profile_.sports, Sport_.id));
            }
            if (criteria.getSwimmingId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getSwimmingId(), Profile_.swimmings, Swimming_.id));
            }
            if (criteria.getCombatId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getCombatId(), Profile_.combats, Combat_.id));
            }
            if (criteria.getLanguageId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getLanguageId(), Profile_.languages, Language_.id));
            }
            if (criteria.getInstrumentId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getInstrumentId(), Profile_.instruments, Instrument_.id));
            }
            if (criteria.getWeaponId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getWeaponId(), Profile_.weapons, Weapon_.id));
            }
            if (criteria.getCyclingId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getCyclingId(), Profile_.cyclings, Cycling_.id));
            }
            if (criteria.getCircusId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getCircusId(), Profile_.circuses, Circus_.id));
            }
            if (criteria.getHorseId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getHorseId(), Profile_.horses, Horse_.id));
            }
        }
        return specification;
    }

}
