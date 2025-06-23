package dev.somlyaip.blog.youdontknowhibernate.partial_fetch.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ProjectArchitect {

    @Id
    private Long id;
    private String name;
    private String email;
    private String position;

    @ManyToOne
    @JoinColumn(name = "feature_id")
    private Feature feature;

}
