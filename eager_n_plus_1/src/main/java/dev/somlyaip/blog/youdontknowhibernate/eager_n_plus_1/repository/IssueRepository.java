package dev.somlyaip.blog.youdontknowhibernate.eager_n_plus_1.repository;

import java.util.List;

import dev.somlyaip.blog.youdontknowhibernate.eager_n_plus_1.entity.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IssueRepository extends JpaRepository<Issue, Long>, JpaSpecificationExecutor<Issue> {

    List<Issue> findByTitle(String title);

}
