package com.haeil.be.cases.domain;

import com.haeil.be.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name="cases")
@Entity
@Getter
@NoArgsConstructor
public class Cases extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
