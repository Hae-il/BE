package com.haeil.full.user.domain;

import com.haeil.full.cases.domain.Cases;
import com.haeil.full.consultation.domain.Consultation;
import com.haeil.full.global.entity.BaseEntity;
import com.haeil.full.user.domain.type.Role;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "users")
@Entity
@Getter
@NoArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "phone")
    private String phone;

    @Column(name = "resident_number", unique = true)
    private String residentNumber;

    @Column(name = "license_number")
    private String licenseNumber;

    @OneToMany(mappedBy = "attorney", fetch = FetchType.LAZY)
    private final List<Cases> casesList = new ArrayList<>();

    @OneToMany(mappedBy = "counselor", fetch = FetchType.LAZY)
    private final List<Consultation> consultationList = new ArrayList<>();

    @Builder
    public User(
            String name,
            String email,
            String password,
            Role role,
            String phone,
            String residentNumber,
            String licenseNumber) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.phone = phone;
        this.residentNumber = residentNumber;
        this.licenseNumber = licenseNumber;
    }
}
