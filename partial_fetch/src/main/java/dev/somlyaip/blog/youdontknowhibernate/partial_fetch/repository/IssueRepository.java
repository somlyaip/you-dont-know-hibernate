package dev.somlyaip.blog.youdontknowhibernate.partial_fetch.repository;

import dev.somlyaip.blog.youdontknowhibernate.partial_fetch.model.entity.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRepository extends JpaRepository<Issue, Long> {
}
