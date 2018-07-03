package com.talentactor.service.mapper;

import com.talentactor.domain.*;
import com.talentactor.service.dto.CircusDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Circus and its DTO CircusDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CircusMapper extends EntityMapper<CircusDTO, Circus> {


    @Mapping(target = "profiles", ignore = true)
    Circus toEntity(CircusDTO circusDTO);

    default Circus fromId(Long id) {
        if (id == null) {
            return null;
        }
        Circus circus = new Circus();
        circus.setId(id);
        return circus;
    }
}
