package com.haeil.full.cases.repository;

import com.haeil.full.cases.domain.CaseDocument;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CaseDocumentRepository extends JpaRepository<CaseDocument, Long> {
    List<CaseDocument> findByCasesId(Long caseId);

    @Modifying
    @Query("DELETE FROM CaseDocument cd WHERE cd.id = :id")
    void deleteByIdDirect(@Param("id") Long id);
}
