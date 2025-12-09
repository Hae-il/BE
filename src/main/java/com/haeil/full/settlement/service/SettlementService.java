package com.haeil.full.settlement.service;

import com.haeil.full.cases.domain.Cases;
import com.haeil.full.cases.domain.type.CaseStatus;
import com.haeil.full.cases.exception.CasesException;
import com.haeil.full.cases.exception.errorcode.CasesErrorCode;
import com.haeil.full.cases.repository.CasesRepository;
import com.haeil.full.settlement.domain.Settlement;
import com.haeil.full.settlement.domain.type.PaymentStatus;
import com.haeil.full.settlement.domain.type.SettlementStatus;
import com.haeil.full.settlement.dto.request.CreateSettlementRequest;
import com.haeil.full.settlement.dto.request.UpdateSettlementRequest;
import com.haeil.full.settlement.dto.response.SettlementListResponse;
import com.haeil.full.settlement.dto.response.SettlementResponse;
import com.haeil.full.settlement.exception.SettlementException;
import com.haeil.full.settlement.exception.errorcode.SettlementErrorCode;
import com.haeil.full.settlement.repository.SettlementRepository;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SettlementService {

    private final SettlementRepository settlementRepository;
    private final CasesRepository casesRepository;

    /**
     * 정산 관리 리스트 조회 진행중 또는 완료된 사건 기준으로 정산서 리스트를 조회합니다. 정산서가 없는 사건도 포함하여 반환합니다. 프론트에서 settlementId가
     * null이면 "정산서 작성" 버튼을 활성화할 수 있습니다.
     *
     * @return 정산 리스트 정보 (사건 정보 + 정산서 정보, 정산서가 없으면 null)
     */
    @Transactional(readOnly = true)
    public Page<SettlementListResponse> getSettlements(
            String attorneyName, PaymentStatus paymentStatus, Pageable pageable) {

        Pageable sortedPageable = pageable;
        if (pageable.getSort().isUnsorted()) {
            sortedPageable =
                    PageRequest.of(
                            pageable.getPageNumber(),
                            pageable.getPageSize(),
                            Sort.by(Sort.Order.desc("c.id"))); // Cases ID default sort
        }

        Page<Object[]> results =
                settlementRepository.findSettlementsByCaseStatuses(
                        Arrays.asList(CaseStatus.IN_PROGRESS, CaseStatus.COMPLETED),
                        attorneyName,
                        paymentStatus,
                        sortedPageable);

        return results.map(SettlementListResponse::from);
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
                        .settlementStatus(SettlementStatus.NONE) // 초기값: NONE
                        .attorneyFee(request.getAttorneyFee())
                        .agreementAmount(request.getAgreementAmount())
                        .expenses(request.getExpenses())
                        .isVatIncluded(
                                request.getIsVatIncluded() != null
                                        ? request.getIsVatIncluded()
                                        : false)
                        .clientReceivable(null) // 자동 계산됨
                        .settlementDate(request.getSettlementDate())
                        .paymentDueDate(request.getPaymentDueDate())
                        .note(request.getNote())
                        .cases(cases)
                        .build();

        // 상태 변경 규칙 적용 (NONE → DRAFT → FINAL)
        settlement.updateSettlementStatus();

        // FINAL 상태가 아니면 입금 대기로 변경할 수 없으므로 초기 상태 유지 또는 조정 필요
        // 하지만 요구사항에 따르면 "FINAL이 될 조건이 채워지지 않았다면 입금 상태를 작성중으로 변경"
        // 여기서 입금 상태는 PaymentStatus, 작성 상태는 SettlementStatus.
        // 요구사항 해석: "입금 상태(PaymentStatus)를 작성중(SettlementStatus.DRAFT)으로 변경"은 논리적으로 맞지 않음.
        // 아마도 SettlementStatus가 DRAFT일 때 PaymentStatus도 '작성중'에 해당하는 상태여야 한다는 의미일 수 있음.
        // 하지만 PaymentStatus에는 '작성중'이 없음. (PENDING, COMPLETED 등)
        // 요구사항: "FINAL이 될 조건이 채워지지 않았다면 입금 상태를 작성중으로 변경해주세요."
        // PaymentStatus에 DRAFT를 추가하거나, SettlementStatus가 DRAFT일 때 PaymentStatus를 PENDING(입금대기)가 아닌
        // 다른 값으로 설정해야 함.
        // PaymentStatus Enum에 'WRITING' 또는 'DRAFT' 추가 필요해 보임.
        // 현재 PaymentStatus: PENDING("입금대기"), COMPLETED("입금완료")
        // 수정: PaymentStatus가 아니라 SettlementStatus를 의미하는 것일 수 있음.
        // "정산서의 FINAL이 될 조건이 채워지지 않았다면 입금 상태를 작성중으로 변경해주세요." -> SettlementStatus를 DRAFT로 설정하는 것은 이미
        // updateSettlementStatus()에서 처리됨.

        // 만약 사용자가 "입금 상태"라고 말한 것이 UI 상의 표시를 의미한다면?
        // 일단 SettlementStatus 업데이트 로직은 이미 존재함.
        // 추가로 PaymentStatus 로직을 확인.

        if (settlement.getSettlementStatus() != SettlementStatus.FINAL) {
            // FINAL이 아니면 PaymentStatus를 무엇으로?
            // 기존 코드: paymentStatus(PaymentStatus.PENDING)
            // 요구사항대로라면 PaymentStatus에도 '작성중' 상태가 필요할 수 있음.
            // 하지만 User Query 4번 "Settlement.java를 참고하여 정산서의 FINAL이 될 조건이 채워지지 않았다면 입금 상태를 작성중으로
            // 변경해주세요."
            // 문맥상 SettlementStatus를 DRAFT로 유지하라는 의미가 강함. (이미 구현됨)
            // 혹시 PaymentStatus를 PENDING으로 두는 것을 변경하라는 의미라면?
            // -> SettlementStatus가 FINAL이 아니면 결제 상태는 의미가 없으므로 PENDING 유지.
        }

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
                request.getAttorneyFee(),
                request.getAgreementAmount(),
                request.getExpenses(),
                request.getIsVatIncluded(),
                request.getSettlementDate(),
                request.getPaymentDueDate(),
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
