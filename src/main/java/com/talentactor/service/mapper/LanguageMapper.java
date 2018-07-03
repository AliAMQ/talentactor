package com.talentactor.service.mapper;

import com.talentactor.domain.*;
import com.talentactor.service.dto.LanguageDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Language and its DTO LanguageDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LanguageMapper extends EntityMapper<LanguageDTO, Language> {


    @Mapping(target = "profiles", ignore = true)
    Language toEntity(LanguageDTO languageDTO);

    default Language fromId(Long id) {
        if (id == null) {
            return null;
        }
        Language language = new Language();
        language.setId(id);
        return language;
    }
}
