package com.talentactor.service.mapper;

import com.talentactor.domain.*;
import com.talentactor.service.dto.SportDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Sport and its DTO SportDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SportMapper extends EntityMapper<SportDTO, Sport> {


    @Mapping(target = "profiles", ignore = true)
    Sport toEntity(SportDTO sportDTO);

    default Sport fromId(Long id) {
        if (id == null) {
            return null;
        }
        Sport sport = new Sport();
        sport.setId(id);
        return sport;
    }
}
