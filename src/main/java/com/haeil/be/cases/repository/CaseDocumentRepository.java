package com.haeil.be.cases.repository;

import com.haeil.be.cases.domain.CaseDocument;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CaseDocumentRepository extends JpaRepository<CaseDocument, Long> {
    List<CaseDocument> findByCasesId(Long caseId);
}
