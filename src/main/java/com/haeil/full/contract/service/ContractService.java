package com.haeil.full.contract.service;

import static com.haeil.full.cases.exception.errorcode.CasesErrorCode.CASE_NOT_FOUND;
import static com.haeil.full.contract.exception.errorcode.ContractErrorCode.*;
import static com.haeil.full.contract.exception.errorcode.ContractErrorCode.CAN_NOT_CHANGE_STATUS;

import com.haeil.full.cases.domain.Cases;
import com.haeil.full.cases.exception.CasesException;
import com.haeil.full.cases.repository.CasesRepository;
import com.haeil.full.contract.domain.Contract;
import com.haeil.full.contract.domain.ExpenseInfo;
import com.haeil.full.contract.domain.FixedFeeContract;
import com.haeil.full.contract.domain.PercentageFeeContract;
import com.haeil.full.contract.domain.type.ContractStatus;
import com.haeil.full.contract.domain.type.FeeType;
import com.haeil.full.contract.dto.request.ContractConditionRequest;
import com.haeil.full.contract.dto.request.ContractCreateRequest;
import com.haeil.full.contract.dto.request.ContractStatusUpdateRequest;
import com.haeil.full.contract.dto.request.ExpenseInfoRequest;
import com.haeil.full.contract.dto.response.ContractDetailResponse;
import com.haeil.full.contract.dto.response.ContractItemResponse;
import com.haeil.full.contract.exception.ContractException;
import com.haeil.full.contract.repository.ContractRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final CasesRepository casesRepository;
    private final ContractRepository contractRepository;

    @Transactional
    public ContractDetailResponse createContract(ContractCreateRequest request) {
        Cases cases = findCasesOrThrow(request.caseId());
        ExpenseInfo expenseInfo = createExpenseInfo(request.expenseInfoRequest());
        Contract newContract;

        // FeeType에 따른 분기
        if (request.feeType() == FeeType.FIXED) {
            newContract = createFixedFeeContract(request, cases, expenseInfo);
        } else if (request.feeType() == FeeType.PERCENTAGE) {
            newContract = createPercentageFeeContract(request, cases, expenseInfo);
        } else {
            throw new ContractException(INVALID_FEE_TYPE);
        }

        Contract savedContract = contractRepository.save(newContract);
        return ContractDetailResponse.from(savedContract);
    }

    @Transactional(readOnly = true)
    public Page<ContractItemResponse> getContractList(ContractStatus status, Pageable pageable) {
        Pageable sortedPageable = pageable;
        if (pageable.getSort().isUnsorted()) {
            sortedPageable =
                    org.springframework.data.domain.PageRequest.of(
                            pageable.getPageNumber(),
                            pageable.getPageSize(),
                            org.springframework.data.domain.Sort.by(
                                    org.springframework.data.domain.Sort.Order.asc("id")));
        }

        Page<Cases> casesPage = casesRepository.findByContractStatus(status, sortedPageable);
        return casesPage.map(ContractItemResponse::from);
    }

    @Transactional(readOnly = true)
    public ContractDetailResponse getContractDetail(Long contractId) {
        Contract contract = findContractOrThrow(contractId);
        return ContractDetailResponse.from(contract);
    }

    @Transactional
    public void updateStatus(Long contractId, ContractStatusUpdateRequest request) {
        Contract contract = findContractOrThrow(contractId);
        ContractStatus currentStatus = contract.getStatus();
        ContractStatus targetStatus = ContractStatus.valueOf(request.contractStatus());
        if (!isValidStatusTransition(currentStatus, targetStatus)) {
            throw new ContractException(CAN_NOT_CHANGE_STATUS);
        }
        contract.updateStatus(targetStatus);
    }

    private Cases findCasesOrThrow(Long caseId) {
        return casesRepository
                .findById(caseId)
                .orElseThrow(() -> new CasesException(CASE_NOT_FOUND));
    }

    private ExpenseInfo createExpenseInfo(ExpenseInfoRequest request) {
        return new ExpenseInfo(request.expenseHandling(), request.expenseDetail());
    }

    private Contract findContractOrThrow(Long contractId) {
        return contractRepository
                .findById(contractId)
                .orElseThrow(() -> new ContractException(CONTRACT_NOT_FOUND));
    }

    private boolean isValidStatusTransition(
            ContractStatus currentStatus, ContractStatus targetStatus) {
        if (currentStatus == targetStatus) {
            return false;
        }
        return true;
    }

    // 정액 계약 생성 로직
    private FixedFeeContract createFixedFeeContract(
            ContractCreateRequest request, Cases cases, ExpenseInfo expenseInfo) {
        List<ContractConditionRequest> conditionRequests = request.contractConditionRequestList();

        if (conditionRequests == null || conditionRequests.isEmpty()) {
            throw new ContractException(CONDITION_SHOULD_NOT_BE_NULL);
        }

        FixedFeeContract fixedContract =
                new FixedFeeContract(
                        request.dueDate(), ContractStatus.AWAITING, cases, expenseInfo);

        conditionRequests.forEach(
                condReq ->
                        fixedContract.addContractCondition(
                                condReq.conditionDetail(), condReq.amount()));

        return fixedContract;
    }

    // 정률 계약 생성 로직
    private PercentageFeeContract createPercentageFeeContract(
            ContractCreateRequest request, Cases cases, ExpenseInfo expenseInfo) {
        if (request.targetAmount() == null || request.feePercentage() == null) {
            throw new ContractException(CONDITION_SHOULD_NOT_BE_NULL);
        }

        return new PercentageFeeContract(
                request.dueDate(),
                ContractStatus.AWAITING,
                cases,
                expenseInfo,
                request.targetAmount(),
                request.feePercentage());
    }
}
