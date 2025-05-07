package com.oscinnovation.fitness.service.mapper;

import com.oscinnovation.fitness.domain.AllenamentoGiornaliero;
import com.oscinnovation.fitness.domain.Corsa;
import com.oscinnovation.fitness.service.dto.AllenamentoGiornalieroDTO;
import com.oscinnovation.fitness.service.dto.CorsaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Corsa} and its DTO {@link CorsaDTO}.
 */
@Mapper(componentModel = "spring")
public interface CorsaMapper extends EntityMapper<CorsaDTO, Corsa> {
    @Mapping(target = "allenamentoGiornaliero", source = "allenamentoGiornaliero", qualifiedByName = "allenamentoGiornalieroId")
    CorsaDTO toDto(Corsa s);

    @Named("allenamentoGiornalieroId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AllenamentoGiornalieroDTO toDtoAllenamentoGiornalieroId(AllenamentoGiornaliero allenamentoGiornaliero);
}
