// src/main/java/com/example/vtaxer/repository/AdminRepository.java
package com.example.vtaxer.repository;

import com.example.vtaxer.model.Admin;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AdminRepository extends MongoRepository<Admin, String> {
    Optional<Admin> findByEmail(String email);
    Optional<Admin> findByEmailAndPassword(String email, String password);
}
