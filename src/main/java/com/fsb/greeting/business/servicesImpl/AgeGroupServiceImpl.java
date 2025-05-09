package com.fsb.greeting.business.servicesImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.fsb.greeting.business.services.AgeGroupService;
import com.fsb.greeting.dao.entities.AgeGroup;
import com.fsb.greeting.dao.repositories.AgeGroupRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AgeGroupServiceImpl  implements AgeGroupService{
   
    @Autowired
    private AgeGroupRepository ageGroupRepository;

    @Override
    public AgeGroup addAgeGroup(AgeGroup ageGroup) {
        if(ageGroup==null){
            return null;
        }
        AgeGroup newAgeGroup=new AgeGroup();
        try{
            newAgeGroup=ageGroupRepository.save(ageGroup);
        }catch(DataIntegrityViolationException e){
            log.error(e.getMessage());
            return null;
        }
        return newAgeGroup;
    }

    @Override
    public AgeGroup getAgeGroupById(Long id) {
        if(id==null){
            return null;
        }
        return this.ageGroupRepository
        .findById(id)
        .orElseThrow(()->new EntityNotFoundException("Age Group with id: "+id+" not found"));
    }

    @Override
    public List<AgeGroup> getAllAgeGroups() {
        return ageGroupRepository.findAll();
    }

    @Override
    public void deleteAgeGroup(Long id) {
        if(id==null){
            return ;
        } else if(this.ageGroupRepository.existsById(id)){
            this.ageGroupRepository.deleteById(id);
        }else{
            throw new EntityNotFoundException("Age Group with id: "+id+" not found");
        }   
    }

    @Override
    public AgeGroup updateAgeGroup(Long id, AgeGroup ageGroup) {
        AgeGroup existingAgeGroup = this.getAgeGroupById(id);
            existingAgeGroup.setGroupName(ageGroup.getGroupName());
            existingAgeGroup.setMinAge(ageGroup.getMinAge());
            existingAgeGroup.setMaxAge(ageGroup.getMaxAge());
            return ageGroupRepository.save(existingAgeGroup);
       
    }
}