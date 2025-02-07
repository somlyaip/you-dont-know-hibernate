package dev.somlyaip.blog.youdontknowhibernate.lazy_to_many.projection;

import dev.somlyaip.blog.youdontknowhibernate.lazy_to_many.entity.Issue;

public interface ProjectIdAndIssueProjection {

    Long getProjectId();

    Issue getIssue();

}
