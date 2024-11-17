package dev.somlyaip.blog.youdontknowhibernate.lazy_multiple_to_one.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import dev.somlyaip.blog.youdontknowhibernate.common.testharness.TestDataPopulator;
import dev.somlyaip.blog.youdontknowhibernate.lazy_multiple_to_one.testharness.TransactionRunner;
import dev.somlyaip.blog.youdontknowhibernate.lazy_multiple_to_one.entity.Issue;
import dev.somlyaip.blog.youdontknowhibernate.lazy_multiple_to_one.entity.Project;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
public class UsingProxyAsAssociationTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private IssueRepository issueRepository;

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {
        "/sql/set-up.sql"
    })
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = {
        "/sql/tear-down.sql"
    })
    @Transactional
    void testSaveWithAssociations_usingProxy_shouldInsertJoinColumnCorrectly() {
        Project projectProxy = entityManager.getReference(Project.class, 1L);
        Issue issue = Issue.builder()
            .title("Feature 1")
            .description("This is a feature")
            .project(projectProxy)
            .build();

        issueRepository.saveAndFlush(issue);
    }
}
