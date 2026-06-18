package com.example.vtaxer.repository;

import com.example.vtaxer.model.Communication;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface CommunicationRepository extends MongoRepository<Communication, String> {
    List<Communication> findByUserId(String userId);
}
