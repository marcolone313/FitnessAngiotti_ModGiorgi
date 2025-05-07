package com.oscinnovation.fitness.service.mapper;

import com.oscinnovation.fitness.domain.CorsoAcademy;
import com.oscinnovation.fitness.service.dto.CorsoAcademyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CorsoAcademy} and its DTO {@link CorsoAcademyDTO}.
 */
@Mapper(componentModel = "spring")
public interface CorsoAcademyMapper extends EntityMapper<CorsoAcademyDTO, CorsoAcademy> {}
