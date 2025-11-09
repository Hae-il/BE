package com.haeil.be.client.domain;

import com.haeil.be.consultation.domain.Consultation;
import com.haeil.be.global.entity.BaseEntity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "client")
@Entity
@Getter
@NoArgsConstructor
public class Client extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "resident_number", unique = true)
    private String residentNumber;

    @Column(name = "birth_date")
    private java.time.LocalDate birthDate;

    @Column(name = "gender")
    private String gender;

    @Column(name = "job_title")
    private String jobTitle;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
    private List<Consultation> consultations = new ArrayList<>();
}
