package dev.somlyaip.blog.youdontknowhibernate.eager_n_plus_1_multiple_to_one.repository;

import java.util.List;

import dev.somlyaip.blog.youdontknowhibernate.eager_n_plus_1_multiple_to_one.entity.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRepository extends JpaRepository<Issue, Long> {

    List<Issue> findByTitle(String title);

}
