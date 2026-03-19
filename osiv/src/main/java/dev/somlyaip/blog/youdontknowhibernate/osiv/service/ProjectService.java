package dev.somlyaip.blog.youdontknowhibernate.osiv.service;

import dev.somlyaip.blog.youdontknowhibernate.osiv.model.entity.Project;
import dev.somlyaip.blog.youdontknowhibernate.osiv.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    /**
     *  Uses EntityGraph, causes Cartesian Product: N * M rows
     */
    @Transactional(readOnly = true)
    public List<Project> getAllProjectsWithRelations() {
        return projectRepository.findAllWithArchitectAndIssuesAndFeatures();
    }

    /**
     * Optimized select with all Project relations - plus pageable option
     */
    @Transactional(readOnly = true)
    public List<Project> getAllProjectsWithRelations(Pageable pageable) {
        var projects = projectRepository.findAllWithArchitect(pageable);
        var idList = projects.stream().map(Project::getId).toList();
        projects = projectRepository.findByIdWithIssuesAndEstimation(idList);
        projects = projectRepository.findByIdWithFeatures(idList, pageable.getSort());
        return projects;
    }

    /**
     * Optimized select with all Project and their direct relations, issues.estimation excluded - plus pageable option
     */
    @Transactional(readOnly = true)
    public List<Project> getAllProjectsWithRelationsExcludeEstimation(Pageable pageable) {
        var projects = projectRepository.findAllWithArchitect(pageable);
        var idList = projects.stream().map(Project::getId).toList();
        projects = projectRepository.findByIdWithIssues(idList);
        projects = projectRepository.findByIdWithFeatures(idList, pageable.getSort());
        return projects;
    }

    /**
     * Simple findAll() with lazy relations
     */
    @Transactional(readOnly = true)
    public Page<Project> getBaseAllProjects(Pageable pageable) {
        return projectRepository.findAll(pageable);
    }

}
