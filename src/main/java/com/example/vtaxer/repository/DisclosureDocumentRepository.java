// DisclosureDocumentRepository.java
package com.example.vtaxer.repository;

import com.example.vtaxer.model.DisclosureDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DisclosureDocumentRepository extends MongoRepository<DisclosureDocument, String> {
    List<DisclosureDocument> findByUserId(String userId);
    List<DisclosureDocument> findByUserIdAndSource(String userId, String source);
    void deleteByUserId(String userId);
    void deleteByIdAndUserId(String id, String userId);
}