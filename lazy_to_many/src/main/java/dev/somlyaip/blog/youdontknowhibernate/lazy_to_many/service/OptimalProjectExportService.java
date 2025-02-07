package dev.somlyaip.blog.youdontknowhibernate.lazy_to_many.service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.EntityManager;

import dev.somlyaip.blog.youdontknowhibernate.lazy_to_many.entity.Issue;
import dev.somlyaip.blog.youdontknowhibernate.lazy_to_many.entity.Project;
import dev.somlyaip.blog.youdontknowhibernate.lazy_to_many.projection.ProjectIdAndIssueProjection;
import dev.somlyaip.blog.youdontknowhibernate.lazy_to_many.repository.IssueRepository;
import dev.somlyaip.blog.youdontknowhibernate.lazy_to_many.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OptimalProjectExportService {

    private final ProjectRepository projectRepository;
    private final IssueRepository issueRepository;
    private final EntityManager entityManager;

    public void exportByNameContainsIgnoreCase(String nameFragment, int pageSize) {
        List<Project> projectsToExport;
        int pageNumber = 0;
        do {
            Pageable pageable = PageRequest.of(pageNumber++, pageSize);
            projectsToExport = projectRepository.findAllByNameContainsIgnoreCase(
                nameFragment, pageable
            );
            bulkFetchIssues(projectsToExport);
        } while (projectsToExport.size() == pageSize);
    }

    private void bulkFetchIssues(List<Project> projectsToExport) {
        HashMap<Long, List<Issue>> fetchedIssuesByProjectId = issueRepository.findAllIdAndIssueByProjectIn(projectsToExport)
            .stream().collect(Collectors.groupingBy(
                ProjectIdAndIssueProjection::getProjectId,
                HashMap::new,
                Collectors.mapping(ProjectIdAndIssueProjection::getIssue, Collectors.toList())
            ));

        for (Project project : projectsToExport) {
            entityManager.detach(project);
            project.setIssues(fetchedIssuesByProjectId.get(project.getId()));
        }
    }

}
