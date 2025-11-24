package com.haeil.full.contract.domain;

import com.haeil.full.cases.domain.Cases;
import com.haeil.full.contract.domain.type.ContractStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("FIXED")
@Getter
@NoArgsConstructor
public class FixedFeeContract extends Contract {

    @OneToMany(mappedBy = "fixedFeeContract", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContractCondition> contractConditionList = new ArrayList<>();

    @Builder
    public FixedFeeContract(
            LocalDate dueDate, ContractStatus status, Cases cases, ExpenseInfo expenseInfo) {
        super(dueDate, status, cases, expenseInfo);
    }

    public void addContractCondition(String conditionDetail, BigDecimal amount) {
        ContractCondition contractCondition = new ContractCondition(conditionDetail, amount, this);
        this.contractConditionList.add(contractCondition);
    }
}
