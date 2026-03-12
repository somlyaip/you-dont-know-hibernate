package dev.somlyaip.blog.youdontknowhibernate.osiv.mapper;

import dev.somlyaip.blog.youdontknowhibernate.osiv.model.dto.FeatureDto;
import dev.somlyaip.blog.youdontknowhibernate.osiv.model.entity.Feature;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.Set;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class FeatureMapper {

    public abstract Feature mapDtoToFeature(FeatureDto featureDto);

    public abstract Set<Feature> mapDtoToFeature(Set<FeatureDto> featureDtoList);

    public abstract FeatureDto mapFeatureToDto(Feature feature);

    public abstract Set<FeatureDto> mapFeatureToDto(Set<Feature> featureList);

}
