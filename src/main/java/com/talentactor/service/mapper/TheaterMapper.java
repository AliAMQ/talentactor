package com.talentactor.service.mapper;

import com.talentactor.domain.*;
import com.talentactor.service.dto.TheaterDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Theater and its DTO TheaterDTO.
 */
@Mapper(componentModel = "spring", uses = {ProfileMapper.class})
public interface TheaterMapper extends EntityMapper<TheaterDTO, Theater> {

    @Mapping(source = "profile.id", target = "profileId")
    TheaterDTO toDto(Theater theater);

    @Mapping(source = "profileId", target = "profile")
    Theater toEntity(TheaterDTO theaterDTO);

    default Theater fromId(Long id) {
        if (id == null) {
            return null;
        }
        Theater theater = new Theater();
        theater.setId(id);
        return theater;
    }
}
