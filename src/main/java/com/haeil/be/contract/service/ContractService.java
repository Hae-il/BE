package com.haeil.be.contract.service;

import com.haeil.be.cases.domain.Cases;
import com.haeil.be.cases.exception.CasesException;
import com.haeil.be.cases.repository.CasesRepository;
import com.haeil.be.contract.domain.Contract;
import com.haeil.be.contract.domain.ExpenseInfo;
import com.haeil.be.contract.domain.FixedFeeContract;
import com.haeil.be.contract.domain.PercentageFeeContract;
import com.haeil.be.contract.domain.type.ContractStatus;
import com.haeil.be.contract.domain.type.FeeType;
import com.haeil.be.contract.dto.request.ContractConditionRequest;
import com.haeil.be.contract.dto.request.ContractCreateRequest;
import com.haeil.be.contract.dto.request.ExpenseInfoRequest;
import com.haeil.be.contract.dto.response.ContractItemResponse;
import com.haeil.be.contract.dto.response.ContractResponse;
import com.haeil.be.contract.exception.ContractException;
import com.haeil.be.contract.repository.ContractRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.haeil.be.cases.exception.errorcode.CasesErrorCode.CASES_NOT_FOUND;
import static com.haeil.be.contract.exception.errorcode.ContractErrorCode.CONDITION_SHOULD_NOT_BE_NULL;
import static com.haeil.be.contract.exception.errorcode.ContractErrorCode.INVALID_FEE_TYPE;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final CasesRepository casesRepository;
    private final ContractRepository contractRepository;

    @Transactional
    public ContractResponse createContract(ContractCreateRequest request){
        Cases cases = findCasesOrThrow(request.caseId());
        ExpenseInfo expenseInfo = createExpenseInfo(request.expenseInfoRequest());
        Contract newContract;

        // FeeType에 따른 분기
        if (request.feeType() == FeeType.FIXED){
            newContract = createFixedFeeContract(request, cases, expenseInfo);
        } else if (request.feeType() == FeeType.PERCENTAGE){
            newContract = createPercentageFeeContract(request, cases, expenseInfo);
        } else {
            throw new ContractException(INVALID_FEE_TYPE);
        }

        Contract savedContract = contractRepository.save(newContract);
        return ContractResponse.from(savedContract);
    }

    private Cases findCasesOrThrow(Long caseId){
        return casesRepository.findById(caseId).orElseThrow(()-> new CasesException(CASES_NOT_FOUND));
    }

    private ExpenseInfo createExpenseInfo(ExpenseInfoRequest request){
        return new ExpenseInfo(request.expenseHandling(), request.expenseDetail());
    }

    // 정액 계약 생성 로직
    private FixedFeeContract createFixedFeeContract(ContractCreateRequest request, Cases cases, ExpenseInfo expenseInfo){
        List<ContractConditionRequest> conditionRequests = request.contractConditionRequestList();

        if (conditionRequests == null || conditionRequests.isEmpty()) {
            throw new ContractException(CONDITION_SHOULD_NOT_BE_NULL);
        }

        FixedFeeContract fixedContract = new FixedFeeContract(
                request.dueDate(),
                ContractStatus.AWAITING,
                cases,
                expenseInfo
        );

        conditionRequests.forEach(condReq -> fixedContract.addContractCondition(
                condReq.conditionDetail(),
                condReq.amount()
        ));

        return fixedContract;
    }

    // 정률 계약 생성 로직
    private PercentageFeeContract createPercentageFeeContract(ContractCreateRequest request, Cases cases, ExpenseInfo expenseInfo) {
        if (request.targetAmount() == null || request.feePercentage() == null) {
            throw new ContractException(CONDITION_SHOULD_NOT_BE_NULL);
        }

        return new PercentageFeeContract(
                request.dueDate(),
                ContractStatus.AWAITING,
                cases,
                expenseInfo,
                request.targetAmount(),
                request.feePercentage()
        );
    }

    @Transactional(readOnly = true)
    public Page<ContractItemResponse> getContractList(Pageable pageable){
        Page<Contract> contractPage = contractRepository.findAll(pageable);
        return contractPage.map(ContractItemResponse::from);
    }
}
