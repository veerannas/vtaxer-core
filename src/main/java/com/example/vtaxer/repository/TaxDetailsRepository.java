// TaxDetailsRepository.java
package com.example.vtaxer.repository;

import com.example.vtaxer.model.TaxDetails;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TaxDetailsRepository extends MongoRepository<TaxDetails, String> {
    Optional<TaxDetails> findByUserId(String userId);
    boolean existsByUserId(String userId);
}