package com.haeil.full.cases.dto.request;

import com.haeil.full.cases.domain.type.DocumentType;

public record CaseDocumentRequest(DocumentType documentType, String description) {}
