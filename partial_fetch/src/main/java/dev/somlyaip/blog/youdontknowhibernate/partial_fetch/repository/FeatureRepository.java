package dev.somlyaip.blog.youdontknowhibernate.partial_fetch.repository;

import dev.somlyaip.blog.youdontknowhibernate.partial_fetch.model.entity.Feature;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FeatureRepository extends JpaRepository<Feature, Long> {

    @EntityGraph(attributePaths = {"estimation", "tasks"})
    @Query("SELECT f FROM Feature f WHERE f.project.id IN :ids")
    List<Feature> findByIdsWithTasksAndEstimation(List<Long> ids);

}
