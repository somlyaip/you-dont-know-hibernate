package dev.somlyaip.blog.youdontknowhibernate.eager_n_plus_1_multiple_to_one.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Issue {
    @Id
    private Long id;
    private String title;
    private String description;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "version_id")
    private Version version;

    @OneToOne
    @JoinColumn(name = "estimation_id")
    private Estimation estimation;
}
