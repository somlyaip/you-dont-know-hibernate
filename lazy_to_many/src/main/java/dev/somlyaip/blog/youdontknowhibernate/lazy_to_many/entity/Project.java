package dev.somlyaip.blog.youdontknowhibernate.lazy_to_many.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedEntityGraph(name = Project.ENTITY_GRAPH_WITH_ISSUES, attributeNodes = {
    @NamedAttributeNode("issues")
})
public class Project {

    public static final String ENTITY_GRAPH_WITH_ISSUES = "ENTITY_GRAPH_WITH_ISSUES";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    @OneToMany(
        mappedBy = "project",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @Builder.Default
    private List<Issue> issues = new ArrayList<>();

    public void addIssue(Issue issue) {
        issues.add(issue);
        issue.setProject(this);
    }

}
