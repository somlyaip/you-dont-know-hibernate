package dev.somlyaip.blog.youdontknowhibernate.eager_n_plus_1;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import com.querydsl.core.BooleanBuilder;
import dev.somlyaip.blog.youdontknowhibernate.common.testharness.TestDataPopulator;
import dev.somlyaip.blog.youdontknowhibernate.eager_n_plus_1.entity.Issue;
import dev.somlyaip.blog.youdontknowhibernate.eager_n_plus_1.entity.QIssue;
import dev.somlyaip.blog.youdontknowhibernate.eager_n_plus_1.repository.IssueRepository;
import io.hypersistence.utils.jdbc.validator.SQLStatementCountValidator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EagerNPlus1SelectTest {

    @Autowired
    private TestDataPopulator testDataPopulator;

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private EntityManager entityManager;

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
        List<Issue> issues = issueRepository.findByTitle("Bug 1");
        assertThat(issues).hasSize(1);
    }

    @Test
    void test_usingHibernateQuery_shouldExecute2Queries() {
        List<Issue> issues = entityManager.createQuery("""
                SELECT i FROM Issue i
                WHERE i.title = :title
                """, Issue.class)
            .setParameter("title", "Bug 1")
            .getResultList();

        assertThat(issues).hasSize(1);
    }

    @Test
    void test_usingHibernateCriteria_shouldExecute2Queries() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Issue> criteriaQuery = builder.createQuery(Issue.class);
        Root<Issue> root = criteriaQuery.from(Issue.class);
        Predicate predicate = builder.equal(root.get("title"), "Bug 1");
        criteriaQuery.where(predicate);
        TypedQuery<Issue> typedQuery = entityManager.createQuery(criteriaQuery);

        List<Issue> issues = typedQuery.getResultList();

        assertThat(issues).hasSize(1);
    }

    @Test
    void test_usingSpecification_shouldExecute2Queries() {
        Specification<Issue> specification = (root, query, cb) ->
            cb.equal(root.get("title"), "Bug 1");

        List<Issue> issues = issueRepository.findAll(specification);

        assertThat(issues).hasSize(1);
    }

    @Test
    void test_usingQueryDsl_shouldExecute2Queries() {
        QIssue qIssue = QIssue.issue;
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        Iterable<Issue> issues = issueRepository.findAll(
            booleanBuilder.and(qIssue.title.eq("Bug 1"))
        );

        assertThat(issues).hasSize(1);
    }

    @Test
    void test_usingFindById_shouldExecute1Query() {
        Optional<Issue> issueOpt = issueRepository.findById(1L);
        assertThat(issueOpt).isPresent();
    }

    // TODO: use join fetch
}
