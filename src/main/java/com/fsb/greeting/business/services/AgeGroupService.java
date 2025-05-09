package com.fsb.greeting.business.services;

import java.util.List;

import com.fsb.greeting.dao.entities.AgeGroup;

public interface AgeGroupService {
    AgeGroup addAgeGroup(AgeGroup ageGroup);
    AgeGroup getAgeGroupById(Long id);
    List<AgeGroup> getAllAgeGroups();
    void deleteAgeGroup(Long id);
    AgeGroup updateAgeGroup(Long id, AgeGroup ageGroup);
}