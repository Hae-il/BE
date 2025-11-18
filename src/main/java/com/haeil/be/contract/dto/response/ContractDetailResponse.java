package com.haeil.be.contract.dto.response;

import static com.haeil.be.contract.exception.errorcode.ContractErrorCode.INVALID_FEE_TYPE;

import com.haeil.be.contract.domain.Contract;
import com.haeil.be.contract.domain.FixedFeeContract;
import com.haeil.be.contract.domain.PercentageFeeContract;
import com.haeil.be.contract.domain.type.ContractStatus;
import com.haeil.be.contract.domain.type.FeeType;
import com.haeil.be.contract.exception.ContractException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ContractDetailResponse {

    private Long contractId;
    private Long caseId;
    private String caseNumber;
    private String caseName;
    private String attorneyName;
    private String clientName;
    private LocalDate dueDate;
    private ContractStatus status;
    private ExpenseInfoResponse expenseInfoResponse;

    private FeeType feeType;

    // 정액 (FIXED) 일 때만 사용되는 피드
    private List<ContractConditionResponse> fixedConditionList;

    // 정률 (PERCENTAGE) 일 때만 사용되는 필드
    private BigDecimal targetAmount;
    private Long feePercentage;

    public static ContractDetailResponse from(Contract contract) {

        ContractDetailResponseBuilder builder =
                ContractDetailResponse.builder()
                        .contractId(contract.getId())
                        .caseId(contract.getCases().getId())
                        .caseNumber(contract.getCases().getCaseNumber())
                        .clientName(contract.getCases().getClient().getName())
                        .attorneyName(contract.getCases().getAttorney().getName())
                        .dueDate(contract.getDueDate())
                        .status(contract.getStatus())
                        .expenseInfoResponse(ExpenseInfoResponse.from(contract.getExpenseInfo()));

        if (contract instanceof FixedFeeContract fixedContract) {

            List<ContractConditionResponse> conditions =
                    fixedContract.getContractConditionList().stream()
                            .map(ContractConditionResponse::from)
                            .collect(Collectors.toList());

            return builder.feeType(FeeType.FIXED).fixedConditionList(conditions).build();

        } else if (contract instanceof PercentageFeeContract percContract) {

            return builder.feeType(FeeType.PERCENTAGE)
                    .targetAmount(percContract.getTargetAmount())
                    .feePercentage(percContract.getFeePercentage())
                    .build();
        }
        throw new ContractException(INVALID_FEE_TYPE);
    }
}
