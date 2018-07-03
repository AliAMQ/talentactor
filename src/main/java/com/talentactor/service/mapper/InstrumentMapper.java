package com.talentactor.service.mapper;

import com.talentactor.domain.*;
import com.talentactor.service.dto.InstrumentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Instrument and its DTO InstrumentDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface InstrumentMapper extends EntityMapper<InstrumentDTO, Instrument> {


    @Mapping(target = "profiles", ignore = true)
    Instrument toEntity(InstrumentDTO instrumentDTO);

    default Instrument fromId(Long id) {
        if (id == null) {
            return null;
        }
        Instrument instrument = new Instrument();
        instrument.setId(id);
        return instrument;
    }
}
