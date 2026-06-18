package com.example.vtaxer.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Document(collection = "deduction_documents")
public class DeductionDocument {
    @Id
    private String id;
    private String userId;
    private String source;
    private String personType; // "taxpayer" or "spouse"
    private String fileName;
    private String fileContent; // base64 encoded
    private String uploadDate;

    // Constructors
    public DeductionDocument() {
    }

    public DeductionDocument(String userId, String source, String personType, String fileName, String fileContent) {
        this.userId = userId;
        this.source = source;
        this.personType = personType;
        this.fileName = fileName;
        this.fileContent = fileContent;
        this.uploadDate = new Date().toInstant().toString();
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPersonType() {
        return personType;
    }

    public void setPersonType(String personType) {
        this.personType = personType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileContent() {
        return fileContent;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    @Override
    public String toString() {
        return "DeductionDocument{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", source='" + source + '\'' +
                ", personType='" + personType + '\'' +
                ", fileName='" + fileName + '\'' +
                ", uploadDate='" + uploadDate + '\'' +
                '}';
    }
}