package com.talentactor.service.mapper;

import com.talentactor.domain.*;
import com.talentactor.service.dto.FilmDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Film and its DTO FilmDTO.
 */
@Mapper(componentModel = "spring", uses = {ProfileMapper.class})
public interface FilmMapper extends EntityMapper<FilmDTO, Film> {

    @Mapping(source = "profile.id", target = "profileId")
    FilmDTO toDto(Film film);

    @Mapping(source = "profileId", target = "profile")
    Film toEntity(FilmDTO filmDTO);

    default Film fromId(Long id) {
        if (id == null) {
            return null;
        }
        Film film = new Film();
        film.setId(id);
        return film;
    }
}
