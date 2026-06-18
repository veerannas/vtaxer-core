package com.example.vtaxer.service;

import com.example.vtaxer.model.DeductionDocument;
import com.example.vtaxer.repository.DeductionDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
public class DeductionDocumentService {

    @Autowired
    private DeductionDocumentRepository deductionDocumentRepository;

    public DeductionDocument saveDocument(String userId, String source, String personType, MultipartFile file) throws IOException {
        DeductionDocument document = new DeductionDocument();
        document.setUserId(userId);
        document.setSource(source);
        document.setPersonType(personType);
        document.setFileName(StringUtils.cleanPath(file.getOriginalFilename()));
        document.setFileContent(Base64.getEncoder().encodeToString(file.getBytes()));
        document.setUploadDate(new Date().toInstant().toString());
        
        return deductionDocumentRepository.save(document);
    }

    public List<DeductionDocument> getDocumentsByUserId(String userId) {
        return deductionDocumentRepository.findByUserId(userId);
    }

    public List<DeductionDocument> getDocumentsByUserIdAndSource(String userId, String source) {
        return deductionDocumentRepository.findByUserIdAndSource(userId, source);
    }

    public void deleteDocument(String id, String userId) {
        deductionDocumentRepository.deleteByIdAndUserId(id, userId);
    }

    public void deleteAllDocumentsForUser(String userId) {
        deductionDocumentRepository.deleteByUserId(userId);
    }
}