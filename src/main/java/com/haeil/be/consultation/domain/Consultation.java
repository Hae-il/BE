package com.haeil.be.consultation.domain;

import com.haeil.be.client.domain.Client;
import com.haeil.be.consultation.domain.type.ConsultationStatus;
import com.haeil.be.global.entity.BaseEntity;
import com.haeil.be.user.domain.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "consultation")
@Entity
@Getter
@NoArgsConstructor
public class Consultation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consult_req_id", nullable = false)
    private ConsultationRequest consultationRequest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "counselor_id", nullable = false)
    private User counselor;

    @Column(name = "consultation_date")
    private LocalDateTime consultationDate;

    @Column(name = "location")
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ConsultationStatus status;

    @OneToMany(mappedBy = "consultation", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ConsultationNote> consultationNotes = new ArrayList<>();
}
