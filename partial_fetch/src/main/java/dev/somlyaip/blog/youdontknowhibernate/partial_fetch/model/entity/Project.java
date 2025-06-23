package dev.somlyaip.blog.youdontknowhibernate.partial_fetch.model.entity;

import dev.somlyaip.blog.youdontknowhibernate.partial_fetch.model.enums.ProjectStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Project {

    @Id
    private Long id;
    private String projectName;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    @OneToMany(mappedBy = "project")
    private List<Issue> issues;

    @OneToMany(mappedBy = "project")
    private List<Feature> features;

}
