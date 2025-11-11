package com.haeil.be.contract.domain;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@DiscriminatorValue("정률")
@Getter
@NoArgsConstructor
public class PercentageFeeContract extends Contract {

    @Column(name="target_amount")
    private BigDecimal targetAmount;

    @Column(name="fee_percentage")
    private Long feePercentage;
}
