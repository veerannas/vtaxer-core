package com.example.vtaxer.repository;

import com.example.vtaxer.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmailAndPassword(String email, String password);
    Optional<User> findByEmail(String email);

    // ✅ Add this line to fix the error
    boolean existsByEmail(String email);
}
