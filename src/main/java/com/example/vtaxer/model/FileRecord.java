package com.example.vtaxer.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "file_records")
public class FileRecord {

    @Id
    private String id;
    private String userId;
    private String fileName;
    private String fileType;
    private long fileSize;
    private byte[] fileData;
    private String message;
    private LocalDateTime uploadedAt;
    private String source; // 'admin' or 'customer'
    private String documentType; // For customer uploads
    private String remarks; // For customer uploads

    // Constructors
    public FileRecord() {
        this.uploadedAt = LocalDateTime.now();
    }

    public FileRecord(String userId, String fileName, String fileType, 
                     long fileSize, byte[] fileData, String source) {
        this();
        this.userId = userId;
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.fileData = fileData;
        this.source = source;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    // Helper methods
    @Override
    public String toString() {
        return "FileRecord{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileType='" + fileType + '\'' +
                ", fileSize=" + fileSize +
                ", uploadedAt=" + uploadedAt +
                ", source='" + source + '\'' +
                ", documentType='" + documentType + '\'' +
                '}';
    }

    public boolean isAdminUpload() {
        return "admin".equalsIgnoreCase(source);
    }

    public boolean isCustomerUpload() {
        return "customer".equalsIgnoreCase(source);
    }
}