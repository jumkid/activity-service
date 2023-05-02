package com.jumkid.activity.model;

import lombok.*;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "activity_entity_link")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityEntityLinkEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "entity_id")
    private String entityId;

    @Column(name = "entity_name")
    private String entityName;

    @ManyToOne
    @JoinColumn(name = "activity_id")
    private ActivityEntity activityEntity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActivityEntityLinkEntity that = (ActivityEntityLinkEntity) o;
        return Objects.equals(entityId, that.entityId) && Objects.equals(entityName, that.entityName) && Objects.equals(activityEntity, that.activityEntity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entityId, entityName, activityEntity);
    }
}
