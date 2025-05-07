package com.oscinnovation.fitness.service.mapper;

import com.oscinnovation.fitness.domain.Circonferenza;
import com.oscinnovation.fitness.domain.Cliente;
import com.oscinnovation.fitness.service.dto.CirconferenzaDTO;
import com.oscinnovation.fitness.service.dto.ClienteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Circonferenza} and its DTO {@link CirconferenzaDTO}.
 */
@Mapper(componentModel = "spring")
public interface CirconferenzaMapper extends EntityMapper<CirconferenzaDTO, Circonferenza> {
    @Mapping(target = "cliente", source = "cliente", qualifiedByName = "clienteEmail")
    CirconferenzaDTO toDto(Circonferenza s);

    @Named("clienteEmail")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    ClienteDTO toDtoClienteEmail(Cliente cliente);
}
