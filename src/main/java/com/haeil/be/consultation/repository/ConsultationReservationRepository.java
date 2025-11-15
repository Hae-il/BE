package com.haeil.be.consultation.repository;

import com.haeil.be.consultation.domain.ConsultationReservation;
import com.haeil.be.consultation.domain.type.ConsultationRequestStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultationReservationRepository extends JpaRepository<ConsultationReservation, Long> {
}
