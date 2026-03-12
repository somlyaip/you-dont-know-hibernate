package dev.somlyaip.blog.youdontknowhibernate.partial_fetch.repository;

import dev.somlyaip.blog.youdontknowhibernate.partial_fetch.model.entity.Estimation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstimationRepository extends JpaRepository<Estimation, Long> {
}
