package com.haeil.be.client.domain;

import com.haeil.be.consultation.domain.Consultation;
import com.haeil.be.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Table(name="client")
@Entity
@Getter
@NoArgsConstructor
public class Client extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name")
    private String name;

    @Column(name="email")
    private String email;

    @Column(name="phone")
    private String phone;

    @Column(name="address")
    private String address;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
    private List<Consultation> consultations = new ArrayList<>();
}
