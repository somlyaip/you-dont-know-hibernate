package dev.somlyaip.blog.youdontknowhibernate.partial_fetch.service;

import dev.somlyaip.blog.youdontknowhibernate.partial_fetch.model.entity.Feature;
import dev.somlyaip.blog.youdontknowhibernate.partial_fetch.model.entity.Project;
import dev.somlyaip.blog.youdontknowhibernate.partial_fetch.repository.FeatureRepository;
import dev.somlyaip.blog.youdontknowhibernate.partial_fetch.repository.ProjectRepository;
import dev.somlyaip.blog.youdontknowhibernate.partial_fetch.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final FeatureRepository featureRepository;
    private final TaskRepository taskRepository;

    /**
     * Optimized select with all Project relations - plus pageable option
     */
    @Transactional(readOnly = true)
    public List<Project> getProjectsWithPartialRelations(Pageable pageable) {
        var projects = projectRepository.findAllWithFeatures(pageable);
        var projectIdList = projects.stream().map(Project::getId).toList();
        var features = featureRepository.findByIdsWithTasksAndEstimation(projectIdList);
        var featureIdList = features.stream().map(Feature::getId).toList();
        taskRepository.findByIdsWithComments(featureIdList);
        return projects;
    }

}
