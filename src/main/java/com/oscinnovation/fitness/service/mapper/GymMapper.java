package com.oscinnovation.fitness.service.mapper;

import com.oscinnovation.fitness.domain.AllenamentoGiornaliero;
import com.oscinnovation.fitness.domain.Esercizio;
import com.oscinnovation.fitness.domain.Gym;
import com.oscinnovation.fitness.service.dto.AllenamentoGiornalieroDTO;
import com.oscinnovation.fitness.service.dto.EsercizioDTO;
import com.oscinnovation.fitness.service.dto.GymDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Gym} and its DTO {@link GymDTO}.
 */
@Mapper(componentModel = "spring")
public interface GymMapper extends EntityMapper<GymDTO, Gym> {
    @Mapping(target = "allenamentoGiornaliero", source = "allenamentoGiornaliero", qualifiedByName = "allenamentoGiornalieroId")
    @Mapping(target = "esercizio", source = "esercizio", qualifiedByName = "esercizioNome")
    GymDTO toDto(Gym s);

    @Named("allenamentoGiornalieroId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AllenamentoGiornalieroDTO toDtoAllenamentoGiornalieroId(AllenamentoGiornaliero allenamentoGiornaliero);

    @Named("esercizioNome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    EsercizioDTO toDtoEsercizioNome(Esercizio esercizio);
}
