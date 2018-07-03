package com.talentactor.service.mapper;

import com.talentactor.domain.*;
import com.talentactor.service.dto.VoiceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Voice and its DTO VoiceDTO.
 */
@Mapper(componentModel = "spring", uses = {ProfileMapper.class})
public interface VoiceMapper extends EntityMapper<VoiceDTO, Voice> {

    @Mapping(source = "profile.id", target = "profileId")
    VoiceDTO toDto(Voice voice);

    @Mapping(source = "profileId", target = "profile")
    Voice toEntity(VoiceDTO voiceDTO);

    default Voice fromId(Long id) {
        if (id == null) {
            return null;
        }
        Voice voice = new Voice();
        voice.setId(id);
        return voice;
    }
}
