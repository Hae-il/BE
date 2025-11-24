package com.haeil.full.consultation.repository;

import com.haeil.full.consultation.domain.ConsultationReservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultationReservationRepository
        extends JpaRepository<ConsultationReservation, Long> {}
