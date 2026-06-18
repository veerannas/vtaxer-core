package com.example.vtaxer.controller;

import com.example.vtaxer.model.*;
import com.example.vtaxer.repository.*;
import com.example.vtaxer.service.TaxDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.util.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.IOException;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommunicationRepository communicationRepository;

    @Autowired
    private FileRecordRepository fileRecordRepository;

    @Autowired
    private RevenueRepository revenueRepository;

    @Autowired
    private TaxDocumentService taxDocumentService;

    // Admin Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Admin loginRequest) {
        Optional<Admin> admin = adminRepository.findByEmailAndPassword(
            loginRequest.getEmail(), loginRequest.getPassword()
        );

        if (admin.isPresent()) {
            return ResponseEntity.ok(admin.get());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }

    // Get All Customers
    @GetMapping("/customers")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    // Create New Customer
    @PostMapping("/customers")
    public ResponseEntity<?> createCustomer(@RequestBody User user) {
        try {
            if (userRepository.existsByEmail(user.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Email already exists");
            }
            
            user.setCreatedAt(LocalDateTime.now());
            User savedUser = userRepository.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to create customer: " + e.getMessage());
        }
    }

    // Update Customer
    @PutMapping("/customers/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable String id, @RequestBody User updatedUser) {
        try {
            Optional<User> userOpt = userRepository.findById(id);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            User user = userOpt.get();
            user.setFirstName(updatedUser.getFirstName());
            user.setLastName(updatedUser.getLastName());
            user.setEmail(updatedUser.getEmail());
            user.setPhone(updatedUser.getPhone());
            user.setSsn(updatedUser.getSsn());
            user.setState(updatedUser.getState());
            user.setStatus(updatedUser.getStatus());
            user.setLastTaxReturnYear(updatedUser.getLastTaxReturnYear());

            User savedUser = userRepository.save(user);
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to update customer: " + e.getMessage());
        }
    }

    // Admin uploads files to customer
    @PostMapping("/customers/{id}/upload")
    public ResponseEntity<?> uploadFiles(
            @PathVariable String id,
            @RequestParam("file") List<MultipartFile> files,
            @RequestParam(value = "message", required = false) String message) {
        try {
            if (!userRepository.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found");
            }

            List<FileRecord> savedFiles = new ArrayList<>();
            for (MultipartFile file : files) {
                if (file.isEmpty()) continue;

                FileRecord record = new FileRecord();
                record.setUserId(id);
                record.setFileName(file.getOriginalFilename());
                record.setFileType(file.getContentType());
                record.setFileSize(file.getSize());
                record.setFileData(file.getBytes());
                record.setMessage(message);
                record.setUploadedAt(LocalDateTime.now());
                record.setSource("admin");

                FileRecord savedRecord = fileRecordRepository.save(record);
                savedFiles.add(savedRecord);
            }

            if (message != null && !message.trim().isEmpty()) {
                Communication comm = new Communication();
                comm.setUserId(id);
                comm.setMessage(message);
                comm.setTimestamp(LocalDateTime.now());
                communicationRepository.save(comm);
            }

            return ResponseEntity.ok(Map.of(
                "message", "Files uploaded successfully",
                "files", savedFiles
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("File upload failed: " + e.getMessage());
        }
    }

    // Get all files for a customer (both admin and customer uploads)
    @GetMapping("/customers/{userId}/all-files")
    public ResponseEntity<?> getAllFilesByUserId(@PathVariable String userId) {
        try {
            if (!userRepository.existsById(userId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found");
            }

            // Get admin-uploaded files
            List<FileRecord> adminFiles = fileRecordRepository.findByUserId(userId);
            
            // Get customer-uploaded tax documents
            List<TaxDocument> customerFiles = taxDocumentService.getDocumentsByUserId(userId);
            
            // Format response
            Map<String, Object> response = new HashMap<>();
            response.put("adminFiles", adminFiles);
            response.put("customerFiles", customerFiles);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to fetch files: " + e.getMessage());
        }
    }

    // Download file
    @GetMapping("/customers/{fileId}/download")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileId) {
        try {
            Optional<FileRecord> fileOpt = fileRecordRepository.findById(fileId);
            if (fileOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            FileRecord file = fileOpt.get();
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, 
                    "attachment; filename=\"" + file.getFileName() + "\"")
                .contentType(MediaType.parseMediaType(file.getFileType()))
                .body(file.getFileData());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get customer communications
    @GetMapping("/customers/{id}/communications")
    public ResponseEntity<?> getCustomerCommunications(@PathVariable String id) {
        try {
            List<Communication> comms = communicationRepository.findByUserId(id);
            return ResponseEntity.ok(comms);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to fetch communications: " + e.getMessage());
        }
    }

    // Delete file
    @DeleteMapping("/customers/files/{fileId}")
    public ResponseEntity<?> deleteFile(@PathVariable String fileId) {
        try {
            if (!fileRecordRepository.existsById(fileId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("File not found");
            }

            fileRecordRepository.deleteById(fileId);
            return ResponseEntity.ok("File deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to delete file: " + e.getMessage());
        }
    }

    // Get customer stats
    @GetMapping("/stats")
    public ResponseEntity<?> getCustomerStats() {
        try {
            Map<String, Object> stats = new HashMap<>();
            List<User> allCustomers = userRepository.findAll();

            stats.put("totalCustomers", allCustomers.size());

            int currentYear = LocalDate.now().getYear();
            long currentYearCustomers = allCustomers.stream()
                .filter(user -> user.getCreatedAt() != null && 
                       user.getCreatedAt().getYear() == currentYear)
                .count();
            stats.put("currentYearCustomers", currentYearCustomers);

            long totalFiled = allCustomers.stream()
                .filter(user -> user.getStatus() != null && 
                       user.getStatus().equalsIgnoreCase("filled"))
                .count();
            stats.put("totalFiled", totalFiled);

            long currentYearFiled = allCustomers.stream()
                .filter(user -> user.getStatus() != null && 
                       user.getStatus().equalsIgnoreCase("filled") &&
                       user.getLastTaxReturnYear() != null &&
                       String.valueOf(currentYear).equals(user.getLastTaxReturnYear()))
                .count();
            stats.put("currentYearFiled", currentYearFiled);

            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to get stats: " + e.getMessage());
        }
    }

    // Get yearly revenue data
    @GetMapping("/chart/yearly-revenue")
    public ResponseEntity<?> getYearlyRevenue() {
        try {
            List<Revenue> revenues = revenueRepository.findByTypeOrderByNameAsc("yearly");
            List<Map<String, Object>> data = new ArrayList<>();

            for (Revenue r : revenues) {
                Map<String, Object> entry = new HashMap<>();
                entry.put("name", r.getName());
                entry.put("revenue", r.getAmount());
                data.add(entry);
            }

            return ResponseEntity.ok(data);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to fetch revenue data: " + e.getMessage());
        }
    }

    // Handle customer file uploads (from customer to admin)
    @PostMapping("/customers/{id}/customer-upload")
    public ResponseEntity<?> handleCustomerUpload(
            @PathVariable String id,
            @RequestParam("file") MultipartFile file,
            @RequestParam("documentType") String documentType,
            @RequestParam(value = "remarks", required = false) String remarks) {
        try {
            if (!userRepository.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found");
            }

            if (file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("File cannot be empty");
            }

            // Save through the tax document service
            TaxDocument savedDocument = taxDocumentService.saveDocument(
                id,
                documentType,
                remarks,
                file
            );

            return ResponseEntity.ok(Map.of(
                "message", "File uploaded successfully",
                "documentId", savedDocument.getId()
            ));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("File upload failed: " + e.getMessage());
        }
    }
    @GetMapping("/customers/files/{fileId}/download")
public ResponseEntity<byte[]> downloadFileById(@PathVariable String fileId) {
    Optional<FileRecord> fileOpt = fileRecordRepository.findById(fileId);
    if (fileOpt.isEmpty()) {
        return ResponseEntity.notFound().build();
    }

    FileRecord file = fileOpt.get();
    
    // Set appropriate content type
    MediaType mediaType;
    if (file.getFileType() != null) {
        try {
            mediaType = MediaType.parseMediaType(file.getFileType());
        } catch (Exception e) {
            mediaType = MediaType.APPLICATION_OCTET_STREAM;
        }
    } else {
        mediaType = MediaType.APPLICATION_OCTET_STREAM;
    }

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
        .contentType(mediaType)
        .body(file.getFileData());
}
    
}
