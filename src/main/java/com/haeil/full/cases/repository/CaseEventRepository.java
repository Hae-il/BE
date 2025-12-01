package com.haeil.full.cases.repository;

import com.haeil.full.cases.domain.CaseEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CaseEventRepository extends JpaRepository<CaseEvent, Long> {}
