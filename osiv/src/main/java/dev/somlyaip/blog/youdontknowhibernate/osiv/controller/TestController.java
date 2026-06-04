package dev.somlyaip.blog.youdontknowhibernate.osiv.controller;

import dev.somlyaip.blog.youdontknowhibernate.osiv.mapper.ProjectMapper;
import dev.somlyaip.blog.youdontknowhibernate.osiv.mapper.ProjectWithoutEstimationMapper;
import dev.somlyaip.blog.youdontknowhibernate.osiv.model.dto.ProjectDto;
import dev.somlyaip.blog.youdontknowhibernate.osiv.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequestMapping("/project")
@RestController
@RequiredArgsConstructor
public class TestController {

    private final ProjectService projectService;
    private final ProjectMapper projectMapper;
    private final ProjectWithoutEstimationMapper projectWithoutEstimationMapper;

    @GetMapping("/all/optimized")
    public ResponseEntity<List<ProjectDto>> getAllProjects(
            @RequestParam Boolean isRelationsIncluded, @SortDefault("id") Pageable pageable
    ) {
        var projects = isRelationsIncluded
                ? projectService.getAllProjectsWithRelations(pageable)
                : projectService.getBaseAllProjects(pageable).getContent();
        var response = isRelationsIncluded
                ? projectMapper.mapWithRelations(projects)
                : projectMapper.mapWithoutRelations(projects);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all/optimized/without-estimation")
    public ResponseEntity<List<ProjectDto>> getAllProjectsWithoutEstimation(@SortDefault("id") Pageable pageable) {
        var projects = projectService.getAllProjectsWithRelationsExcludeEstimation(pageable);

        return ResponseEntity.ok(projectWithoutEstimationMapper.mapProjectWithoutEstimation(projects));
    }

    /**
     * Since only the Feature relationship is a Set type Collection, there is only one Feature,
     * whereas the list of Issues is of type List, which is why there will be N Issues in the response.
     */
    @GetMapping("/all/descartes")
    public ResponseEntity<List<ProjectDto>> getAllProjectsDescartes() {
        var allProjectsWithRelations = projectService.getAllProjectsWithRelations();

        return ResponseEntity.ok(projectMapper.mapWithRelations(allProjectsWithRelations));
    }

    /**
     * Set a breakpoint in the SessionImpl class located in the org.hibernate.internal package.
     * When open-session-in-view is disabled, the breakpoint will not be hit during the endpoint call,
     * whereas when it is enabled, the breakpoint will be hit because this is where the session opens.
     * Also, check for the `Opening Hibernate Session` and `Closing session [...]` logs in the console.
     */
    @GetMapping("/test/open-session-in-view")
    public ResponseEntity<Void> testOsiv() {
        try {
            log.info("testSleep starts");
            Thread.sleep(5000);
            log.info("testSleep ends");
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().build();
    }

}
