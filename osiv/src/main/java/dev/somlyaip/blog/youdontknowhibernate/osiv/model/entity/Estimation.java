package dev.somlyaip.blog.youdontknowhibernate.osiv.model.entity;

import jakarta.persistence.*;

import lombok.*;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Estimation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "estimation_seq")
    private Long id;
    private Integer originalManDays;

}
