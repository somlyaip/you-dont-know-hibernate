package dev.somlyaip.blog.youdontknowhibernate.eager_n_plus_1;

import java.util.List;
import java.util.Optional;

import dev.somlyaip.blog.youdontknowhibernate.common.testharness.TestDataPopulator;
import dev.somlyaip.blog.youdontknowhibernate.eager_n_plus_1.entity.Issue;
import dev.somlyaip.blog.youdontknowhibernate.eager_n_plus_1.repository.IssueRepository;
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
public class EagerNPlus1SelectWithSqlStatementCountAssertionTest {

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
    void test_usingJpaQueryMethod_shouldExecute2Queries() {
        assertThatSqlStatements(() -> {
            List<Issue> issues = issueRepository.findByTitle("Bug 1");
            assertThat(issues).hasSize(1);
        }).hasSelectCount(2);
    }

    @Test
    void test_usingFindById_shouldExecute1Query() {
        assertThatSqlStatements(() -> {
            Optional<Issue> issueOpt = issueRepository.findById(1L);
            assertThat(issueOpt).isPresent();
        }).hasSelectCount(1);
    }
}
