package com.haeil.be.file.domain;

import com.haeil.be.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "file")
@Entity
@Getter
@NoArgsConstructor
public class FileEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "original_filename", nullable = false)
    private String originalFilename;

    @Column(name = "stored_filename", nullable = false, unique = true)
    private String storedFilename;

    @Column(name = "file_url", nullable = false)
    private String fileUrl;

    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @Column(name = "content_type")
    private String contentType;

    @Builder
    public FileEntity(
            String originalFilename,
            String storedFilename,
            String fileUrl,
            Long fileSize,
            String contentType) {
        this.originalFilename = originalFilename;
        this.storedFilename = storedFilename;
        this.fileUrl = fileUrl;
        this.fileSize = fileSize;
        this.contentType = contentType;
    }
}
