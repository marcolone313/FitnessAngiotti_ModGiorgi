package com.oscinnovation.fitness.service.mapper;

import com.oscinnovation.fitness.domain.Cliente;
import com.oscinnovation.fitness.domain.Plicometria;
import com.oscinnovation.fitness.service.dto.ClienteDTO;
import com.oscinnovation.fitness.service.dto.PlicometriaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Plicometria} and its DTO {@link PlicometriaDTO}.
 */
@Mapper(componentModel = "spring")
public interface PlicometriaMapper extends EntityMapper<PlicometriaDTO, Plicometria> {
    @Mapping(target = "cliente", source = "cliente", qualifiedByName = "clienteEmail")
    PlicometriaDTO toDto(Plicometria s);

    @Named("clienteEmail")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    ClienteDTO toDtoClienteEmail(Cliente cliente);
}
