package com.example.vtaxer.service;

import com.example.vtaxer.model.Dependent;
import com.example.vtaxer.repository.DependentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DependentService {

    @Autowired
    private DependentRepository dependentRepository;

    public List<Dependent> getDependentsByUserId(String userId) {
        return dependentRepository.findByUserId(userId);
    }

    public Dependent addDependent(Dependent dependent) {
        return dependentRepository.save(dependent);
    }

    public List<Dependent> addMultipleDependents(List<Dependent> dependents) {
        return dependentRepository.saveAll(dependents);
    }

    public void deleteDependent(String id, String userId) {
        dependentRepository.deleteByIdAndUserId(id, userId);
    }

    public void deleteAllDependentsForUser(String userId) {
        dependentRepository.deleteByUserId(userId);
    }

    public Dependent updateDependent(Dependent dependent) {
        return dependentRepository.save(dependent);
    }
}