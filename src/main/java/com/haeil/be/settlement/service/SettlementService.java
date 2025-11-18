package com.haeil.be.settlement.service;

import com.haeil.be.cases.domain.Cases;
import com.haeil.be.cases.domain.type.CaseStatus;
import com.haeil.be.cases.exception.CasesException;
import com.haeil.be.cases.exception.errorcode.CasesErrorCode;
import com.haeil.be.cases.repository.CasesRepository;
import com.haeil.be.settlement.domain.Settlement;
import com.haeil.be.settlement.domain.type.PaymentStatus;
import com.haeil.be.settlement.dto.request.CreateSettlementRequest;
import com.haeil.be.settlement.dto.request.UpdateSettlementRequest;
import com.haeil.be.settlement.dto.response.SettlementListResponse;
import com.haeil.be.settlement.dto.response.SettlementResponse;
import com.haeil.be.settlement.exception.SettlementException;
import com.haeil.be.settlement.exception.errorcode.SettlementErrorCode;
import com.haeil.be.settlement.repository.SettlementRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SettlementService {

    private final SettlementRepository settlementRepository;
    private final CasesRepository casesRepository;

    /**
     * 정산 관리 리스트 조회 완료된 사건 기준으로 정산서 리스트를 조회합니다. 정산서가 없는 사건도 포함하여 반환합니다. 프론트에서 settlementId가 null이면
     * "정산서 작성" 버튼을 활성화할 수 있습니다.
     *
     * @return 정산 리스트 정보 (사건 정보 + 정산서 정보, 정산서가 없으면 null)
     */
    @Transactional(readOnly = true)
    public List<SettlementListResponse> getSettlements() {
        List<Object[]> results =
                settlementRepository.findSettlementsByCaseStatus(CaseStatus.COMPLETED);

        return results.stream().map(SettlementListResponse::from).collect(Collectors.toList());
    }

    /**
     * 정산서 작성 사건 ID 기준으로 관련 사건을 조회한 후 Settlement 엔티티를 생성합니다. 초기 paymentStatus는 입금 대기(PENDING)로
     * 설정됩니다.
     *
     * @param request 정산서 생성 요청 DTO
     * @return 생성된 정산서 정보
     */
    @Transactional
    public SettlementResponse createSettlement(CreateSettlementRequest request) {
        Cases cases =
                casesRepository
                        .findById(request.getCaseId())
                        .orElseThrow(() -> new CasesException(CasesErrorCode.CASE_NOT_FOUND));

        Settlement settlement =
                Settlement.builder()
                        .paymentStatus(PaymentStatus.PENDING) // 초기값: 입금 대기
                        .lawyerFee(request.getLawyerFee())
                        .total(request.getTotal())
                        .expenses(request.getExpenses())
                        .isVatIncluded(
                                request.getIsVatIncluded() != null
                                        ? request.getIsVatIncluded()
                                        : false)
                        .clientReceivable(null) // 자동 계산됨
                        .settlementDate(request.getSettlementDate())
                        .dueDate(request.getDueDate())
                        .note(request.getNote())
                        .cases(cases)
                        .build();

        // clientReceivable 자동 계산
        settlement.recalculateClientReceivable();

        Settlement savedSettlement = settlementRepository.save(settlement);
        return SettlementResponse.from(savedSettlement);
    }

    /**
     * 정산서 상세 조회 정산서 ID로 상세 정보를 조회합니다.
     *
     * @param id 정산서 ID
     * @return 정산서 상세 정보
     * @throws SettlementException 정산서가 존재하지 않는 경우
     */
    @Transactional(readOnly = true)
    public SettlementResponse getSettlement(Long id) {
        Settlement settlement =
                settlementRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new SettlementException(
                                                SettlementErrorCode.SETTLEMENT_NOT_FOUND));
        return SettlementResponse.from(settlement);
    }

    /**
     * 정산서 수정 수정 가능한 필드만 업데이트합니다.
     *
     * @param id 정산서 ID
     * @param request 정산서 수정 요청 DTO
     * @return 수정된 정산서 정보
     * @throws SettlementException 정산서가 존재하지 않는 경우
     */
    @Transactional
    public SettlementResponse updateSettlement(Long id, UpdateSettlementRequest request) {
        Settlement settlement =
                settlementRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new SettlementException(
                                                SettlementErrorCode.SETTLEMENT_NOT_FOUND));

        settlement.update(
                request.getLawyerFee(),
                request.getTotal(),
                request.getExpenses(),
                request.getIsVatIncluded(),
                request.getClientReceivable(),
                request.getSettlementDate(),
                request.getDueDate(),
                request.getNote());

        Settlement updatedSettlement = settlementRepository.save(settlement);
        return SettlementResponse.from(updatedSettlement);
    }

    /**
     * 정산 상태 변경 정산서의 결제 상태를 변경합니다.
     *
     * @param id 정산서 ID
     * @param paymentStatus 변경할 결제 상태
     * @return 수정된 정산서 정보
     * @throws SettlementException 정산서가 존재하지 않는 경우
     */
    @Transactional
    public SettlementResponse updatePaymentStatus(Long id, PaymentStatus paymentStatus) {
        Settlement settlement =
                settlementRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new SettlementException(
                                                SettlementErrorCode.SETTLEMENT_NOT_FOUND));

        settlement.changePaymentStatus(paymentStatus);

        Settlement updatedSettlement = settlementRepository.save(settlement);
        return SettlementResponse.from(updatedSettlement);
    }
}
