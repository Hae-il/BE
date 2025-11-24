package com.haeil.be.cases.repository;

import com.haeil.be.cases.domain.Petition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetitionRepository extends JpaRepository<Petition, Long> {}
