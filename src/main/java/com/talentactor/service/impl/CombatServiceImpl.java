package com.talentactor.service.impl;

import com.talentactor.service.CombatService;
import com.talentactor.domain.Combat;
import com.talentactor.repository.CombatRepository;
import com.talentactor.service.dto.CombatDTO;
import com.talentactor.service.mapper.CombatMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing Combat.
 */
@Service
@Transactional
public class CombatServiceImpl implements CombatService {

    private final Logger log = LoggerFactory.getLogger(CombatServiceImpl.class);

    private final CombatRepository combatRepository;

    private final CombatMapper combatMapper;

    public CombatServiceImpl(CombatRepository combatRepository, CombatMapper combatMapper) {
        this.combatRepository = combatRepository;
        this.combatMapper = combatMapper;
    }

    /**
     * Save a combat.
     *
     * @param combatDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CombatDTO save(CombatDTO combatDTO) {
        log.debug("Request to save Combat : {}", combatDTO);
        Combat combat = combatMapper.toEntity(combatDTO);
        combat = combatRepository.save(combat);
        return combatMapper.toDto(combat);
    }

    /**
     * Get all the combats.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CombatDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Combats");
        return combatRepository.findAll(pageable)
            .map(combatMapper::toDto);
    }


    /**
     * Get one combat by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CombatDTO> findOne(Long id) {
        log.debug("Request to get Combat : {}", id);
        return combatRepository.findById(id)
            .map(combatMapper::toDto);
    }

    /**
     * Delete the combat by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Combat : {}", id);
        combatRepository.deleteById(id);
    }
}
