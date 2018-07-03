package com.talentactor.service.mapper;

import com.talentactor.domain.*;
import com.talentactor.service.dto.CombatDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Combat and its DTO CombatDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CombatMapper extends EntityMapper<CombatDTO, Combat> {


    @Mapping(target = "profiles", ignore = true)
    Combat toEntity(CombatDTO combatDTO);

    default Combat fromId(Long id) {
        if (id == null) {
            return null;
        }
        Combat combat = new Combat();
        combat.setId(id);
        return combat;
    }
}
