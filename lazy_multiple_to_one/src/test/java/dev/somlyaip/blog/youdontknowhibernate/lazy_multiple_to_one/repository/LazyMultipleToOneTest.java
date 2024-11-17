package dev.somlyaip.blog.youdontknowhibernate.lazy_multiple_to_one.repository;

import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;

import dev.somlyaip.blog.youdontknowhibernate.common.testharness.TestDataPopulator;
import dev.somlyaip.blog.youdontknowhibernate.lazy_multiple_to_one.testharness.TransactionRunner;
import dev.somlyaip.blog.youdontknowhibernate.lazy_multiple_to_one.entity.Issue;
import dev.somlyaip.blog.youdontknowhibernate.lazy_multiple_to_one.entity.Project;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static dev.somlyaip.blog.youdontknowhibernate.common.testharness.SqlStatementAssertions.assertThatSqlStatements;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LazyMultipleToOneTest {

    @Autowired
    private TestDataPopulator testDataPopulator;

    @Autowired
    private TransactionRunner transactionRunner;

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
    void test_usingJpaQueryMethod_shouldExecute1Query() {
        assertThatSqlStatements(() -> {
            List<Issue> issues = issueRepository.findAllByDescriptionContains("bug");
            assertThat(issues).hasSize(1);
            assertThat(issues.getFirst().getId()).isEqualTo(1);
            assertThat(issues.getFirst().getTitle()).isEqualTo("Bug 1");
        }).hasSelectCount(1);
    }

    @Test
    @Transactional
    void testFetchLazyAssociation_usingFindByIdAndGetFieldOfLazyAssociation_shouldExecute2Queries() {
        assertThatSqlStatements(() -> {
            Optional<Issue> issueOpt = issueRepository.findById(1L);
            assertThat(issueOpt).isPresent();

            Issue issue = issueOpt.get();
            Project project = issue.getProject();
            assertThat(project.getName()).isEqualTo("Project 1");
        }).hasSelectCount(2);
    }

    @Test
    @Transactional
    void testFetchLazyAssociation_usingTransactionalAnnotation_shouldNotThrowLazyInitializationException() {
        Optional<Issue> issueOpt = issueRepository.findById(1L);
        assertThat(issueOpt).isPresent();

        Issue issue = issueOpt.get();
        Project project = issue.getProject();
        assertThat(project.getName()).isEqualTo("Project 1");
    }

    @Test
    void testFetchLazyAssociation_usingOpenedTransaction_shouldNotThrowLazyInitializationException() {
        transactionRunner.executeInTransaction(() -> {
            Optional<Issue> issueOpt = issueRepository.findById(1L);
            assertThat(issueOpt).isPresent();

            Issue issue = issueOpt.get();
            Project project = issue.getProject();
            assertThat(project.getName()).isEqualTo("Project 1");
            /* There is an open transaction here
            So we can modify some data here as well */
        });
        // Here modifications are available for both this test and another threads, e.g. your rest service
    }

    @Test
    void testFetchLazyAssociation_usingFindWithProjectByIdAndGetFieldOfLazyAssociation_shouldExecute1Query() {
        assertThatSqlStatements(() -> {
            Optional<Issue> issueOpt = issueRepository.findWithProjectById(1L);
            assertThat(issueOpt).isPresent();

            Issue issue = issueOpt.get();
            Project project = issue.getProject();
            assertThat(project.getName()).isEqualTo("Project 1");
        }).hasSelectCount(1);
    }

}
