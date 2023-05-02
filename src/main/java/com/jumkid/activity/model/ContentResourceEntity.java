package com.jumkid.activity.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "activity_content_resource")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ContentResourceEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "content_resource_id")
    private String contentResourceId;

    @ManyToOne
    @JoinColumn(name = "activity_id")
    private ActivityEntity activityEntity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContentResourceEntity that = (ContentResourceEntity) o;
        return Objects.equals(contentResourceId, that.contentResourceId) && Objects.equals(activityEntity, that.activityEntity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contentResourceId, activityEntity);
    }
}
