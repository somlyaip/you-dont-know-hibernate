package dev.somlyaip.blog.youdontknowhibernate.osiv.service;

import dev.somlyaip.blog.youdontknowhibernate.osiv.model.entity.*;
import dev.somlyaip.blog.youdontknowhibernate.osiv.model.enums.FeatureStatus;
import dev.somlyaip.blog.youdontknowhibernate.osiv.model.enums.IssueLevel;
import dev.somlyaip.blog.youdontknowhibernate.osiv.model.enums.IssueStatus;
import dev.somlyaip.blog.youdontknowhibernate.osiv.model.enums.ProjectStatus;
import dev.somlyaip.blog.youdontknowhibernate.osiv.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InitDbService {

    private final ProjectRepository projectRepository;
    private final ProjectArchitectRepository projectArchitectRepository;
    private final IssueRepository issueRepository;
    private final FeatureRepository featureRepository;
    private final EstimationRepository estimationRepository;

    @Transactional
    public void initDatabase() {
        var architect1 = projectArchitectRepository.save(ProjectArchitect.builder()
                .name("Person 1")
                .email("personone@mail.com")
                .position("fullstack").build());
        var architect2 = projectArchitectRepository.save(ProjectArchitect.builder()
                .name("Person 2")
                .email("persontwo@mail.com")
                .position("frontend").build());
        var architect3 = projectArchitectRepository.save(ProjectArchitect.builder()
                .name("Person 3")
                .email("personthree@mail.com")
                .position("backend").build());
        var architect4 = projectArchitectRepository.save(ProjectArchitect.builder()
                .name("Person 4")
                .email("personfour@mail.com")
                .position("fullstack").build());
        var architect5 = projectArchitectRepository.save(ProjectArchitect.builder()
                .name("Person 5")
                .email("personfive@mail.com")
                .position("frontend").build());
        var architect6 = projectArchitectRepository.save(ProjectArchitect.builder()
                .name("Person 6")
                .email("personsix@mail.com")
                .position("backend").build());

        var project1 = projectRepository.save(Project.builder()
                .projectName("project 1")
                .projectArchitect(architect1)
                .status(ProjectStatus.PLANNING).build());
        var project2 = projectRepository.save(Project.builder()
                .projectName("project 2")
                .projectArchitect(architect2)
                .status(ProjectStatus.INITIATION).build());
        var project3 = projectRepository.save(Project.builder()
                .projectName("project 3")
                .projectArchitect(architect3)
                .status(ProjectStatus.EXECUTION).build());
        var project4 = projectRepository.save(Project.builder()
                .projectName("project 4")
                .projectArchitect(architect4)
                .status(ProjectStatus.EXECUTION).build());
        var project5 = projectRepository.save(Project.builder()
                .projectName("project 5")
                .projectArchitect(architect5)
                .status(ProjectStatus.ON_HOLD).build());
        var project6 = projectRepository.save(Project.builder()
                .projectName("project 6")
                .projectArchitect(architect6)
                .status(ProjectStatus.EXECUTION).build());
        var project7 = projectRepository.save(Project.builder()
                .projectName("project 7")
                .projectArchitect(architect1)
                .status(ProjectStatus.INITIATION).build());
        var project8 = projectRepository.save(Project.builder()
                .projectName("project 8")
                .projectArchitect(architect1)
                .status(ProjectStatus.PLANNING).build());

        var estimation1 = estimationRepository.save(Estimation.builder()
                .originalManDays(1).build());

        issueRepository.save(Issue.builder()
                .title("issue title 1")
                .description("description 1")
                .status(IssueStatus.NEW)
                .issueLevel(IssueLevel.BLOCKING)
                .estimation(estimation1)
                .project(project1).build());
        issueRepository.save(Issue.builder()
                .title("issue title 2")
                .description("description 2")
                .status(IssueStatus.IN_PROGRESS)
                .issueLevel(IssueLevel.MEDIUM)
                .estimation(Estimation.builder().originalManDays(2).build())
                .project(project2).build());
        issueRepository.save(Issue.builder()
                .title("issue title 3")
                .description("description 3")
                .status(IssueStatus.RESOLVED)
                .issueLevel(IssueLevel.EASY)
                .project(project3).build());
        issueRepository.save(Issue.builder()
                .title("issue title 4")
                .description("description 4")
                .status(IssueStatus.IN_PROGRESS)
                .issueLevel(IssueLevel.BLOCKING)
                .project(project1).build());
        issueRepository.save(Issue.builder()
                .title("issue title 5")
                .description("description 5")
                .status(IssueStatus.NEW)
                .issueLevel(IssueLevel.BLOCKING)
                .project(project1).build());
        issueRepository.save(Issue.builder()
                .title("issue title 6")
                .description("description 6")
                .status(IssueStatus.IN_PROGRESS)
                .issueLevel(IssueLevel.EASY)
                .project(project1).build());
        issueRepository.save(Issue.builder()
                .title("issue title 7")
                .description("description 7")
                .status(IssueStatus.NEW)
                .issueLevel(IssueLevel.BLOCKING)
                .project(project2).build());
        issueRepository.save(Issue.builder()
                .title("issue title 8")
                .description("description 8")
                .status(IssueStatus.IN_PROGRESS)
                .issueLevel(IssueLevel.BLOCKING)
                .project(project4).build());
        issueRepository.save(Issue.builder()
                .title("issue title 9")
                .description("description 9")
                .status(IssueStatus.IN_PROGRESS)
                .issueLevel(IssueLevel.EASY)
                .project(project6).build());
        issueRepository.save(Issue.builder()
                .title("issue title 10")
                .description("description 10")
                .status(IssueStatus.RESOLVED)
                .issueLevel(IssueLevel.MEDIUM)
                .project(project7).build());
        issueRepository.save(Issue.builder()
                .title("issue title 11")
                .description("description 11")
                .status(IssueStatus.CLOSED)
                .issueLevel(IssueLevel.BLOCKING)
                .project(project8).build());
        issueRepository.save(Issue.builder()
                .title("issue title 12")
                .description("description 12")
                .status(IssueStatus.IN_PROGRESS)
                .issueLevel(IssueLevel.BLOCKING)
                .project(project2).build());
        issueRepository.save(Issue.builder()
                .title("issue title 13")
                .description("description 13")
                .status(IssueStatus.NEW)
                .issueLevel(IssueLevel.MEDIUM)
                .project(project4).build());
        issueRepository.save(Issue.builder()
                .title("issue title 14")
                .description("description 14")
                .status(IssueStatus.RESOLVED)
                .issueLevel(IssueLevel.BLOCKING)
                .project(project7).build());
        issueRepository.save(Issue.builder()
                .title("issue title 15")
                .description("description 15")
                .status(IssueStatus.RESOLVED)
                .issueLevel(IssueLevel.BLOCKING)
                .project(project1).build());
        issueRepository.save(Issue.builder()
                .title("issue title 16")
                .description("description 16")
                .status(IssueStatus.RESOLVED)
                .issueLevel(IssueLevel.BLOCKING)
                .project(project1).build());
        issueRepository.save(Issue.builder()
                .title("issue title 17")
                .description("description 17")
                .status(IssueStatus.RESOLVED)
                .issueLevel(IssueLevel.BLOCKING)
                .project(project1).build());
        issueRepository.save(Issue.builder()
                .title("issue title 18")
                .description("description 18")
                .status(IssueStatus.RESOLVED)
                .issueLevel(IssueLevel.BLOCKING)
                .project(project1).build());

        featureRepository.save(Feature.builder()
                .title("feature title 1")
                .description("description 1")
                .status(FeatureStatus.ACCEPTED)
                .project(project1).build());
        featureRepository.save(Feature.builder()
                .title("feature title 2")
                .description("description 2")
                .status(FeatureStatus.COMPLETED)
                .project(project1).build());
        featureRepository.save(Feature.builder()
                .title("feature title 3")
                .description("description 3")
                .status(FeatureStatus.REJECTED)
                .project(project1).build());
        featureRepository.save(Feature.builder()
                .title("feature title 4")
                .description("description 4")
                .status(FeatureStatus.COMPLETED)
                .project(project2).build());
        featureRepository.save(Feature.builder()
                .title("feature title 5")
                .description("description 5")
                .status(FeatureStatus.PROPOSED)
                .project(project3).build());
        featureRepository.save(Feature.builder()
                .title("feature title 6")
                .description("description 6")
                .status(FeatureStatus.DEPRECATED)
                .project(project3).build());
        featureRepository.save(Feature.builder()
                .title("feature title 7")
                .description("description 7")
                .status(FeatureStatus.ACCEPTED)
                .project(project7).build());
        featureRepository.save(Feature.builder()
                .title("feature title 8")
                .description("description 8")
                .status(FeatureStatus.IN_DEVELOPMENT)
                .project(project2).build());
        featureRepository.save(Feature.builder()
                .title("feature title 9")
                .description("description 9")
                .status(FeatureStatus.ACCEPTED)
                .project(project8).build());
        featureRepository.save(Feature.builder()
                .title("feature title 10")
                .description("description 10")
                .status(FeatureStatus.IN_DEVELOPMENT)
                .project(project5).build());
        featureRepository.save(Feature.builder()
                .title("feature title 11")
                .description("description 11")
                .status(FeatureStatus.IN_DEVELOPMENT)
                .project(project1).build());
        featureRepository.save(Feature.builder()
                .title("feature title 12")
                .description("description 12")
                .status(FeatureStatus.IN_DEVELOPMENT)
                .project(project1).build());
        featureRepository.save(Feature.builder()
                .title("feature title 13")
                .description("description 13")
                .status(FeatureStatus.IN_DEVELOPMENT)
                .project(project1).build());
        featureRepository.save(Feature.builder()
                .title("feature title 14")
                .description("description 14")
                .status(FeatureStatus.IN_DEVELOPMENT)
                .project(project1).build());
        featureRepository.save(Feature.builder()
                .title("feature title 15")
                .description("description 15")
                .status(FeatureStatus.IN_DEVELOPMENT)
                .project(project1).build());
    }

}
