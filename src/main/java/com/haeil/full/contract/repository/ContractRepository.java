package com.haeil.full.contract.repository;

import com.haeil.full.contract.domain.Contract;
import com.haeil.full.user.domain.User;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContractRepository extends JpaRepository<Contract, Long> {

    @Query(
            "SELECT c FROM Contract c "
                    + "JOIN c.cases cs "
                    + "WHERE c.dueDate BETWEEN :start AND :end "
                    + "AND cs.attorney = :attorney")
    List<Contract> findAllByDueDateBetweenAndAttorney(
            @Param("start") LocalDate start,
            @Param("end") LocalDate end,
            @Param("attorney") User attorney);
}
