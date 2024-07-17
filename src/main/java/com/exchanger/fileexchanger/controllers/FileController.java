package com.exchanger.fileexchanger.controllers;


import com.exchanger.fileexchanger.Entities.Document;
import com.exchanger.fileexchanger.Services.DocumentService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/")
public class FileController {
    private final DocumentService documentService;

    public FileController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String>  uploadDocument(@RequestParam("file") MultipartFile multipartFile, @RequestParam String password) {
        try {
            Document newDocument = new Document();
            newDocument.setFileName(multipartFile.getOriginalFilename());
            newDocument.setFileExtencion(multipartFile.getContentType());
            newDocument.setContent(multipartFile.getBytes());
            newDocument.setFileSize(multipartFile.getSize());
            newDocument.setAddingDate(new Date());
            Document doc;
            if(!password.isEmpty()){
                doc = documentService.addDocument(newDocument, password);
            }else{
                doc = documentService.addDocument(newDocument);
            }
            String message = doc.getFileUrl();
            String jsonResponse = "{\"message\": \"" + message + "\"}";

            return ResponseEntity.ok(jsonResponse);
        } catch (IOException e) {
            return ResponseEntity.status(500).build();
        }
    }
    @GetMapping("/files")
    public ResponseEntity<List<Document>> getFile() {
        List<Document> files = documentService.findAll();
        return ResponseEntity.ok(files);
    }

    @GetMapping("/fileInfo/{fileUrl}")
    public ResponseEntity<Document> fileInfo(@PathVariable String fileUrl){
        Document fileEntity = documentService.findByUrl(fileUrl).orElse(null);
        if (fileEntity != null) {
            fileEntity.setContent(null);
            return ResponseEntity.ok(fileEntity);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/download/{fileUrl}")
    public ResponseEntity<?> downloadFile(@PathVariable String fileUrl,
                                          @Param("password") String password) {
        Document fileEntity = documentService.findByUrl(fileUrl).orElse(null);

        if (fileEntity != null && (fileEntity.getFilePassword() == null || fileEntity.getFilePassword().equals(documentService.checkPassword(password, fileUrl)))) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(fileEntity.getFileExtencion()));
            headers.setContentDisposition(ContentDisposition.attachment().filename(fileEntity.getFileName()).build());
            ByteArrayResource resource = new ByteArrayResource(fileEntity.getContent());
            return ResponseEntity.ok().headers(headers).body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
