package com.haeil.be.cases.domain;

import com.haeil.be.cases.domain.type.EventType;
import com.haeil.be.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name="case_event")
@Entity
@Getter
@NoArgsConstructor
public class CaseEvent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="location")
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(name="event_type")
    private EventType type;

    @Column(name="date")
    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="case_id")
    private Cases cases;
}
