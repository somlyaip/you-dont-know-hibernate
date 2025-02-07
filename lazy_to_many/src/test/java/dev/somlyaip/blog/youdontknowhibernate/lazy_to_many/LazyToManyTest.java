package dev.somlyaip.blog.youdontknowhibernate.lazy_to_many;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import jakarta.persistence.EntityManager;

import dev.somlyaip.blog.youdontknowhibernate.common.testharness.TestDataPopulator;
import dev.somlyaip.blog.youdontknowhibernate.lazy_to_many.entity.Issue;
import dev.somlyaip.blog.youdontknowhibernate.lazy_to_many.entity.Project;
import dev.somlyaip.blog.youdontknowhibernate.lazy_to_many.repository.ProjectRepository;
import dev.somlyaip.blog.youdontknowhibernate.lazy_to_many.service.NaiveProjectExportService;
import dev.somlyaip.blog.youdontknowhibernate.lazy_to_many.testharness.ProjectTestDataPopulator;
import dev.somlyaip.blog.youdontknowhibernate.lazy_to_many.testharness.TransactionRunner;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
    "spring.jpa.properties.hibernate.order_inserts=true",
    "spring.jpa.properties.hibernate.jdbc.batch_size=50"
})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LazyToManyTest {

    @Autowired
    private TestDataPopulator testDataPopulator;

    @Autowired
    private ProjectTestDataPopulator projectTestDataPopulator;

    @Autowired
    private ProjectRepository projectRepository;

    @BeforeAll
    void setUpTestData() {
        testDataPopulator.executeScriptsFromResources("/sql/set-up.sql");
        projectTestDataPopulator.insertWithBatches(100, 100);
    }

    @AfterAll
    void cleanUpTestData() {
        testDataPopulator.executeScriptsFromResources("/sql/tear-down.sql");
    }

    @Test
    void testPagination_usingLazyToManyAssociations_shouldPaginateOnDatabaseServer() {
        List<Project> projects = projectRepository.findAllByNameContainsIgnoreCase(
            "oject", Pageable.ofSize(5)
        );
        assertThat(projects).hasSize(5);
    }

    @Test
    void testPagination_usingEntityGraph_shouldPaginateInMemory() {
        List<Project> projects = projectRepository.findAllWithIssuesByNameContainsIgnoreCase(
            "oject", Pageable.ofSize(5)
        );
        assertThat(projects).hasSize(5);
    }

}
