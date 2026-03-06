package dev.somlyaip.blog.youdontknowhibernate.lazy_to_many;

import java.util.concurrent.TimeUnit;

import dev.somlyaip.blog.youdontknowhibernate.common.testharness.TestDataPopulator;
import dev.somlyaip.blog.youdontknowhibernate.lazy_to_many.repository.ProjectRepository;
import dev.somlyaip.blog.youdontknowhibernate.lazy_to_many.testharness.ProjectTestDataPopulator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.StopWatch;

@SpringBootTest(properties = {
    "spring.jpa.properties.hibernate.order_inserts=true",
    "spring.jpa.properties.hibernate.jdbc.batch_size=50"
})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StopWatchDemoTest {

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
    void measureQueryPerformanceWithStopWatch() {
        StopWatch stopWatch = new StopWatch("Query Performance");

        stopWatch.start("findAll");
        projectRepository.findAll();
        stopWatch.stop();

        stopWatch.start("findAllByName (paginated)");
        projectRepository.findAllByNameContainsIgnoreCase("jec", PageRequest.of(0, 20));
        stopWatch.stop();

        System.out.println(stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
    }

}