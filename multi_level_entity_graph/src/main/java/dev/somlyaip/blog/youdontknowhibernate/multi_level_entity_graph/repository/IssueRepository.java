package dev.somlyaip.blog.youdontknowhibernate.multi_level_entity_graph.repository;

import java.util.List;

import dev.somlyaip.blog.youdontknowhibernate.multi_level_entity_graph.entity.Issue;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRepository extends JpaRepository<Issue, Long> {

    @EntityGraph(Issue.ENTITY_GRAPH_WITH_BRANCH)
    List<Issue> findByTitleContains(String title);

}
