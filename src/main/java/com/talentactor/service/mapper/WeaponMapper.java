package com.talentactor.service.mapper;

import com.talentactor.domain.*;
import com.talentactor.service.dto.WeaponDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Weapon and its DTO WeaponDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface WeaponMapper extends EntityMapper<WeaponDTO, Weapon> {


    @Mapping(target = "profiles", ignore = true)
    Weapon toEntity(WeaponDTO weaponDTO);

    default Weapon fromId(Long id) {
        if (id == null) {
            return null;
        }
        Weapon weapon = new Weapon();
        weapon.setId(id);
        return weapon;
    }
}
