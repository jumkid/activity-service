package com.jumkid.activity.model;

import com.jumkid.activity.enums.ActivityStatus;
import lombok.*;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "activity")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ActivityEntity {

    @Id
    @Column(name = "activity_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ActivityStatus status;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "modification_date")
    private LocalDateTime modificationDate;

    @Column(name = "modified_by")
    private String modifiedBy;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "priority_id")
    private PriorityEntity priorityEntity;

    @OneToOne(mappedBy = "activityEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @PrimaryKeyJoinColumn
    private ActivityNotificationEntity activityNotificationEntity;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "activity_id")
    private List<ActivityEntityLinkEntity> activityEntityLinkEntities;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "activity_id")
    private List<ActivityAssigneeEntity> activityAssigneeEntities;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name="activity_id", referencedColumnName="activity_id")
    private List<ContentResourceEntity> contentResourceEntities;

}
