package dev.somlyaip.blog.youdontknowhibernate.lazy_multiple_to_one.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.SequenceGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@NamedEntityGraph(
    name = Issue.ENTITY_GRAPH_PROJECT,
    attributeNodes = {
        @NamedAttributeNode("project")
    }
)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Issue {

    public static final String ENTITY_GRAPH_PROJECT = "ENTITY_GRAPH_PROJECT";

    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "issue_seq"
    )
    private Long id;
    private String title;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "version_id")
    private Version version;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estimation_id")
    private Estimation estimation;
}
