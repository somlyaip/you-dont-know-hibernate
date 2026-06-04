package dev.somlyaip.blog.youdontknowhibernate.partial_fetch.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    private Long id;
    private String title;
    private String description;

    @OneToMany(mappedBy = "task")
    private List<Comment> comments;

    @OneToMany(mappedBy = "task")
    private List<SubTask> subTasks;

    @ManyToOne
    @JoinColumn(name = "feature_id")
    private Feature feature;

}
