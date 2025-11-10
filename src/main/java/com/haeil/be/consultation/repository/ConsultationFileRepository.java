package com.haeil.be.consultation.repository;

import com.haeil.be.consultation.domain.ConsultationFile;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultationFileRepository extends JpaRepository<ConsultationFile, Long> {
    List<ConsultationFile> findByConsultationId(Long consultationId);
}
