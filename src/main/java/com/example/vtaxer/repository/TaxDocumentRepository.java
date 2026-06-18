// TaxDocumentRepository.java
package com.example.vtaxer.repository;

import com.example.vtaxer.model.TaxDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TaxDocumentRepository extends MongoRepository<TaxDocument, String> {
    List<TaxDocument> findByUserId(String userId);
    void deleteByUserId(String userId);
    void deleteByIdAndUserId(String id, String userId);
}