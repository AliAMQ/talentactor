package com.talentactor.service.mapper;

import com.talentactor.domain.*;
import com.talentactor.service.dto.RoleDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Role and its DTO RoleDTO.
 */
@Mapper(componentModel = "spring", uses = {ProfileMapper.class, ProjectMapper.class})
public interface RoleMapper extends EntityMapper<RoleDTO, Role> {

    @Mapping(source = "profile.id", target = "profileId")
    @Mapping(source = "project.id", target = "projectId")
    RoleDTO toDto(Role role);

    @Mapping(source = "profileId", target = "profile")
    @Mapping(source = "projectId", target = "project")
    Role toEntity(RoleDTO roleDTO);

    default Role fromId(Long id) {
        if (id == null) {
            return null;
        }
        Role role = new Role();
        role.setId(id);
        return role;
    }
}
