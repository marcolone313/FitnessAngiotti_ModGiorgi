package com.oscinnovation.fitness.service.mapper;

import com.oscinnovation.fitness.domain.AllenamentoGiornaliero;
import com.oscinnovation.fitness.domain.Circuito;
import com.oscinnovation.fitness.service.dto.AllenamentoGiornalieroDTO;
import com.oscinnovation.fitness.service.dto.CircuitoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Circuito} and its DTO {@link CircuitoDTO}.
 */
@Mapper(componentModel = "spring")
public interface CircuitoMapper extends EntityMapper<CircuitoDTO, Circuito> {
    @Mapping(target = "allenamentoGiornaliero", source = "allenamentoGiornaliero", qualifiedByName = "allenamentoGiornalieroId")
    CircuitoDTO toDto(Circuito s);

    @Named("allenamentoGiornalieroId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AllenamentoGiornalieroDTO toDtoAllenamentoGiornalieroId(AllenamentoGiornaliero allenamentoGiornaliero);
}
