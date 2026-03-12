package dev.somlyaip.blog.youdontknowhibernate.osiv.repository;

import dev.somlyaip.blog.youdontknowhibernate.osiv.model.entity.Project;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    /**
     * There are two types of EntityGraphType: FETCH GRAPH, LOAD GRAPH - the default is FETCH,
     * which only loads what's defined in it, even if the entity has FetchType.EAGER set.
     * e.g. `@EntityGraph(attributePaths = "projectArchitect", type = EntityGraph.EntityGraphType.LOAD)`
     */
    @EntityGraph(attributePaths = "projectArchitect")
    @Query("SELECT p FROM Project p")
    List<Project> findAllWithArchitect(Pageable pageable);

    /**
     * If both issues and features were of List type Collection, it would throw a MultipleBagException.
     *
     * If I set one of them to be of Set type, with 100 Issues and 100 Features, it would result in a Cartesian product.
     * On the database side, it can only return the result in N * M rows, which can involve a huge amount of network
     * traffic, and it would process all of this in memory.
     * There would be 1 connection from the Set type and N connections from the List type.
     */
    @EntityGraph(attributePaths = {"projectArchitect", "issues.estimation", "features"})
    @Query("SELECT p FROM Project p")
    List<Project> findAllWithArchitectAndIssuesAndFeatures();

    @EntityGraph(attributePaths = "issues")
    @Query("SELECT p FROM Project p WHERE p.id IN :idList")
    List<Project> findByIdWithIssues(List<Long> idList);

    @EntityGraph(attributePaths = "issues.estimation")
    @Query("SELECT p FROM Project p WHERE p.id IN :idList")
    List<Project> findByIdWithIssuesAndEstimation(List<Long> idList);

    @EntityGraph(attributePaths = "features")
    @Query("SELECT p FROM Project p WHERE p.id IN :idList")
    List<Project> findByIdWithFeatures(List<Long> idList, Sort sort);

}
