package dev.somlyaip.blog.youdontknowhibernate.osiv.repository;

import dev.somlyaip.blog.youdontknowhibernate.osiv.model.entity.Feature;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeatureRepository extends JpaRepository<Feature, Long> {
}
