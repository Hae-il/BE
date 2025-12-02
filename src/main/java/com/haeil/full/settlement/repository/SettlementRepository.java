package com.haeil.full.settlement.repository;

import com.haeil.full.cases.domain.type.CaseStatus;
import com.haeil.full.settlement.domain.Settlement;
import com.haeil.full.user.domain.User;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SettlementRepository extends JpaRepository<Settlement, Long> {

    /**
     * 진행중 또는 완료된 사건 기준으로 정산서 리스트를 조회합니다. 정산서가 없는 사건도 포함하여 반환합니다.
     *
     * @return 사건 정보와 정산서 정보를 포함한 리스트 (정산서가 없으면 null)
     */
    @Query(
            "SELECT c, s FROM com.haeil.full.cases.domain.Cases c "
                    + "LEFT JOIN com.haeil.full.settlement.domain.Settlement s ON s.cases.id = c.id "
                    + "WHERE c.caseStatus IN :caseStatuses "
                    + "ORDER BY c.createdDate DESC")
    List<Object[]> findSettlementsByCaseStatuses(
            @Param("caseStatuses") List<CaseStatus> caseStatuses);

    @Query(
            "SELECT s FROM Settlement s "
                    + "JOIN s.cases c "
                    + "WHERE s.paymentDueDate BETWEEN :start AND :end "
                    + "AND c.attorney = :attorney")
    List<Settlement> findAllByPaymentDueDateBetweenAndAttorney(
            @Param("start") LocalDate start,
            @Param("end") LocalDate end,
            @Param("attorney") User attorney);
}
