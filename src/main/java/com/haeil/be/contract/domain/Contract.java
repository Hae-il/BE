package com.haeil.be.contract.domain;

import com.haeil.be.cases.domain.Cases;
import com.haeil.be.contract.domain.type.ContractStatus;
import com.haeil.be.global.entity.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "contract")
@Entity
@Getter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "fee_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Contract extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "contract_status")
    @Enumerated(EnumType.STRING)
    private ContractStatus status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id")
    private Cases cases;

    @Embedded private ExpenseInfo expenseInfo;

    public Contract(
            LocalDate dueDate, ContractStatus status, Cases cases, ExpenseInfo expenseInfo) {
        this.dueDate = dueDate;
        this.status = status;
        this.cases = cases;
        this.expenseInfo = expenseInfo;
    }
}
