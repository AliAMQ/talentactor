package com.talentactor.service.impl;

import com.talentactor.service.WeaponService;
import com.talentactor.domain.Weapon;
import com.talentactor.repository.WeaponRepository;
import com.talentactor.service.dto.WeaponDTO;
import com.talentactor.service.mapper.WeaponMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing Weapon.
 */
@Service
@Transactional
public class WeaponServiceImpl implements WeaponService {

    private final Logger log = LoggerFactory.getLogger(WeaponServiceImpl.class);

    private final WeaponRepository weaponRepository;

    private final WeaponMapper weaponMapper;

    public WeaponServiceImpl(WeaponRepository weaponRepository, WeaponMapper weaponMapper) {
        this.weaponRepository = weaponRepository;
        this.weaponMapper = weaponMapper;
    }

    /**
     * Save a weapon.
     *
     * @param weaponDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public WeaponDTO save(WeaponDTO weaponDTO) {
        log.debug("Request to save Weapon : {}", weaponDTO);
        Weapon weapon = weaponMapper.toEntity(weaponDTO);
        weapon = weaponRepository.save(weapon);
        return weaponMapper.toDto(weapon);
    }

    /**
     * Get all the weapons.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<WeaponDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Weapons");
        return weaponRepository.findAll(pageable)
            .map(weaponMapper::toDto);
    }


    /**
     * Get one weapon by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<WeaponDTO> findOne(Long id) {
        log.debug("Request to get Weapon : {}", id);
        return weaponRepository.findById(id)
            .map(weaponMapper::toDto);
    }

    /**
     * Delete the weapon by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Weapon : {}", id);
        weaponRepository.deleteById(id);
    }
}
