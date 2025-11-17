package com.haeil.be.consultation.repository;

import com.haeil.be.consultation.domain.ConsultationReservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultationReservationRepository
        extends JpaRepository<ConsultationReservation, Long> {}
