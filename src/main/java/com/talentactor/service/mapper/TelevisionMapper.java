package com.talentactor.service.mapper;

import com.talentactor.domain.*;
import com.talentactor.service.dto.TelevisionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Television and its DTO TelevisionDTO.
 */
@Mapper(componentModel = "spring", uses = {ProfileMapper.class})
public interface TelevisionMapper extends EntityMapper<TelevisionDTO, Television> {

    @Mapping(source = "profile.id", target = "profileId")
    TelevisionDTO toDto(Television television);

    @Mapping(source = "profileId", target = "profile")
    Television toEntity(TelevisionDTO televisionDTO);

    default Television fromId(Long id) {
        if (id == null) {
            return null;
        }
        Television television = new Television();
        television.setId(id);
        return television;
    }
}
