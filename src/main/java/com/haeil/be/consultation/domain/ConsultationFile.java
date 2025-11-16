package com.haeil.be.consultation.domain;

import com.haeil.be.file.domain.FileEntity;
import com.haeil.be.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "consultation_file")
@Entity
@Getter
@NoArgsConstructor
public class ConsultationFile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consultation_id", nullable = false)
    private Consultation consultation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id", nullable = false)
    private FileEntity file;

    @Column(name = "description")
    private String description;

    @Builder
    public ConsultationFile(Consultation consultation, FileEntity file, String description) {
        this.consultation = consultation;
        this.file = file;
        this.description = description;
    }
}
