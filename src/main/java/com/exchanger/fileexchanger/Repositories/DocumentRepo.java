package com.exchanger.fileexchanger.Repositories;

import com.exchanger.fileexchanger.Entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepo extends JpaRepository<Document, Long> {
//    @Query("SELECT new Document(d.id, d.fileName) FROM Document d")
    List<Document> findAll();
    Optional<Document> findDocumentByFileUrl(String fileUrl);
    List<Document> findAllByAddingDateBefore(Date cutoffTime);

}