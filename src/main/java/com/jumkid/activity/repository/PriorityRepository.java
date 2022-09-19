package com.jumkid.activity.repository;

import com.jumkid.activity.model.PriorityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Optional;

@Repository
public interface PriorityRepository extends JpaRepository<PriorityEntity, Integer> {

    @Cacheable(value = "priorityEntity")
    Optional<PriorityEntity> findByIdentifier(String identifier);

    @Override
    @Cacheable(value = "priorityEntity")
    List<PriorityEntity> findAll();

}
