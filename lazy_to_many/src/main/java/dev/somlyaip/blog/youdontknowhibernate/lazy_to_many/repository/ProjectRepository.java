package dev.somlyaip.blog.youdontknowhibernate.lazy_to_many.repository;

import java.util.List;

import dev.somlyaip.blog.youdontknowhibernate.lazy_to_many.entity.Project;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findAllByNameContainsIgnoreCase(String projectName, Pageable pageable);

    @EntityGraph(Project.ENTITY_GRAPH_WITH_ISSUES)
    List<Project> findAllWithIssuesByNameContainsIgnoreCase(String projectName, Pageable pageable);

}
