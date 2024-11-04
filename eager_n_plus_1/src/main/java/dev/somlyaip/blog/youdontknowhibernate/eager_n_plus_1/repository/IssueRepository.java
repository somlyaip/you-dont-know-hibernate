package dev.somlyaip.blog.youdontknowhibernate.eager_n_plus_1.repository;

import java.util.List;

import dev.somlyaip.blog.youdontknowhibernate.eager_n_plus_1.entity.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface IssueRepository extends JpaRepository<Issue, Long>, JpaSpecificationExecutor<Issue>, QuerydslPredicateExecutor<Issue> {

    List<Issue> findByTitle(String title);

}
