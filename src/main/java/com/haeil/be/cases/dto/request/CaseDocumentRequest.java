package com.haeil.be.cases.dto.request;

import com.haeil.be.cases.domain.type.DocumentType;

public record CaseDocumentRequest(
        DocumentType documentType,
        String description
) {}

