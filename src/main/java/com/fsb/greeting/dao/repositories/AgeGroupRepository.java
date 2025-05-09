package com.fsb.greeting.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fsb.greeting.dao.entities.AgeGroup;

public interface AgeGroupRepository extends JpaRepository<AgeGroup, Long> {
    
}