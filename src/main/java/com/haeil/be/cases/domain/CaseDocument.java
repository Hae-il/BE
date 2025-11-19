package com.haeil.be.cases.domain;

import com.haeil.be.cases.domain.type.DocumentType;
import com.haeil.be.file.domain.FileEntity;
import com.haeil.be.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "case_document")
@Entity
@Getter
@NoArgsConstructor
public class CaseDocument extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type")
    private DocumentType documentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id")
    private Cases cases;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id")
    private FileEntity file;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Builder
    public CaseDocument(
            DocumentType documentType,
            Cases cases,
            FileEntity file,
            String description) {
        this.documentType = documentType;
        this.cases = cases;
        this.file = file;
        this.description = description;
    }
}
