package com.haeil.be.consultation.domain;

import com.haeil.be.client.domain.Client;
import com.haeil.be.consultation.domain.type.ConsultationStatus;
import com.haeil.be.global.entity.BaseEntity;
import com.haeil.be.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;

@Table(name="consultation")
@Entity
@Getter
@NoArgsConstructor
public class Consultation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="requested_time")
    private LocalDate requestedTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="requested_attorney_id")
    private User requestedAttorney;

    @Column(name="attachment")
    private String attachment;

    @Enumerated(EnumType.STRING)
    @Column(name="status")
    private ConsultationStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="client_id")
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="counselor_id")
    private User counselor;

    @OneToOne(mappedBy = "consultation", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private ConsultationNote consultationNote;
}
