package com.haeil.be.cases.repository;

import com.haeil.be.cases.domain.Cases;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CasesRepository extends JpaRepository<Cases, Long> {
}
