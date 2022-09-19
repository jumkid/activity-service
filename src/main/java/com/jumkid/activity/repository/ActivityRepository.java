package com.jumkid.activity.repository;

import com.jumkid.activity.model.ActivityEntity;
import com.jumkid.activity.model.PriorityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<ActivityEntity, Long> {

    List<ActivityEntity> findByPriorityEntity(PriorityEntity priorityEntity);

}
