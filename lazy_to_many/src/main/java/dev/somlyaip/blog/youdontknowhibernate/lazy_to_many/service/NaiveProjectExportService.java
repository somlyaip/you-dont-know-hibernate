package dev.somlyaip.blog.youdontknowhibernate.lazy_to_many.service;

import java.util.List;

import dev.somlyaip.blog.youdontknowhibernate.lazy_to_many.entity.Project;
import dev.somlyaip.blog.youdontknowhibernate.lazy_to_many.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NaiveProjectExportService {

    private final ProjectRepository projectRepository;

    public void exportByNameContainsIgnoreCase(String nameFragment, int pageSize) {
        List<Project> projectsToExport;
        int pageNumber = 0;
        do {
            Pageable pageable = PageRequest.of(pageNumber++, pageSize);
            projectsToExport = projectRepository.findAllWithIssuesByNameContainsIgnoreCase(
                nameFragment, pageable
            );
        } while (projectsToExport.size() == pageSize);
    }
}
