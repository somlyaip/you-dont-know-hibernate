package dev.somlyaip.blog.youdontknowhibernate.osiv.model.dto;

import dev.somlyaip.blog.youdontknowhibernate.osiv.model.enums.FeatureStatus;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeatureDto {

    private Long id;
    private String title;
    private String description;
    private FeatureStatus status;
    private OffsetDateTime createdAt;
    private ProjectDto project;

}
