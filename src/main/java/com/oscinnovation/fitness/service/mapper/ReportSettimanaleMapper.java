package com.oscinnovation.fitness.service.mapper;

import com.oscinnovation.fitness.domain.Cliente;
import com.oscinnovation.fitness.domain.ReportSettimanale;
import com.oscinnovation.fitness.domain.SchedaSettimanale;
import com.oscinnovation.fitness.service.dto.ClienteDTO;
import com.oscinnovation.fitness.service.dto.ReportSettimanaleDTO;
import com.oscinnovation.fitness.service.dto.SchedaSettimanaleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReportSettimanale} and its DTO {@link ReportSettimanaleDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReportSettimanaleMapper extends EntityMapper<ReportSettimanaleDTO, ReportSettimanale> {
    @Mapping(target = "schedaSettimanale", source = "schedaSettimanale", qualifiedByName = "schedaSettimanaleId")
    @Mapping(target = "cliente", source = "cliente", qualifiedByName = "clienteEmail")
    ReportSettimanaleDTO toDto(ReportSettimanale s);

    @Named("schedaSettimanaleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SchedaSettimanaleDTO toDtoSchedaSettimanaleId(SchedaSettimanale schedaSettimanale);

    @Named("clienteEmail")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    ClienteDTO toDtoClienteEmail(Cliente cliente);
}
