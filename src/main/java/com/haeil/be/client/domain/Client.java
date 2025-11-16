package com.haeil.be.client.domain;

import com.haeil.be.client.domain.type.Gender;
import com.haeil.be.consultation.domain.Consultation;
import com.haeil.be.global.entity.BaseEntity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "job_title")
    private String jobTitle;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
    private List<Consultation> consultations = new ArrayList<>();

    @Builder
    public Client(
            String name,
            String email,
            String phone,
            String address,
            String residentNumber,
            java.time.LocalDate birthDate,
            Gender gender,
            String jobTitle) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.residentNumber = residentNumber;
        this.birthDate = birthDate;
        this.gender = gender;
        this.jobTitle = jobTitle;
    }
}
