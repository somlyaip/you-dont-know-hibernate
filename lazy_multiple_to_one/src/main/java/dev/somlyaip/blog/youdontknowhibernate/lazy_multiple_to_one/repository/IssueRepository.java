package dev.somlyaip.blog.youdontknowhibernate.lazy_multiple_to_one.repository;

import java.util.List;
import java.util.Optional;

import dev.somlyaip.blog.youdontknowhibernate.lazy_multiple_to_one.entity.Issue;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRepository extends JpaRepository<Issue, Long> {

    List<Issue> findAllByDescriptionContains(String description);

    @EntityGraph(Issue.ENTITY_GRAPH_PROJECT)
    Optional<Issue> findWithProjectById(long id);
}
