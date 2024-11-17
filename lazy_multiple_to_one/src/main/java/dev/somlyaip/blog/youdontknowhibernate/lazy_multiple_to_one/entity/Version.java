package dev.somlyaip.blog.youdontknowhibernate.lazy_multiple_to_one.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Version {
    @Id
    private Long id;
    private Integer minor;
    private Integer major;
    private Integer patch;
}
