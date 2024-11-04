package dev.somlyaip.blog.youdontknowhibernate.eager_n_plus_1_multiple_to_one.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    @Id
    private Long id;
    private String name;
}
