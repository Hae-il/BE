package com.haeil.full.cases.repository;

import com.haeil.full.cases.domain.Cases;
import com.haeil.full.cases.domain.type.CaseStatus;
import com.haeil.full.cases.domain.type.CaseType;
import com.haeil.full.contract.domain.type.ContractStatus;
import com.haeil.full.user.domain.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CasesRepository extends JpaRepository<Cases, Long> {
    List<Cases> findByCaseStatusOrderByIdDesc(CaseStatus caseStatus);

    List<Cases> findByCaseStatusAndAttorneyOrderByIdDesc(CaseStatus caseStatus, User attorney);

    @Query(
            "SELECT c FROM Cases c WHERE c.caseStatus = :caseStatus AND (:caseType IS NULL OR c.caseType = :caseType)")
    Page<Cases> findByCaseStatusAndCaseType(
            @Param("caseStatus") CaseStatus caseStatus,
            @Param("caseType") CaseType caseType,
            Pageable pageable);

    @Query(
            "SELECT c FROM Cases c WHERE c.caseStatus = :caseStatus AND c.attorney = :attorney AND (:caseType IS NULL OR c.caseType = :caseType)")
    Page<Cases> findByCaseStatusAndAttorneyAndCaseType(
            @Param("caseStatus") CaseStatus caseStatus,
            @Param("attorney") User attorney,
            @Param("caseType") CaseType caseType,
            Pageable pageable);

    @Query(
            "SELECT c FROM Cases c LEFT JOIN c.contract ct "
                    + "WHERE (:status IS NULL) "
                    + "OR (:status = 'AWAITING' AND (ct IS NULL OR ct.status = 'AWAITING')) "
                    + "OR (ct.status = :status)")
    Page<Cases> findByContractStatus(@Param("status") ContractStatus status, Pageable pageable);
}
