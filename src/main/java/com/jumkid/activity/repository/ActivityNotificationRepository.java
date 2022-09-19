package com.jumkid.activity.repository;

import com.jumkid.activity.model.ActivityNotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ActivityNotificationRepository extends JpaRepository<ActivityNotificationEntity, Long> {

    List<ActivityNotificationEntity> findByExpiredAndTriggerDatetimeBefore(Boolean expired, LocalDateTime now);

}
