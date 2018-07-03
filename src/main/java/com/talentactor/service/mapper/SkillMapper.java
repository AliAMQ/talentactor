package com.talentactor.service.mapper;

import com.talentactor.domain.*;
import com.talentactor.service.dto.SkillDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Skill and its DTO SkillDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SkillMapper extends EntityMapper<SkillDTO, Skill> {


    @Mapping(target = "profiles", ignore = true)
    Skill toEntity(SkillDTO skillDTO);

    default Skill fromId(Long id) {
        if (id == null) {
            return null;
        }
        Skill skill = new Skill();
        skill.setId(id);
        return skill;
    }
}
