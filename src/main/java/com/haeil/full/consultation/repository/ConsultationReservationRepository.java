package com.haeil.full.consultation.repository;

import com.haeil.full.consultation.domain.ConsultationReservation;
import com.haeil.full.consultation.domain.type.ConsultationRequestStatus;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultationReservationRepository
        extends JpaRepository<ConsultationReservation, Long> {
    List<ConsultationReservation> findByStatus(ConsultationRequestStatus status, Sort sort);
}
