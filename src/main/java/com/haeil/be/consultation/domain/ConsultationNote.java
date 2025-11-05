package com.haeil.be.consultation.domain;

import com.haeil.be.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name="consultation_note")
@Entity
@Getter
@NoArgsConstructor
public class ConsultationNote extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="summary")
    private String summary;

    @Column(name="legal_opinion")
    private String legalOpinion;

    @Column(name="plan")
    private String plan;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="consultation_id", unique = true)
    private Consultation consultation;
}
