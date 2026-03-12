package dev.somlyaip.blog.youdontknowhibernate.osiv.model.dto;

import dev.somlyaip.blog.youdontknowhibernate.osiv.model.enums.IssueLevel;
import dev.somlyaip.blog.youdontknowhibernate.osiv.model.enums.IssueStatus;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssueDto {

    private Long id;
    private String title;
    private String description;
    private IssueStatus status;
    private IssueLevel issueLevel;
    private OffsetDateTime createdAt;
    private ProjectDto project;
    private EstimationDto estimation;

}
