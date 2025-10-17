package dev.somlyaip.blog.youdontknowhibernate.lazy_to_many;

import java.util.concurrent.TimeUnit;

import dev.somlyaip.blog.youdontknowhibernate.common.testharness.TestDataPopulator;
import dev.somlyaip.blog.youdontknowhibernate.lazy_to_many.service.NaiveProjectExportService;
import dev.somlyaip.blog.youdontknowhibernate.lazy_to_many.service.OptimalProjectExportService;
import dev.somlyaip.blog.youdontknowhibernate.lazy_to_many.testharness.ProjectTestDataPopulator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
    "spring.jpa.properties.hibernate.order_inserts=true",
    "spring.jpa.properties.hibernate.jdbc.batch_size=50"
})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProjectExportTest {

    private static final int PAGE_SIZE = 20;

    @Autowired
    private TestDataPopulator testDataPopulator;

    @Autowired
    private ProjectTestDataPopulator projectTestDataPopulator;

    @Autowired
    private NaiveProjectExportService naiveProjectExportService;

    @Autowired
    private OptimalProjectExportService optimalProjectExportService;

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
    void testNaiveProjectExport() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        naiveProjectExportService.exportByNameContainsIgnoreCase("jec", PAGE_SIZE);

        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint(TimeUnit.SECONDS));
    }

    @Test
    void testOptimalProjectExport() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        optimalProjectExportService.exportByNameContainsIgnoreCase("jec", PAGE_SIZE);

        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint(TimeUnit.SECONDS));
    }

}
