package com.haeil.full.consultation.repository;

import com.haeil.full.consultation.domain.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultationRepository extends JpaRepository<Consultation, Long> {}
