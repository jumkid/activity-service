package com.jumkid.activity.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "activity_assignee")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ActivityAssigneeEntity {

    @Id
    @Column(name = "activity_assignee_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long activityAssigneeId;

    @NotBlank
    @Column(name = "assignee_id")
    private String assigneeId;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id")
    private ActivityEntity activityEntity;

}
