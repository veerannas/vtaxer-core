// TaxDocumentService.java
package com.example.vtaxer.service;

import com.example.vtaxer.model.TaxDocument;
import com.example.vtaxer.repository.TaxDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
public class TaxDocumentService {

    @Autowired
    private TaxDocumentRepository taxDocumentRepository;

    public TaxDocument saveDocument(String userId, String documentType, String remarks, MultipartFile file) throws IOException {
        TaxDocument document = new TaxDocument();
        document.setUserId(userId);
        document.setDocumentType(documentType);
        document.setRemarks(remarks);
        document.setFileName(StringUtils.cleanPath(file.getOriginalFilename()));
        document.setFileContent(Base64.getEncoder().encodeToString(file.getBytes()));
        document.setUploadDate(new Date().toInstant().toString());
        
        return taxDocumentRepository.save(document);
    }

    public List<TaxDocument> getDocumentsByUserId(String userId) {
        return taxDocumentRepository.findByUserId(userId);
    }

    public void deleteDocument(String id, String userId) {
        taxDocumentRepository.deleteByIdAndUserId(id, userId);
    }

    public void deleteAllDocumentsForUser(String userId) {
        taxDocumentRepository.deleteByUserId(userId);
    }
}