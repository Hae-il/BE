package com.haeil.be.consultation.repository;

import com.haeil.be.consultation.domain.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultationRepository extends JpaRepository<Consultation, Long> {}
