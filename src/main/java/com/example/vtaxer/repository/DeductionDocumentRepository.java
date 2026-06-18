// DeductionDocumentRepository.java
package com.example.vtaxer.repository;

import com.example.vtaxer.model.DeductionDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DeductionDocumentRepository extends MongoRepository<DeductionDocument, String> {
    List<DeductionDocument> findByUserId(String userId);
    List<DeductionDocument> findByUserIdAndSource(String userId, String source);
    void deleteByUserId(String userId);
    void deleteByIdAndUserId(String id, String userId);
}