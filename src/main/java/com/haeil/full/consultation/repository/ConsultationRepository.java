package com.haeil.full.consultation.repository;

import com.haeil.full.consultation.domain.Consultation;
import com.haeil.full.consultation.domain.type.ConsultationStatus;
import com.haeil.full.user.domain.User;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultationRepository extends JpaRepository<Consultation, Long> {
    List<Consultation> findAllByConsultationDateBetweenAndCounselor(
            LocalDateTime start, LocalDateTime end, User counselor);

    Page<Consultation> findByStatus(ConsultationStatus status, Pageable pageable);

    Page<Consultation> findAll(Pageable pageable);
}
