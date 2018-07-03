package com.talentactor.service.mapper;

import com.talentactor.domain.*;
import com.talentactor.service.dto.PrintDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Print and its DTO PrintDTO.
 */
@Mapper(componentModel = "spring", uses = {ProfileMapper.class})
public interface PrintMapper extends EntityMapper<PrintDTO, Print> {

    @Mapping(source = "profile.id", target = "profileId")
    PrintDTO toDto(Print print);

    @Mapping(source = "profileId", target = "profile")
    Print toEntity(PrintDTO printDTO);

    default Print fromId(Long id) {
        if (id == null) {
            return null;
        }
        Print print = new Print();
        print.setId(id);
        return print;
    }
}
