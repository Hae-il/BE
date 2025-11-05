package com.haeil.be.user.domain;

import com.haeil.be.cases.domain.Cases;
import com.haeil.be.consultation.domain.Consultation;
import com.haeil.be.global.entity.BaseEntity;
import com.haeil.be.user.domain.type.Role;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Table(name="users")
@Entity
@Getter
@NoArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name")
    private String name;

    @Column(name="email")
    private String email;

    @Column(name="password")
    private String password;

    @Column(name="role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "attorney", fetch = FetchType.LAZY)
    private List<Cases> casesList = new ArrayList<>();

    @OneToMany(mappedBy = "counselor", fetch = FetchType.LAZY)
    private List<Consultation> consultationList = new ArrayList<>();

    @OneToMany(mappedBy = "requestedAttorney", fetch = FetchType.LAZY)
    private List<Consultation> consultationRequestList = new ArrayList<>();

    @Builder
    public User(String name, String email, String password, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
