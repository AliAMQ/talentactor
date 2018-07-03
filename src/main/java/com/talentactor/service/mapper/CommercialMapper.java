package com.talentactor.service.mapper;

import com.talentactor.domain.*;
import com.talentactor.service.dto.CommercialDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Commercial and its DTO CommercialDTO.
 */
@Mapper(componentModel = "spring", uses = {ProfileMapper.class})
public interface CommercialMapper extends EntityMapper<CommercialDTO, Commercial> {

    @Mapping(source = "profile.id", target = "profileId")
    CommercialDTO toDto(Commercial commercial);

    @Mapping(source = "profileId", target = "profile")
    Commercial toEntity(CommercialDTO commercialDTO);

    default Commercial fromId(Long id) {
        if (id == null) {
            return null;
        }
        Commercial commercial = new Commercial();
        commercial.setId(id);
        return commercial;
    }
}
