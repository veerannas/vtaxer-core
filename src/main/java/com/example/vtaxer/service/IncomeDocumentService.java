// IncomeDocumentService.java
package com.example.vtaxer.service;

import com.example.vtaxer.model.IncomeDocument;
import com.example.vtaxer.repository.IncomeDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
public class IncomeDocumentService {

    @Autowired
    private IncomeDocumentRepository incomeDocumentRepository;

    public IncomeDocument saveDocument(String userId, String source, String personType, MultipartFile file) throws IOException {
        IncomeDocument document = new IncomeDocument();
        document.setUserId(userId);
        document.setSource(source);
        document.setPersonType(personType);
        document.setFileName(StringUtils.cleanPath(file.getOriginalFilename()));
        document.setFileContent(Base64.getEncoder().encodeToString(file.getBytes()));
        document.setUploadDate(new Date().toInstant().toString());
        
        return incomeDocumentRepository.save(document);
    }

    public List<IncomeDocument> getDocumentsByUserId(String userId) {
        return incomeDocumentRepository.findByUserId(userId);
    }

    public List<IncomeDocument> getDocumentsByUserIdAndSource(String userId, String source) {
        return incomeDocumentRepository.findByUserIdAndSource(userId, source);
    }

    public void deleteDocument(String id, String userId) {
        incomeDocumentRepository.deleteByIdAndUserId(id, userId);
    }

    public void deleteAllDocumentsForUser(String userId) {
        incomeDocumentRepository.deleteByUserId(userId);
    }
}