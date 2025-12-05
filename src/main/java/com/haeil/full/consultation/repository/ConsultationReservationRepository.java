package com.haeil.full.consultation.repository;

import com.haeil.full.consultation.domain.ConsultationReservation;
import com.haeil.full.consultation.domain.type.ConsultationRequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultationReservationRepository
        extends JpaRepository<ConsultationReservation, Long> {
    Page<ConsultationReservation> findByStatus(ConsultationRequestStatus status, Pageable pageable);

    Page<ConsultationReservation> findAll(Pageable pageable);
}
