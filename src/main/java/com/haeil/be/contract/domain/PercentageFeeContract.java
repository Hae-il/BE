package com.haeil.be.contract.domain;

import com.haeil.be.cases.domain.Cases;
import com.haeil.be.contract.domain.type.ContractStatus;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("PERCENTAGE")
@Getter
@NoArgsConstructor
public class PercentageFeeContract extends Contract {

    @Column(name = "target_amount")
    private BigDecimal targetAmount;

    @Column(name = "fee_percentage")
    private Long feePercentage;

    public PercentageFeeContract(
            LocalDate dueDate,
            ContractStatus status,
            Cases cases,
            ExpenseInfo expenseInfo,
            BigDecimal targetAmount,
            Long feePercentage) {
        super(dueDate, status, cases, expenseInfo);
        this.targetAmount = targetAmount;
        this.feePercentage = feePercentage;
    }
}
