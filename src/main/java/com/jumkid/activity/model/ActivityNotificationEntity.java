package com.jumkid.activity.model;

import com.jumkid.activity.enums.NotifyTimeUnit;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "activity_notification")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ActivityNotificationEntity {

    @Id
    @Column(name = "activity_notification_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long activityNotificationId;

    @Column(name = "notify_before")
    private Integer notifyBefore;

    @Enumerated(EnumType.STRING)
    @Column(name = "notify_before_unit")
    private NotifyTimeUnit notifyBeforeUnit;

    @Column(name = "trigger_datetime")
    private LocalDateTime triggerDatetime;

    @Column(name = "expired")
    private boolean expired;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "activity_id")
    private ActivityEntity activityEntity;

}
