package com.haeil.be.contract.domain;

import com.haeil.be.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name="contract")
@Entity
@Getter
@NoArgsConstructor
public class Contract extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
