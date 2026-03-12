package dev.somlyaip.blog.youdontknowhibernate.osiv.repository;

import dev.somlyaip.blog.youdontknowhibernate.osiv.model.entity.Estimation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstimationRepository extends JpaRepository<Estimation, Long> {
}
