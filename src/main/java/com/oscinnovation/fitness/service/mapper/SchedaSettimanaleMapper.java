package com.oscinnovation.fitness.service.mapper;

import com.oscinnovation.fitness.domain.Cliente;
import com.oscinnovation.fitness.domain.SchedaSettimanale;
import com.oscinnovation.fitness.service.dto.ClienteDTO;
import com.oscinnovation.fitness.service.dto.SchedaSettimanaleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SchedaSettimanale} and its DTO {@link SchedaSettimanaleDTO}.
 */
@Mapper(componentModel = "spring")
public interface SchedaSettimanaleMapper extends EntityMapper<SchedaSettimanaleDTO, SchedaSettimanale> {
    @Mapping(target = "cliente", source = "cliente", qualifiedByName = "clienteEmail")
    SchedaSettimanaleDTO toDto(SchedaSettimanale s);

    @Named("clienteEmail")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    ClienteDTO toDtoClienteEmail(Cliente cliente);
}
