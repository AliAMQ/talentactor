package com.talentactor.service.mapper;

import com.talentactor.domain.*;
import com.talentactor.service.dto.SwimmingDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Swimming and its DTO SwimmingDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SwimmingMapper extends EntityMapper<SwimmingDTO, Swimming> {


    @Mapping(target = "profiles", ignore = true)
    Swimming toEntity(SwimmingDTO swimmingDTO);

    default Swimming fromId(Long id) {
        if (id == null) {
            return null;
        }
        Swimming swimming = new Swimming();
        swimming.setId(id);
        return swimming;
    }
}
