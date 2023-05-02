package com.jumkid.activity.model;

import com.jumkid.activity.enums.NotifyTimeUnit;
import lombok.*;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "activity_notification")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ActivityNotificationEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "notify_before")
    private Integer notifyBefore;

    @Enumerated(EnumType.STRING)
    @Column(name = "notify_before_unit")
    private NotifyTimeUnit notifyBeforeUnit;

    @Column(name = "trigger_datetime")
    private LocalDateTime triggerDatetime;

    @Column(name = "expired")
    private boolean expired;

    @OneToOne
    @JoinColumn(name = "activity_id")
    private ActivityEntity activityEntity;

}
