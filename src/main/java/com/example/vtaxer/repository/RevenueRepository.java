package com.example.vtaxer.repository;

import com.example.vtaxer.model.Revenue;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface RevenueRepository extends MongoRepository<Revenue, String> {
    
    // This method enables auto query by 'type' and ordered by 'name' ASC
    List<Revenue> findByTypeOrderByNameAsc(String type);
}
