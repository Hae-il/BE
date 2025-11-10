package com.haeil.be.consultation.repository;

import com.haeil.be.consultation.domain.ConsultationRequest;
import com.haeil.be.consultation.domain.type.ConsultationRequestStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultationRequestRepository extends JpaRepository<ConsultationRequest, Long> {

    List<ConsultationRequest> findByStatus(ConsultationRequestStatus status);

    List<ConsultationRequest> findByConsultLawyerIdAndStatus(
            Long consultLawyerId, ConsultationRequestStatus status);
}
