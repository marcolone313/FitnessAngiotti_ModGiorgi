package com.oscinnovation.fitness.service.mapper;

import com.oscinnovation.fitness.domain.Esercizio;
import com.oscinnovation.fitness.service.dto.EsercizioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Esercizio} and its DTO {@link EsercizioDTO}.
 */
@Mapper(componentModel = "spring")
public interface EsercizioMapper extends EntityMapper<EsercizioDTO, Esercizio> {}
