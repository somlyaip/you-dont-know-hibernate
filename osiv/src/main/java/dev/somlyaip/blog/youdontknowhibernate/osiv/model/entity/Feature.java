package dev.somlyaip.blog.youdontknowhibernate.osiv.model.entity;

import dev.somlyaip.blog.youdontknowhibernate.osiv.model.enums.FeatureStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Feature {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "feature_seq")
    private Long id;
    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private FeatureStatus status;

    @CreatedDate
    private OffsetDateTime createdAt;

    @ManyToOne
    private Project project;

}
