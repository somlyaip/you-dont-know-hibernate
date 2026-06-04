package dev.somlyaip.blog.youdontknowhibernate.osiv;

import dev.somlyaip.blog.youdontknowhibernate.osiv.service.ProjectService;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import static dev.somlyaip.blog.youdontknowhibernate.common.testharness.SqlStatementAssertions.assertThatSqlStatements;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProjectServiceTest {

    @Autowired
    private ProjectService projectService;

    @Test
    @DisplayName("""
            Given a pageable object,
            When I call the - optimized - tested method,
            Then `Select` count should be 3, with fully loaded relations.
            """)
    void testGetAllProjectsWithRelations_optimized_with_all_relations() {
        // Arrange
        var pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));

        // Act && Assert
        assertThatSqlStatements(() -> {
            var result = projectService.getAllProjectsWithRelations(pageable);

            assertSoftly(softly -> {
                softly.assertThat(result).hasSize(8);
                softly.assertThat(result.getFirst().getIssues()).hasSize(8);
                softly.assertThat(result.getFirst().getFeatures()).hasSize(8);
                softly.assertThat(result.getFirst().getIssues().getFirst().getEstimation()).isNotNull();
                softly.assertThat(result.getFirst().getIssues().getFirst().getEstimation().getOriginalManDays())
                        .isEqualTo(1);
            });
        }).hasSelectCount(3);
    }

    @Test
    @DisplayName("""
            Given a not optimized select by EntityGraph,
            When I call the - not optimized - tested method,
            Then `Select` count should be 1, and it should result in a Cartesian product.
            """)
    void testGetAllProjectsWithRelations_returnsCartesianProduct() {
        // Arrange && Act && Assert
        assertThatSqlStatements(() -> {
            var result = projectService.getAllProjectsWithRelations();

            assertSoftly(softly -> {
                softly.assertThat(result).hasSize(8);
                softly.assertThat(result.getFirst().getIssues()).hasSize(64);
                softly.assertThat(result.getFirst().getFeatures()).hasSize(8);
                softly.assertThat(result.getFirst().getIssues().getFirst().getEstimation()).isNotNull();
                softly.assertThat(result.getFirst().getIssues().getFirst().getEstimation().getOriginalManDays())
                        .isEqualTo(1);
            });
        }).hasSelectCount(1);
    }

    @Test
    @DisplayName("""
            Given a pageable object,
            When I call the - optimized - tested method,
            Then `Select` count should be 3, with loaded relations - estimation excluded.
            """)
    void testGetAllProjectsWithRelations_optimized_without_estimation() {
        // Arrange
        var pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));

        // Act && Assert
        assertThatSqlStatements(() -> {
            var result = projectService.getAllProjectsWithRelationsExcludeEstimation(pageable);

            assertSoftly(softly -> {
                softly.assertThat(result).hasSize(8);
                softly.assertThat(result.getFirst().getIssues()).hasSize(8);
                softly.assertThat(result.getFirst().getFeatures()).hasSize(8);
                softly.assertThat(Hibernate.isInitialized(result.getFirst().getIssues().getFirst().getEstimation())).isFalse();
            });
        }).hasSelectCount(3);
    }

    @Test
    @DisplayName("""
            Given a pageable object,
            When I call the - basic findAll() - tested method,
            Then `Select` count should be 1, with only lazy relations.
            """)
    void testGetBaseAllProjects() {
        // Arrange
        var pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));

        // Act && Assert
        assertThatSqlStatements(() -> {
            var result = projectService.getBaseAllProjects(pageable).getContent();

            assertSoftly(softly -> {
                softly.assertThat(result).hasSize(8);
                softly.assertThat(Hibernate.isInitialized(result.getFirst().getIssues())).isFalse();
                softly.assertThat(Hibernate.isInitialized(result.getFirst().getFeatures())).isFalse();
                softly.assertThat(Hibernate.isInitialized(result.getFirst().getProjectArchitect())).isFalse();
            });
        }).hasSelectCount(1);
    }

}