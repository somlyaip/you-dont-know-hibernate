package dev.somlyaip.blog.youdontknowhibernate.multi_level_entity_graph.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedSubgraph;
import jakarta.persistence.OneToOne;

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
@NamedEntityGraph(
    name = Issue.ENTITY_GRAPH_WITH_BRANCH,
    attributeNodes = {
        @NamedAttributeNode(
            value = "developmentAttributes",
            subgraph = "SUBGRAPH_DEVELOPMENT_ATTRIBUTES_BRANCH"
        )
    },
    subgraphs = {
        @NamedSubgraph(
            name = "SUBGRAPH_DEVELOPMENT_ATTRIBUTES_BRANCH",
            attributeNodes = @NamedAttributeNode("branch")
        )
    }
)
public class Issue {

    public static final String ENTITY_GRAPH_WITH_BRANCH = "ENTITY_GRAPH_WITH_BRANCH";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String title;

    private String description;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "development_attributes_id")
    private DevelopmentAttributes developmentAttributes;

}
