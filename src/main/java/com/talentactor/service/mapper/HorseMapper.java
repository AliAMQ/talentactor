package com.talentactor.service.mapper;

import com.talentactor.domain.*;
import com.talentactor.service.dto.HorseDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Horse and its DTO HorseDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface HorseMapper extends EntityMapper<HorseDTO, Horse> {


    @Mapping(target = "profiles", ignore = true)
    Horse toEntity(HorseDTO horseDTO);

    default Horse fromId(Long id) {
        if (id == null) {
            return null;
        }
        Horse horse = new Horse();
        horse.setId(id);
        return horse;
    }
}
