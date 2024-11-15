package dev.somlyaip.blog.youdontknowhibernate.eager_n_plus_1_multiple_to_one;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import dev.somlyaip.blog.youdontknowhibernate.common.testharness.TestDataPopulator;
import dev.somlyaip.blog.youdontknowhibernate.eager_n_plus_1_multiple_to_one.entity.Issue;
import dev.somlyaip.blog.youdontknowhibernate.eager_n_plus_1_multiple_to_one.repository.IssueRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;

import static dev.somlyaip.blog.youdontknowhibernate.common.testharness.SqlStatementAssertions.assertThatSqlStatements;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EagerNPlus1SelectMultipleToOneTest {

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
    void test_usingJpaQueryMethod_shouldExecute4Queries() {
        assertThatSqlStatements(() -> {
            List<Issue> issues = issueRepository.findByTitle("Bug 1");
            assertThat(issues).hasSize(1);
        }).hasSelectCount(4);
    }

    @Test
    void test_usingFindById_shouldExecute1Query() {
        assertThatSqlStatements(() -> {
            Optional<Issue> issueOpt = issueRepository.findById(1L);
            assertThat(issueOpt).isPresent();
        }).hasSelectCount(1);
    }

    @Test
    void test_usingJpaQueryMethodWithEntityGraph_shouldExecute1Query() {
        assertThatSqlStatements(() -> {
            List<Issue> issues = issueRepository.findWithAllAssociationsByTitle("Bug 1");
            assertThat(issues).hasSize(1);
        }).hasSelectCount(1);
    }

}
