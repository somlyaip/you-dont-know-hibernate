package dev.somlyaip.blog.youdontknowhibernate.lazy_to_many.repository;

import java.util.List;

import dev.somlyaip.blog.youdontknowhibernate.lazy_to_many.entity.Issue;
import dev.somlyaip.blog.youdontknowhibernate.lazy_to_many.entity.Project;
import dev.somlyaip.blog.youdontknowhibernate.lazy_to_many.projection.ProjectIdAndIssueProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IssueRepository extends JpaRepository<Issue, Long> {

    @Query("""
        SELECT
            p.id as projectId,
            i as issue
        FROM
            Issue i JOIN FETCH i.project p
        WHERE i.project IN :projects
        """)
    List<ProjectIdAndIssueProjection> findAllIdAndIssueByProjectIn(List<Project> projects);
}
