package dev.somlyaip.blog.youdontknowhibernate.osiv.model.entity;

import dev.somlyaip.blog.youdontknowhibernate.osiv.model.enums.ProjectStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "project_seq")
    private Long id;
    private String projectName;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    @CreatedDate
    @Column(updatable = false)
    private OffsetDateTime createdAt;

    /**
     * If the open-session-in-view is enabled, despite lazy loading, the mapper in the web layer will call the getters
     * and generate as many selects as there are relationships.
     * With the @Fetch annotation:
     * `SELECT` is the default - a separate select for each relationship.
     * `JOIN` - only by ID.
     * `SUBSELECT` - invalid in toOne case.
     * When open-session-in-view = true, despite being lazy, if an entity is accessed by a mapper,
     * for example in a controller - entity-to-DTO mapper,
     * additional queries might occur because the persistence context remains open.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private ProjectArchitect projectArchitect;

    /**
     * If it was FetchType.EAGER, it would always load even when it's not needed, with as many selects as there are issues.
     */
    @OneToMany(
            mappedBy = "project",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Issue> issues;

    @OneToMany(
            mappedBy = "project",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<Feature> features;

}
