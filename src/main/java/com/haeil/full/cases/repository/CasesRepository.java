package com.haeil.full.cases.repository;

import com.haeil.full.cases.domain.Cases;
import com.haeil.full.cases.domain.type.CaseStatus;
import com.haeil.full.user.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CasesRepository extends JpaRepository<Cases, Long> {
    List<Cases> findByCaseStatus(CaseStatus caseStatus);

    List<Cases> findByCaseStatusAndAttorney(CaseStatus caseStatus, User attorney);
}
