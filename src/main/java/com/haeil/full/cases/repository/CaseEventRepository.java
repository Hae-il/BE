package com.haeil.full.cases.repository;

import com.haeil.full.cases.domain.CaseEvent;
import com.haeil.full.user.domain.User;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CaseEventRepository extends JpaRepository<CaseEvent, Long> {

    @Query(
            "SELECT ce FROM CaseEvent ce JOIN ce.cases c "
                    + "WHERE c.client.id = :clientId "
                    + "AND ce.date >= CURRENT_TIMESTAMP "
                    + "ORDER BY ce.date ASC")
    List<CaseEvent> findUpcomingEventByClientId(@Param("clientId") Long clientId);

    @Query(
            "SELECT ce FROM CaseEvent ce "
                    + "JOIN ce.cases c "
                    + "WHERE ce.date BETWEEN :start AND :end "
                    + "AND c.attorney = :attorney")
    List<CaseEvent> findAllByDateBetweenAndAttorney(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("attorney") User attorney);
}
