package dev.somlyaip.blog.youdontknowhibernate.osiv.mapper;

import dev.somlyaip.blog.youdontknowhibernate.osiv.model.dto.FeatureDto;
import dev.somlyaip.blog.youdontknowhibernate.osiv.model.dto.IssueDto;
import dev.somlyaip.blog.youdontknowhibernate.osiv.model.dto.ProjectDto;
import dev.somlyaip.blog.youdontknowhibernate.osiv.model.entity.Feature;
import dev.somlyaip.blog.youdontknowhibernate.osiv.model.entity.Issue;
import dev.somlyaip.blog.youdontknowhibernate.osiv.model.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class ProjectWithoutEstimationMapper {

    @Mapping(target = "project", ignore = true)
    @Mapping(target = "estimation", ignore = true)
    public abstract IssueDto mapIgnoredEstimationIssueToDto(Issue issue);

    @Mapping(target = "project", ignore = true)
    public abstract FeatureDto mapFeatureToDto(Feature feature);

    public abstract ProjectDto mapProjectWithoutEstimation(Project project);

    public abstract List<ProjectDto> mapProjectWithoutEstimation(List<Project> project);

}
