package com.haeil.full.consultation.repository;

import com.haeil.full.consultation.domain.ConsultationNote;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultationNoteRepository extends JpaRepository<ConsultationNote, Long> {

    List<ConsultationNote> findByConsultationIdOrderByCreatedDateDesc(Long consultationId);
}
