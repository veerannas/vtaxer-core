package com.example.vtaxer.service;

import com.example.vtaxer.model.DisclosureDocument;
import com.example.vtaxer.repository.DisclosureDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
public class DisclosureDocumentService {

    @Autowired
    private DisclosureDocumentRepository disclosureDocumentRepository;

    public DisclosureDocument saveDocument(String userId, String source, String personType, MultipartFile file) throws IOException {
        DisclosureDocument document = new DisclosureDocument();
        document.setUserId(userId);
        document.setSource(source);
        document.setPersonType(personType);
        document.setFileName(StringUtils.cleanPath(file.getOriginalFilename()));
        document.setFileContent(Base64.getEncoder().encodeToString(file.getBytes()));
        document.setUploadDate(new Date().toInstant().toString());
        
        return disclosureDocumentRepository.save(document);
    }

    public List<DisclosureDocument> getDocumentsByUserId(String userId) {
        return disclosureDocumentRepository.findByUserId(userId);
    }

    public List<DisclosureDocument> getDocumentsByUserIdAndSource(String userId, String source) {
        return disclosureDocumentRepository.findByUserIdAndSource(userId, source);
    }

    public void deleteDocument(String id, String userId) {
        disclosureDocumentRepository.deleteByIdAndUserId(id, userId);
    }

    public void deleteAllDocumentsForUser(String userId) {
        disclosureDocumentRepository.deleteByUserId(userId);
    }
}