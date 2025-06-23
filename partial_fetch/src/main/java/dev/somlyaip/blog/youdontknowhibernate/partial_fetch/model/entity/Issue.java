package dev.somlyaip.blog.youdontknowhibernate.partial_fetch.model.entity;

import dev.somlyaip.blog.youdontknowhibernate.partial_fetch.model.enums.IssueLevel;
import dev.somlyaip.blog.youdontknowhibernate.partial_fetch.model.enums.IssueStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Issue {

    @Id
    private Long id;
    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private IssueStatus status;

    @Enumerated(EnumType.STRING)
    private IssueLevel issueLevel;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estimation_id", referencedColumnName = "id")
    private Estimation estimation;

}
