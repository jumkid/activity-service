package com.jumkid.activity.model;

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

    @Column(name = "email")
    private String email;

    @Column(name = "sms_id")
    private String smsId;

    @Column(name = "notify_datetime")
    private LocalDateTime notifyDatetime;

    @Column(name = "snooze_repeat")
    private Integer snoozeRepeat;

    @Column(name = "snooze_repeat_interval")
    private Integer snoozeRepeatInterval;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "activity_id")
    private ActivityEntity activityEntity;

}
