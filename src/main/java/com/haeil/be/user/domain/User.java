package com.haeil.be.user.domain;

import com.haeil.be.global.entity.BaseEntity;
import com.haeil.be.user.domain.type.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(name="role")
    @Enumerated(EnumType.STRING)
    private Role role;
}
