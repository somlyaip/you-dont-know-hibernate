package dev.somlyaip.blog.youdontknowhibernate.partial_fetch.repository;

import dev.somlyaip.blog.youdontknowhibernate.partial_fetch.model.entity.Developer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeveloperRepository extends JpaRepository<Developer, Long> {
}
