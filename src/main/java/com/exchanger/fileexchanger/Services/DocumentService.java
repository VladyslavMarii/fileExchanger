package com.exchanger.fileexchanger.Services;

import com.exchanger.fileexchanger.Entities.Document;
import com.exchanger.fileexchanger.Repositories.DocumentRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DocumentService {

    private final DocumentRepo documentRepo;
    @Scheduled(fixedDelay = 60*1000)
    public void cleanupExpiredDocuments() {
        Date cutoffTime = new Date(System.currentTimeMillis() - (24 * 60 * 60 * 1000)); // Current time - 24 hours
        List<Document> oldDocuments = documentRepo.findAllByAddingDateBefore(cutoffTime);
        documentRepo.deleteAll(oldDocuments);
    }

    @Autowired
    public DocumentService(DocumentRepo documentRepo) {
        this.documentRepo = documentRepo;
    }


    public Document addDocument(Document document){
        document.setFileUrl(UUID.randomUUID().toString());
        return documentRepo.save(document);
    }
    public Document addDocument(Document document, String password){
        document.setFilePassword(password.hashCode()+""+document.getAddingDate().hashCode());
        document.setFileUrl(UUID.randomUUID().toString());
        return documentRepo.save(document);
    }

    public Optional<Document> findByUrl(String url){
        return documentRepo.findDocumentByFileUrl(url);
    }
    public String checkPassword(String password, String url){
        Optional<Document> check = findByUrl(url);
        return password.hashCode()+""+check.get().getAddingDate().hashCode();
    }
    public List<Document> findAll() {
        return documentRepo.findAll();
    }
}
