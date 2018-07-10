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

import com.talentactor.domain.Voice;
import com.talentactor.domain.*; // for static metamodels
import com.talentactor.repository.VoiceRepository;
import com.talentactor.service.dto.VoiceCriteria;

import com.talentactor.service.dto.VoiceDTO;
import com.talentactor.service.mapper.VoiceMapper;

/**
 * Service for executing complex queries for Voice entities in the database.
 * The main input is a {@link VoiceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link VoiceDTO} or a {@link Page} of {@link VoiceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VoiceQueryService extends QueryService<Voice> {

    private final Logger log = LoggerFactory.getLogger(VoiceQueryService.class);

    private final VoiceRepository voiceRepository;

    private final VoiceMapper voiceMapper;

    public VoiceQueryService(VoiceRepository voiceRepository, VoiceMapper voiceMapper) {
        this.voiceRepository = voiceRepository;
        this.voiceMapper = voiceMapper;
    }

    /**
     * Return a {@link List} of {@link VoiceDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<VoiceDTO> findByCriteria(VoiceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Voice> specification = createSpecification(criteria);
        return voiceMapper.toDto(voiceRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link VoiceDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<VoiceDTO> findByCriteria(VoiceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Voice> specification = createSpecification(criteria);
        return voiceRepository.findAll(specification, page)
            .map(voiceMapper::toDto);
    }

    /**
     * Function to convert VoiceCriteria to a {@link Specification}
     */
    private Specification<Voice> createSpecification(VoiceCriteria criteria) {
        Specification<Voice> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Voice_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Voice_.title));
            }
            if (criteria.getDirector() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDirector(), Voice_.director));
            }
            if (criteria.getLink() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLink(), Voice_.link));
            }
            if (criteria.getVideopath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVideopath(), Voice_.videopath));
            }
            if (criteria.getAudiopath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAudiopath(), Voice_.audiopath));
            }
            if (criteria.getProfileId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getProfileId(), Voice_.profile, Profile_.id));
            }
        }
        return specification;
    }

}
