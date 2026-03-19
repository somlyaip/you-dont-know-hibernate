package dev.somlyaip.blog.youdontknowhibernate.osiv.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ProjectArchitect {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
    generator = "project_architect_seq")
    private Long id;
    private String name;
    private String email;
    private String position;

}
