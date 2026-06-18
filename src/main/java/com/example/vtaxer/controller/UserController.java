package com.example.vtaxer.controller;

import com.example.vtaxer.model.*;
import com.example.vtaxer.repository.UserRepository;
import com.example.vtaxer.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/customer")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaxDetailsService taxDetailsService;

    @Autowired
    private DependentService dependentService;

    @Autowired
    private TaxDocumentService taxDocumentService;

    @Autowired
    private IncomeDocumentService incomeDocumentService;

    @Autowired
    private DeductionDocumentService deductionDocumentService;

    @Autowired
    private DisclosureDocumentService disclosureDocumentService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, Object> userMap) {
        try {
            String email = (String) userMap.get("email");
            String password = (String) userMap.get("password");

            if (email == null || password == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Email and password are required"));
            }

            Optional<User> existingUser = userRepository.findByEmail(email);
            if (existingUser.isPresent()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Email already exists"));
            }

            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            User user = new User();
            user.setFirstName((String) userMap.get("firstName"));
            user.setLastName((String) userMap.get("lastName"));
            user.setEmail(email);
            user.setPassword(hashedPassword);
            user.setPhone((String) userMap.get("phone"));
            user.setSsn((String) userMap.get("ssn"));
            user.setState((String) userMap.get("state"));

            if (userMap.containsKey("middleName")) {
                user.setMiddleName((String) userMap.get("middleName"));
            }
            if (userMap.containsKey("alternatePhone")) {
                user.setAlternatePhone((String) userMap.get("alternatePhone"));
            }

            user.setCreatedAt(LocalDateTime.now());
            user.setLastLoginAt(null);

            User savedUser = userRepository.save(user);
            savedUser.setPassword(null); // Don't return password hash

            return ResponseEntity.ok(Map.of(
                "message", "Registration successful",
                "userId", savedUser.getId(),
                "email", savedUser.getEmail()
            ));
        } catch (Exception e) {
            logger.error("Registration error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Registration failed: " + e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> credentials) {
        try {
            String email = credentials.get("email");
            String password = credentials.get("password");

            if (email == null || password == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Email and password are required"));
            }

            Optional<User> userOpt = userRepository.findByEmail(email);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(401).body(Map.of("error", "Invalid email or password"));
            }

            User user = userOpt.get();
            
            if (!BCrypt.checkpw(password, user.getPassword())) {
                return ResponseEntity.status(401).body(Map.of("error", "Invalid email or password"));
            }

            user.setLastLoginAt(LocalDateTime.now());
            userRepository.save(user);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("userId", user.getId());
            response.put("email", user.getEmail());
            response.put("firstName", user.getFirstName());
            response.put("lastName", user.getLastName());
            response.put("token", "dummy-token");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Login error: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of("error", "Login failed: " + e.getMessage()));
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        try {
            Optional<User> userOpt = userRepository.findByEmail(email);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of("error", "User not found"));
            }

            User user = userOpt.get();
            user.setPassword(null);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            logger.error("Error fetching user by email: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        try {
            Optional<User> user = userRepository.findById(id);
            if (user.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found"));
            }
            
            User userData = user.get();
            userData.setPassword(null);
            return ResponseEntity.ok(userData);
        } catch (Exception e) {
            logger.error("Error fetching user: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody Map<String, Object> updates) {
        try {
            User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

            if (updates.containsKey("firstName")) user.setFirstName((String) updates.get("firstName"));
            if (updates.containsKey("lastName")) user.setLastName((String) updates.get("lastName"));
            if (updates.containsKey("middleName")) user.setMiddleName((String) updates.get("middleName"));
            if (updates.containsKey("phone")) user.setPhone((String) updates.get("phone"));
            if (updates.containsKey("alternatePhone")) user.setAlternatePhone((String) updates.get("alternatePhone"));
            if (updates.containsKey("password")) {
                String newPassword = (String) updates.get("password");
                user.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
            }

            User updatedUser = userRepository.save(user);
            updatedUser.setPassword(null);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            logger.error("Error updating user: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{userId}/tax-details")
    public ResponseEntity<?> saveTaxDetails(@PathVariable String userId, @RequestBody TaxDetails taxDetails) {
        try {
            if (!userRepository.existsById(userId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found"));
            }

            taxDetails.setUserId(userId);
            TaxDetails savedDetails = taxDetailsService.saveOrUpdateTaxDetails(taxDetails);
            return ResponseEntity.ok(savedDetails);
        } catch (Exception e) {
            logger.error("Error saving tax details: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{userId}/tax-details")
    public ResponseEntity<?> getTaxDetails(@PathVariable String userId) {
        if (!userRepository.existsById(userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found"));
        }

        Optional<TaxDetails> taxDetails = taxDetailsService.getByUserId(userId);
        return taxDetails.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Tax details not found")));
    }

    @GetMapping("/{userId}/dependents")
    public ResponseEntity<?> getDependents(@PathVariable String userId) {
        try {
            if (!userRepository.existsById(userId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found"));
            }

            List<Dependent> dependents = dependentService.getDependentsByUserId(userId);
            return ResponseEntity.ok(dependents);
        } catch (Exception e) {
            logger.error("Error fetching dependents: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{userId}/dependents")
    public ResponseEntity<?> addDependent(@PathVariable String userId, @RequestBody Dependent dependent) {
        try {
            if (!userRepository.existsById(userId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found"));
            }

            dependent.setUserId(userId);
            Dependent savedDependent = dependentService.addDependent(dependent);
            return ResponseEntity.ok(savedDependent);
        } catch (Exception e) {
            logger.error("Error adding dependent: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{userId}/dependents/batch")
    public ResponseEntity<?> addMultipleDependents(@PathVariable String userId, @RequestBody List<Dependent> dependents) {
        try {
            if (!userRepository.existsById(userId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found"));
            }

            dependents.forEach(d -> d.setUserId(userId));
            List<Dependent> savedDependents = dependentService.addMultipleDependents(dependents);
            return ResponseEntity.ok(savedDependents);
        } catch (Exception e) {
            logger.error("Error adding multiple dependents: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{userId}/dependents/{dependentId}")
    public ResponseEntity<?> deleteDependent(@PathVariable String userId, @PathVariable String dependentId) {
        try {
            if (!userRepository.existsById(userId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found"));
            }

            dependentService.deleteDependent(dependentId, userId);
            return ResponseEntity.ok(Map.of("message", "Dependent deleted successfully"));
        } catch (Exception e) {
            logger.error("Error deleting dependent: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{userId}/dependents")
    public ResponseEntity<?> deleteAllDependents(@PathVariable String userId) {
        try {
            if (!userRepository.existsById(userId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found"));
            }

            dependentService.deleteAllDependentsForUser(userId);
            return ResponseEntity.ok(Map.of("message", "All dependents deleted successfully"));
        } catch (Exception e) {
            logger.error("Error deleting all dependents: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{userId}/documents")
    public ResponseEntity<?> uploadDocument(
            @PathVariable String userId,
            @RequestParam("document") String documentType,
            @RequestParam(value = "remarks", required = false) String remarks,
            @RequestParam("file") MultipartFile file) {
        
        try {
            if (!userRepository.existsById(userId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "User not found"));
            }

            TaxDocument savedDocument = taxDocumentService.saveDocument(userId, documentType, remarks, file);
            return ResponseEntity.ok(savedDocument);
        } catch (Exception e) {
            logger.error("Error uploading document: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{userId}/documents")
    public ResponseEntity<?> getDocuments(@PathVariable String userId) {
        try {
            if (!userRepository.existsById(userId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "User not found"));
            }

            List<TaxDocument> documents = taxDocumentService.getDocumentsByUserId(userId);
            return ResponseEntity.ok(documents);
        } catch (Exception e) {
            logger.error("Error fetching documents: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{userId}/documents/{documentId}")
    public ResponseEntity<?> deleteDocument(@PathVariable String userId, @PathVariable String documentId) {
        try {
            if (!userRepository.existsById(userId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "User not found"));
            }

            taxDocumentService.deleteDocument(documentId, userId);
            return ResponseEntity.ok(Map.of("message", "Document deleted successfully"));
        } catch (Exception e) {
            logger.error("Error deleting document: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{userId}/income-documents")
    public ResponseEntity<?> uploadIncomeDocument(
            @PathVariable String userId,
            @RequestParam("source") String source,
            @RequestParam("personType") String personType,
            @RequestParam("file") MultipartFile file) {
        
        try {
            if (!userRepository.existsById(userId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "User not found"));
            }

            IncomeDocument savedDocument = incomeDocumentService.saveDocument(userId, source, personType, file);
            return ResponseEntity.ok(savedDocument);
        } catch (Exception e) {
            logger.error("Error uploading income document: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{userId}/income-documents")
    public ResponseEntity<?> getIncomeDocuments(@PathVariable String userId) {
        try {
            if (!userRepository.existsById(userId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "User not found"));
            }

            List<IncomeDocument> documents = incomeDocumentService.getDocumentsByUserId(userId);
            return ResponseEntity.ok(documents);
        } catch (Exception e) {
            logger.error("Error fetching income documents: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{userId}/income-documents/{source}")
    public ResponseEntity<?> getIncomeDocumentsBySource(
            @PathVariable String userId,
            @PathVariable String source) {
        try {
            if (!userRepository.existsById(userId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "User not found"));
            }

            List<IncomeDocument> documents = incomeDocumentService.getDocumentsByUserIdAndSource(userId, source);
            return ResponseEntity.ok(documents);
        } catch (Exception e) {
            logger.error("Error fetching income documents by source: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{userId}/income-documents/{documentId}")
    public ResponseEntity<?> deleteIncomeDocument(
            @PathVariable String userId,
            @PathVariable String documentId) {
        try {
            if (!userRepository.existsById(userId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "User not found"));
            }

            incomeDocumentService.deleteDocument(documentId, userId);
            return ResponseEntity.ok(Map.of("message", "Income document deleted successfully"));
        } catch (Exception e) {
            logger.error("Error deleting income document: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{userId}/deduction-documents")
    public ResponseEntity<?> uploadDeductionDocument(
            @PathVariable String userId,
            @RequestParam("source") String source,
            @RequestParam("personType") String personType,
            @RequestParam("file") MultipartFile file) {
        
        try {
            if (!userRepository.existsById(userId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "User not found"));
            }

            DeductionDocument savedDocument = deductionDocumentService.saveDocument(userId, source, personType, file);
            return ResponseEntity.ok(savedDocument);
        } catch (Exception e) {
            logger.error("Error uploading deduction document: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{userId}/deduction-documents")
    public ResponseEntity<?> getDeductionDocuments(@PathVariable String userId) {
        try {
            if (!userRepository.existsById(userId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "User not found"));
            }

            List<DeductionDocument> documents = deductionDocumentService.getDocumentsByUserId(userId);
            return ResponseEntity.ok(documents);
        } catch (Exception e) {
            logger.error("Error fetching deduction documents: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{userId}/deduction-documents/{source}")
    public ResponseEntity<?> getDeductionDocumentsBySource(
            @PathVariable String userId,
            @PathVariable String source) {
        try {
            if (!userRepository.existsById(userId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "User not found"));
            }

            List<DeductionDocument> documents = deductionDocumentService.getDocumentsByUserIdAndSource(userId, source);
            return ResponseEntity.ok(documents);
        } catch (Exception e) {
            logger.error("Error fetching deduction documents by source: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{userId}/deduction-documents/{documentId}")
    public ResponseEntity<?> deleteDeductionDocument(
            @PathVariable String userId,
            @PathVariable String documentId) {
        try {
            if (!userRepository.existsById(userId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "User not found"));
            }

            deductionDocumentService.deleteDocument(documentId, userId);
            return ResponseEntity.ok(Map.of("message", "Deduction document deleted successfully"));
        } catch (Exception e) {
            logger.error("Error deleting deduction document: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{userId}/disclosure-documents")
    public ResponseEntity<?> uploadDisclosureDocument(
            @PathVariable String userId,
            @RequestParam("source") String source,
            @RequestParam("personType") String personType,
            @RequestParam("file") MultipartFile file) {
        
        try {
            if (!userRepository.existsById(userId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "User not found"));
            }

            DisclosureDocument savedDocument = disclosureDocumentService.saveDocument(userId, source, personType, file);
            return ResponseEntity.ok(savedDocument);
        } catch (Exception e) {
            logger.error("Error uploading disclosure document: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{userId}/disclosure-documents")
    public ResponseEntity<?> getDisclosureDocuments(@PathVariable String userId) {
        try {
            if (!userRepository.existsById(userId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "User not found"));
            }

            List<DisclosureDocument> documents = disclosureDocumentService.getDocumentsByUserId(userId);
            return ResponseEntity.ok(documents);
        } catch (Exception e) {
            logger.error("Error fetching disclosure documents: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{userId}/disclosure-documents/{source}")
    public ResponseEntity<?> getDisclosureDocumentsBySource(
            @PathVariable String userId,
            @PathVariable String source) {
        try {
            if (!userRepository.existsById(userId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "User not found"));
            }

            List<DisclosureDocument> documents = disclosureDocumentService.getDocumentsByUserIdAndSource(userId, source);
            return ResponseEntity.ok(documents);
        } catch (Exception e) {
            logger.error("Error fetching disclosure documents by source: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{userId}/disclosure-documents/{documentId}")
    public ResponseEntity<?> deleteDisclosureDocument(
            @PathVariable String userId,
            @PathVariable String documentId) {
        try {
            if (!userRepository.existsById(userId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "User not found"));
            }

            disclosureDocumentService.deleteDocument(documentId, userId);
            return ResponseEntity.ok(Map.of("message", "Disclosure document deleted successfully"));
        } catch (Exception e) {
            logger.error("Error deleting disclosure document: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}