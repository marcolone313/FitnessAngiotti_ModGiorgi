package com.oscinnovation.fitness.service.mapper;

import com.oscinnovation.fitness.domain.Esercizio;
import com.oscinnovation.fitness.domain.PassaggioEsercizio;
import com.oscinnovation.fitness.service.dto.EsercizioDTO;
import com.oscinnovation.fitness.service.dto.PassaggioEsercizioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PassaggioEsercizio} and its DTO {@link PassaggioEsercizioDTO}.
 */
@Mapper(componentModel = "spring")
public interface PassaggioEsercizioMapper extends EntityMapper<PassaggioEsercizioDTO, PassaggioEsercizio> {
    @Mapping(target = "esercizio", source = "esercizio", qualifiedByName = "esercizioNome")
    PassaggioEsercizioDTO toDto(PassaggioEsercizio s);

    @Named("esercizioNome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    EsercizioDTO toDtoEsercizioNome(Esercizio esercizio);
}
