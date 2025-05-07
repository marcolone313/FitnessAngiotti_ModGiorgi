package com.oscinnovation.fitness.service.mapper;

import com.oscinnovation.fitness.domain.Cliente;
import com.oscinnovation.fitness.domain.ClienteToLezioneCorso;
import com.oscinnovation.fitness.domain.LezioneCorso;
import com.oscinnovation.fitness.service.dto.ClienteDTO;
import com.oscinnovation.fitness.service.dto.ClienteToLezioneCorsoDTO;
import com.oscinnovation.fitness.service.dto.LezioneCorsoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ClienteToLezioneCorso} and its DTO {@link ClienteToLezioneCorsoDTO}.
 */
@Mapper(componentModel = "spring")
public interface ClienteToLezioneCorsoMapper extends EntityMapper<ClienteToLezioneCorsoDTO, ClienteToLezioneCorso> {
    @Mapping(target = "cliente", source = "cliente", qualifiedByName = "clienteEmail")
    @Mapping(target = "lezioneCorso", source = "lezioneCorso", qualifiedByName = "lezioneCorsoTitolo")
    ClienteToLezioneCorsoDTO toDto(ClienteToLezioneCorso s);

    @Named("clienteEmail")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    ClienteDTO toDtoClienteEmail(Cliente cliente);

    @Named("lezioneCorsoTitolo")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "titolo", source = "titolo")
    LezioneCorsoDTO toDtoLezioneCorsoTitolo(LezioneCorso lezioneCorso);
}
