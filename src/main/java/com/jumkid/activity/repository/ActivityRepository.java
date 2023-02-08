package com.jumkid.activity.repository;

import com.jumkid.activity.model.ActivityEntity;
import com.jumkid.activity.model.PriorityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<ActivityEntity, Long> {

    List<ActivityEntity> findByPriorityEntity(PriorityEntity priorityEntity);

    @Query("SELECT a FROM ActivityEntity AS a " +
            "LEFT JOIN ActivityAssigneeEntity AS b ON a.id = b.activityEntity.id " +
            "WHERE a.createdBy = :userId OR b.assigneeId = :userId")
    List<ActivityEntity> findByUser(@Param("userId") String userId);

    @Query("SELECT a FROM ActivityEntity AS a " +
            "LEFT JOIN ActivityAssigneeEntity AS b ON a.id = b.activityEntity.id " +
            "LEFT JOIN ActivityEntityLinkEntity AS c ON a.id = c.activityEntity.id " +
            "WHERE (c.entityId = :entityId AND c.entityName = :entityName) " +
            "AND (a.createdBy = :userId OR b.assigneeId = :userId) ")
    List<ActivityEntity> findByEntityLink(@Param("entityId") String entityId,
            @Param("entityName") String entityName, @Param("userId") String userId);
}
