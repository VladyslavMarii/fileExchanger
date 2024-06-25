package com.exchanger.fileexchanger.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name= "filestorage")
@Getter
@Setter
@NoArgsConstructor
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 512, nullable = false)
    private String fileName;
    private String fileExtencion;
    private long fileSize;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "adding_date", nullable = false)
    private Date addingDate;
    private byte[] content;
    private String filePassword;
    @Column(unique = true)
    private String fileUrl;
}
