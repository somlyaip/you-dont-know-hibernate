package dev.somlyaip.blog.youdontknowhibernate.multi_level_entity_graph.repository;

import java.util.List;
import java.util.Optional;

import dev.somlyaip.blog.youdontknowhibernate.common.testharness.TestDataPopulator;
import dev.somlyaip.blog.youdontknowhibernate.multi_level_entity_graph.entity.DevelopmentAttributes;
import dev.somlyaip.blog.youdontknowhibernate.multi_level_entity_graph.entity.Issue;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static dev.somlyaip.blog.youdontknowhibernate.common.testharness.SqlStatementAssertions.assertThatSqlStatements;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IssueRepositoryTest {

    @Autowired
    private TestDataPopulator testDataPopulator;

    @Autowired
    private IssueRepository issueRepository;

    @BeforeAll
    void setUp() {
        testDataPopulator.executeScriptsFromResources(
            "/sql/set-up.sql"
        );
    }

    @AfterAll
    void tearDown() {
        testDataPopulator.executeScriptsFromResources(
            "/sql/tear-down.sql"
        );
    }

    @Test
    void testFindById_shouldSelectOnlyTheRootEntityWithOneQuery() {
        assertThatSqlStatements(() -> {
            Optional<Issue> issueOpt = issueRepository.findById(1L);

            assertThat(issueOpt.isPresent()).isTrue();
            Issue issue = issueOpt.get();
            assertThat(issue.getId()).isEqualTo(1L);
            assertThat(Hibernate.isInitialized(issue.getDevelopmentAttributes())).isFalse();
        }).hasSelectCount(1);
    }

    @Test
    void findByTitleContains_shouldSelectRelatedEntitiesInOneSelect() {
        assertThatSqlStatements(() -> {
            List<Issue> issues = issueRepository.findByTitleContains("Awesome");
            assertThat(issues).hasSize(1);
            Issue issue = issues.getFirst();
            DevelopmentAttributes developmentAttributes = issue.getDevelopmentAttributes();
            assertThat(Hibernate.isInitialized(developmentAttributes)).isTrue();
            assertThat(Hibernate.isInitialized(developmentAttributes.getBranch())).isTrue();
            assertThat(Hibernate.isInitialized(developmentAttributes.getMergeRequest())).isFalse();
            assertThat(Hibernate.isInitialized(developmentAttributes.getCommits())).isFalse();
        }).hasSelectCount(1);
    }

}
