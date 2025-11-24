package com.haeil.full.cases.dto.response;

import com.haeil.full.cases.domain.CaseDocument;
import com.haeil.full.cases.domain.type.DocumentType;

public record CaseDocumentResponse(
        Long documentId,
        DocumentType documentType,
        String originalFilename,
        String fileUrl,
        Long fileSize,
        String description) {
    public static CaseDocumentResponse from(CaseDocument caseDocument) {
        return new CaseDocumentResponse(
                caseDocument.getId(),
                caseDocument.getDocumentType(),
                caseDocument.getFile() != null
                        ? caseDocument.getFile().getOriginalFilename()
                        : null,
                caseDocument.getFile() != null ? caseDocument.getFile().getFileUrl() : null,
                caseDocument.getFile() != null ? caseDocument.getFile().getFileSize() : null,
                caseDocument.getDescription());
    }
}
