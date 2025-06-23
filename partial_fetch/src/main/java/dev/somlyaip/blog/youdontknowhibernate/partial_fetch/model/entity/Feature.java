package dev.somlyaip.blog.youdontknowhibernate.partial_fetch.model.entity;

import dev.somlyaip.blog.youdontknowhibernate.partial_fetch.model.enums.FeatureStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Feature {

    @Id
    private Long id;
    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private FeatureStatus status;

    @OneToMany(mappedBy = "feature")
    private List<ProjectArchitect> architects;

    @OneToMany(mappedBy = "feature")
    private List<Developer> developers;

    @OneToMany(mappedBy = "feature")
    private List<Task> tasks;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estimation_id", referencedColumnName = "id")
    private Estimation estimation;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

}
