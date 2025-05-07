package com.oscinnovation.fitness.service.mapper;

import com.oscinnovation.fitness.domain.CorsoAcademy;
import com.oscinnovation.fitness.domain.LezioneCorso;
import com.oscinnovation.fitness.service.dto.CorsoAcademyDTO;
import com.oscinnovation.fitness.service.dto.LezioneCorsoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LezioneCorso} and its DTO {@link LezioneCorsoDTO}.
 */
@Mapper(componentModel = "spring")
public interface LezioneCorsoMapper extends EntityMapper<LezioneCorsoDTO, LezioneCorso> {
    @Mapping(target = "corsoAcademy", source = "corsoAcademy", qualifiedByName = "corsoAcademyTitolo")
    LezioneCorsoDTO toDto(LezioneCorso s);

    @Named("corsoAcademyTitolo")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "titolo", source = "titolo")
    CorsoAcademyDTO toDtoCorsoAcademyTitolo(CorsoAcademy corsoAcademy);
}
