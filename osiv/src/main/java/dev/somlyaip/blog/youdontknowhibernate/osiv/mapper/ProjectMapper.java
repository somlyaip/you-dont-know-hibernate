package dev.somlyaip.blog.youdontknowhibernate.osiv.mapper;

import dev.somlyaip.blog.youdontknowhibernate.osiv.model.dto.FeatureDto;
import dev.somlyaip.blog.youdontknowhibernate.osiv.model.dto.IssueDto;
import dev.somlyaip.blog.youdontknowhibernate.osiv.model.dto.ProjectDto;
import dev.somlyaip.blog.youdontknowhibernate.osiv.model.entity.Feature;
import dev.somlyaip.blog.youdontknowhibernate.osiv.model.entity.Issue;
import dev.somlyaip.blog.youdontknowhibernate.osiv.model.entity.Project;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class ProjectMapper {

    public abstract ProjectDto mapWithRelations(Project project);

    public abstract List<ProjectDto> mapWithRelations(List<Project> project);

    @Named("mapWithoutRelations")
    @Mapping(target = "projectArchitect", ignore = true)
    @Mapping(target = "issues", ignore = true)
    @Mapping(target = "features", ignore = true)
    public abstract ProjectDto mapWithoutRelations(Project project);

    @IterableMapping(qualifiedByName = "mapWithoutRelations")
    public abstract List<ProjectDto> mapWithoutRelations(List<Project> project);

    @Mapping(target = "project", ignore = true)
    public abstract IssueDto mapIssueToDto(Issue issue);

    @Mapping(target = "project", ignore = true)
    public abstract FeatureDto mapFeatureToDto(Feature feature);

}
