package com.oscinnovation.fitness.service.mapper;

import com.oscinnovation.fitness.domain.AllenamentoGiornaliero;
import com.oscinnovation.fitness.domain.SchedaSettimanale;
import com.oscinnovation.fitness.service.dto.AllenamentoGiornalieroDTO;
import com.oscinnovation.fitness.service.dto.SchedaSettimanaleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AllenamentoGiornaliero} and its DTO {@link AllenamentoGiornalieroDTO}.
 */
@Mapper(componentModel = "spring")
public interface AllenamentoGiornalieroMapper extends EntityMapper<AllenamentoGiornalieroDTO, AllenamentoGiornaliero> {
    @Mapping(target = "schedaSettimanale", source = "schedaSettimanale", qualifiedByName = "schedaSettimanaleId")
    AllenamentoGiornalieroDTO toDto(AllenamentoGiornaliero s);

    @Named("schedaSettimanaleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SchedaSettimanaleDTO toDtoSchedaSettimanaleId(SchedaSettimanale schedaSettimanale);
}
