package com.talentactor.service.mapper;

import com.talentactor.domain.*;
import com.talentactor.service.dto.ProfileDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Profile and its DTO ProfileDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, SkillMapper.class, SportMapper.class, SwimmingMapper.class, CombatMapper.class, LanguageMapper.class, InstrumentMapper.class, WeaponMapper.class, CyclingMapper.class, CircusMapper.class, HorseMapper.class})
public interface ProfileMapper extends EntityMapper<ProfileDTO, Profile> {

    @Mapping(source = "user.id", target = "userId")
    ProfileDTO toDto(Profile profile);

    @Mapping(source = "userId", target = "user")
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "films", ignore = true)
    @Mapping(target = "televisions", ignore = true)
    @Mapping(target = "internets", ignore = true)
    @Mapping(target = "commercials", ignore = true)
    @Mapping(target = "prints", ignore = true)
    @Mapping(target = "theaters", ignore = true)
    @Mapping(target = "voices", ignore = true)
    Profile toEntity(ProfileDTO profileDTO);

    default Profile fromId(Long id) {
        if (id == null) {
            return null;
        }
        Profile profile = new Profile();
        profile.setId(id);
        return profile;
    }
}
