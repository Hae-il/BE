package com.haeil.be.consultation.repository;

import com.haeil.be.consultation.domain.ConsultationNote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultationNoteRepository extends JpaRepository<ConsultationNote, Long> {}
