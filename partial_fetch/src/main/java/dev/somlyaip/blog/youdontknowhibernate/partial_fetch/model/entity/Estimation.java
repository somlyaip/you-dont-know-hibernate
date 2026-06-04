package dev.somlyaip.blog.youdontknowhibernate.partial_fetch.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Estimation {

    @Id
    private Long id;
    private Integer originalManDays;

}
