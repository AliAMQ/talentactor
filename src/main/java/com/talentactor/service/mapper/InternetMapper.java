package com.talentactor.service.mapper;

import com.talentactor.domain.*;
import com.talentactor.service.dto.InternetDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Internet and its DTO InternetDTO.
 */
@Mapper(componentModel = "spring", uses = {ProfileMapper.class})
public interface InternetMapper extends EntityMapper<InternetDTO, Internet> {

    @Mapping(source = "profile.id", target = "profileId")
    InternetDTO toDto(Internet internet);

    @Mapping(source = "profileId", target = "profile")
    Internet toEntity(InternetDTO internetDTO);

    default Internet fromId(Long id) {
        if (id == null) {
            return null;
        }
        Internet internet = new Internet();
        internet.setId(id);
        return internet;
    }
}
