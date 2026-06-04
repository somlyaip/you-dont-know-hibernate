package dev.somlyaip.blog.youdontknowhibernate.osiv.repository;

import dev.somlyaip.blog.youdontknowhibernate.osiv.model.entity.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRepository extends JpaRepository<Issue, Long> {
}
