package dev.somlyaip.blog.youdontknowhibernate.osiv.mapper;

import dev.somlyaip.blog.youdontknowhibernate.osiv.model.dto.EstimationDto;
import dev.somlyaip.blog.youdontknowhibernate.osiv.model.entity.Estimation;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class EstimationMapper {

    public abstract Estimation mapDtoToEstimation(EstimationDto dto);

    public abstract EstimationDto mapEstimationToDto(Estimation estimation);

}
