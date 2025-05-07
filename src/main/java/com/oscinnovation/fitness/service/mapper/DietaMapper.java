package com.oscinnovation.fitness.service.mapper;

import com.oscinnovation.fitness.domain.Cliente;
import com.oscinnovation.fitness.domain.Dieta;
import com.oscinnovation.fitness.service.dto.ClienteDTO;
import com.oscinnovation.fitness.service.dto.DietaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Dieta} and its DTO {@link DietaDTO}.
 */
@Mapper(componentModel = "spring")
public interface DietaMapper extends EntityMapper<DietaDTO, Dieta> {
    @Mapping(target = "cliente", source = "cliente", qualifiedByName = "clienteEmail")
    DietaDTO toDto(Dieta s);

    @Named("clienteEmail")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    ClienteDTO toDtoClienteEmail(Cliente cliente);
}
