package dev.somlyaip.blog.youdontknowhibernate.osiv.mapper;

import dev.somlyaip.blog.youdontknowhibernate.osiv.model.dto.IssueDto;
import dev.somlyaip.blog.youdontknowhibernate.osiv.model.entity.Issue;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class IssueMapper {

    public abstract Issue mapDtoToIssue(IssueDto issueDto);

    public abstract List<Issue> mapDtoToIssue(List<IssueDto> issueDtoList);

    public abstract IssueDto mapIssueToDto(Issue issue);

    public abstract List<IssueDto> mapIssueToDto(List<Issue> issueList);

}
