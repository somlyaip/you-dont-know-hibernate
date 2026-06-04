package dev.somlyaip.blog.youdontknowhibernate.multi_level_entity_graph.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MergeRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    // TODO: add source and target branches
}
