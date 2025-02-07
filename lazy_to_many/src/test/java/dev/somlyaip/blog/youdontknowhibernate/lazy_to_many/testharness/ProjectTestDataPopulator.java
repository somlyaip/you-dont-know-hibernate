package dev.somlyaip.blog.youdontknowhibernate.lazy_to_many.testharness;

import jakarta.persistence.EntityManager;

import dev.somlyaip.blog.youdontknowhibernate.lazy_to_many.entity.Issue;
import dev.somlyaip.blog.youdontknowhibernate.lazy_to_many.entity.Project;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectTestDataPopulator {

    private final TransactionRunner transactionRunner;
    private final EntityManager entityManager;

    public void insertWithBatches(int projectCount, int issueCount) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        transactionRunner.executeInTransaction(() -> {
            for (int projectIndex = 0; projectIndex < projectCount; projectIndex++) {
                Project project = Project.builder()
                    .name("Project " + projectIndex)
                    .build();
                for (int issueIndex = 0; issueIndex < issueCount; issueIndex++) {
                    Issue issue = Issue.builder()
                        .title("Issue " + issueIndex)
                        .description("Description " + issueIndex)
                        .build();
                    project.addIssue(issue);
                }
                entityManager.persist(project);
            }
        });
        stopWatch.stop();
        log.info(
            "{} issues and {} project have been inserted in {} ms",
            projectCount * issueCount, projectCount, stopWatch.getTotalTimeMillis()
        );
    }
}
