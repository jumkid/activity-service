package com.jumkid.activity.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "activity_assignee")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ActivityAssigneeEntity {

    @Id
    @Column(name = "activity_assignee_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "assignee_id")
    private String assigneeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id")
    private ActivityEntity activityEntity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActivityAssigneeEntity that = (ActivityAssigneeEntity) o;
        return Objects.equals(assigneeId, that.assigneeId) && Objects.equals(activityEntity, that.activityEntity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(assigneeId, activityEntity);
    }
}
