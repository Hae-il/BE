package com.haeil.full.cases.repository;

import com.haeil.full.cases.domain.Petition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetitionRepository extends JpaRepository<Petition, Long> {}
