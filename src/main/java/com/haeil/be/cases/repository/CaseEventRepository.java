package com.haeil.be.cases.repository;

import com.haeil.be.cases.domain.CaseEvent;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CaseEventRepository extends JpaRepository<CaseEvent, Long> {

    // 특정 의뢰인의 다가오는 일정을 시간 순으로 조회하는 쿼리
    @Query(
            "SELECT ce FROM CaseEvent ce JOIN ce.cases c "
                    + "WHERE c.client.id = :clientId "
                    + "AND ce.date >= CURRENT_TIMESTAMP "
                    + "ORDER BY ce.date ASC")
    List<CaseEvent> findUpcomingEventByClientId(@Param("clientId") Long clientId);
}
