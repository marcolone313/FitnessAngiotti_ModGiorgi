package com.oscinnovation.fitness.service.mapper;

import com.oscinnovation.fitness.domain.Cliente;
import com.oscinnovation.fitness.domain.PesoCliente;
import com.oscinnovation.fitness.service.dto.ClienteDTO;
import com.oscinnovation.fitness.service.dto.PesoClienteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PesoCliente} and its DTO {@link PesoClienteDTO}.
 */
@Mapper(componentModel = "spring")
public interface PesoClienteMapper extends EntityMapper<PesoClienteDTO, PesoCliente> {
    @Mapping(target = "cliente", source = "cliente", qualifiedByName = "clienteEmail")
    PesoClienteDTO toDto(PesoCliente s);

    @Named("clienteEmail")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    ClienteDTO toDtoClienteEmail(Cliente cliente);
}
