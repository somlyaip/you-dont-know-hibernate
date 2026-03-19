package dev.somlyaip.blog.youdontknowhibernate.osiv.model.entity;

import dev.somlyaip.blog.youdontknowhibernate.osiv.model.enums.IssueLevel;
import dev.somlyaip.blog.youdontknowhibernate.osiv.model.enums.IssueStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "issue_seq")
    private Long id;
    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private IssueStatus status;

    @Enumerated(EnumType.STRING)
    private IssueLevel issueLevel;

    @CreatedDate
    @Column(updatable = false)
    private OffsetDateTime createdAt;

    @ManyToOne
    private Project project;

    @OneToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    @JoinColumn(name = "estimation_id", referencedColumnName = "id")
    private Estimation estimation;

}
