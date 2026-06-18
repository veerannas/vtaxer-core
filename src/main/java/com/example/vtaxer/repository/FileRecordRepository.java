package com.example.vtaxer.repository;

import com.example.vtaxer.model.FileRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FileRecordRepository extends MongoRepository<FileRecord, String> {
    List<FileRecord> findByUserId(String userId);
}
