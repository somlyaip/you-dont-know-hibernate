package dev.somlyaip.blog.youdontknowhibernate.partial_fetch.service;

import dev.somlyaip.blog.youdontknowhibernate.common.testharness.TestDataPopulator;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.BeforeAll;
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

    @Autowired
    private TestDataPopulator testDataPopulator;

    @BeforeAll
    void setUp() {
        testDataPopulator.executeScriptsFromResources(
                "/sql/tear-down.sql"
        );
        testDataPopulator.executeScriptsFromResources(
                "/sql/set-up.sql"
        );
    }

    @Test
    @DisplayName("""
            Given a pageable object,
            When I call the - optimized - tested method,
            Then `Select` count should be 3, with partially loaded relations.
            Not initialized entities: Issues, Developer, ProjectArchitect, SubTask.
            """)
    void testGetAllProjectsWithPartialRelations_optimized() {
        // Arrange
        var pageable = PageRequest.of(1, 1, Sort.by(Sort.Direction.ASC, "id"));

        // Act && Assert
        assertThatSqlStatements(() -> {
            var result = projectService.getProjectsWithPartialRelations(pageable);

            assertSoftly(softly -> {
                softly.assertThat(result).hasSize(1);
                softly.assertThat(
                                result.getFirst().getFeatures())
                        .hasSize(4);
                softly.assertThat(
                                result.getFirst()
                                        .getFeatures().getFirst()
                                        .getTasks())
                        .hasSize(3);
                softly.assertThat(
                                result.getFirst()
                                        .getFeatures().getFirst()
                                        .getTasks().getFirst()
                                        .getComments())
                        .hasSize(2);

                softly.assertThat(Hibernate.isInitialized(result.getFirst().getIssues())).isFalse();
                softly.assertThat(Hibernate.isInitialized(
                        result.getFirst().getFeatures().getFirst().getDevelopers())).isFalse();
                softly.assertThat(Hibernate.isInitialized(
                        result.getFirst().getFeatures().getFirst().getArchitects())).isFalse();
                softly.assertThat(Hibernate.isInitialized(
                        result.getFirst().getFeatures().getFirst().getTasks().getFirst().getSubTasks())).isFalse();
            });
        }).hasSelectCount(3);
    }

}