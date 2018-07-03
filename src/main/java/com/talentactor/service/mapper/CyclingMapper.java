package com.talentactor.service.mapper;

import com.talentactor.domain.*;
import com.talentactor.service.dto.CyclingDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Cycling and its DTO CyclingDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CyclingMapper extends EntityMapper<CyclingDTO, Cycling> {


    @Mapping(target = "profiles", ignore = true)
    Cycling toEntity(CyclingDTO cyclingDTO);

    default Cycling fromId(Long id) {
        if (id == null) {
            return null;
        }
        Cycling cycling = new Cycling();
        cycling.setId(id);
        return cycling;
    }
}
