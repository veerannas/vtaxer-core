// IncomeDocumentRepository.java
package com.example.vtaxer.repository;

import com.example.vtaxer.model.IncomeDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IncomeDocumentRepository extends MongoRepository<IncomeDocument, String> {
    List<IncomeDocument> findByUserId(String userId);
    List<IncomeDocument> findByUserIdAndSource(String userId, String source);
    void deleteByUserId(String userId);
    void deleteByIdAndUserId(String id, String userId);
}