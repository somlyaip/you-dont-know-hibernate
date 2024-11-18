package dev.somlyaip.blog.youdontknowhibernate.lazy_multiple_to_one.repository;

import java.util.List;
import java.util.Optional;

import dev.somlyaip.blog.youdontknowhibernate.lazy_multiple_to_one.entity.Issue;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IssueRepository extends JpaRepository<Issue, Long> {

    List<Issue> findAllByDescriptionContains(String description);

    @EntityGraph(Issue.ENTITY_GRAPH_PROJECT)
    Optional<Issue> findWithProjectById(long id);

    @Query("""
        SELECT i
        FROM Issue i JOIN FETCH i.version
        WHERE i.title LIKE %:title%
        """)
    List<Issue> findAllWithVersionByTitleContains(String title, Pageable pageable);
}
