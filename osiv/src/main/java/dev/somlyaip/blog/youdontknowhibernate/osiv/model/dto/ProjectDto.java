package dev.somlyaip.blog.youdontknowhibernate.osiv.model.dto;

import dev.somlyaip.blog.youdontknowhibernate.osiv.model.enums.ProjectStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
public class ProjectDto {

    private Long id;
    private String projectName;
    private ProjectStatus status;
    private OffsetDateTime createdAt;
    private ProjectArchitectDto projectArchitect;
    private List<IssueDto> issues;
    private Set<FeatureDto> features;

}
