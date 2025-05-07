package com.oscinnovation.fitness.service.mapper;

import com.oscinnovation.fitness.domain.Circuito;
import com.oscinnovation.fitness.domain.CircuitoToEsercizio;
import com.oscinnovation.fitness.domain.Esercizio;
import com.oscinnovation.fitness.service.dto.CircuitoDTO;
import com.oscinnovation.fitness.service.dto.CircuitoToEsercizioDTO;
import com.oscinnovation.fitness.service.dto.EsercizioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CircuitoToEsercizio} and its DTO {@link CircuitoToEsercizioDTO}.
 */
@Mapper(componentModel = "spring")
public interface CircuitoToEsercizioMapper extends EntityMapper<CircuitoToEsercizioDTO, CircuitoToEsercizio> {
    @Mapping(target = "esercizio", source = "esercizio", qualifiedByName = "esercizioNome")
    @Mapping(target = "circuito", source = "circuito", qualifiedByName = "circuitoId")
    CircuitoToEsercizioDTO toDto(CircuitoToEsercizio s);

    @Named("esercizioNome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    EsercizioDTO toDtoEsercizioNome(Esercizio esercizio);

    @Named("circuitoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CircuitoDTO toDtoCircuitoId(Circuito circuito);
}
