package com.example.vtaxer.repository;

import com.example.vtaxer.model.Dependent;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DependentRepository extends MongoRepository<Dependent, String> {
    List<Dependent> findByUserId(String userId);
    void deleteByUserId(String userId);
    void deleteByIdAndUserId(String id, String userId);
}