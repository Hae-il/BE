package com.haeil.be.cases.repository;

import com.haeil.be.cases.domain.Cases;
import com.haeil.be.cases.domain.type.CaseStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CasesRepository extends JpaRepository<Cases, Long> {
    List<Cases> findByCaseStatus(CaseStatus caseStatus);
}
