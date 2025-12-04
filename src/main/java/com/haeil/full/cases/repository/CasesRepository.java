package com.haeil.full.cases.repository;

import com.haeil.full.cases.domain.Cases;
import com.haeil.full.cases.domain.type.CaseStatus;
import com.haeil.full.contract.domain.type.ContractStatus;
import com.haeil.full.user.domain.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CasesRepository extends JpaRepository<Cases, Long> {
    List<Cases> findByCaseStatus(CaseStatus caseStatus);

    List<Cases> findByCaseStatusAndAttorney(CaseStatus caseStatus, User attorney);

    @Query(
            "SELECT c FROM Cases c LEFT JOIN c.contract ct "
                    + "WHERE (:status IS NULL) "
                    + "OR (:status = 'AWAITING' AND (ct IS NULL OR ct.status = 'AWAITING')) "
                    + "OR (ct.status = :status)")
    Page<Cases> findByContractStatus(@Param("status") ContractStatus status, Pageable pageable);
}
