package com.haeil.be.contract.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "contract_condition")
@Getter
@NoArgsConstructor
public class ContractCondition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "condition_detail")
    private String conditionDetail;

    @Column(name = "amount")
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id")
    private FixedFeeContract fixedFeeContract;

    public ContractCondition(
            String conditionDetail, BigDecimal amount, FixedFeeContract fixedFeeContract) {
        this.conditionDetail = conditionDetail;
        this.amount = amount;
        this.fixedFeeContract = fixedFeeContract;
    }
}
